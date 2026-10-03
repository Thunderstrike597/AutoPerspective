package net.kenji.auto_perspective;

import net.kenji.auto_perspective.api.SwitchCauseType;
import net.kenji.auto_perspective.compat.EffortlessBuildingCompat;
import net.kenji.auto_perspective.compat.SophistocatedBuildingCompat;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class PerspectiveManager extends CompatManager{

    private static final double THIRD_PERSON_DIST = 4.0;  // vanilla; raise if you use a zoom mod
    private static final double ENTER_RATIO = 0.6;        // obstructed when clear distance < 60% of desired
    private static final double EXIT_RATIO = 0.85;        // clear again only when > 85% (hysteresis)
    private static final double OCCLUSION_MAX_DIST = 3.5;  // only care about targets this close to the eye
    private static final double OCCLUDE_ENTER = 0.6;       // fraction of sample points hidden to trigger
    private static final double OCCLUDE_EXIT = 0.4;        // must drop below this to release (hysteresis)

    // Tighter than before; only catches genuinely tiny sealed spaces now
    private static final FloodFillRoomDetector room = new FloodFillRoomDetector();
    private static boolean suspended = false;   // player overrode us; wait until the area is clear
    private static int ticksObstructed = 0;
    private static int ticksClear = 0;

    private static int miningTimer = -1;

    private static PerspectiveSwitchData switchData = PerspectiveSwitchData.init();

    private static boolean blockMiningSwitch = false;
    private static boolean renderFirstPersonHand;

    public static void setRenderFirstPersonHand(boolean value) {
        renderFirstPersonHand = value;}
    public static boolean shouldRenderFirstPersonHand() {
        return renderFirstPersonHand;
    }

    public static void setBlockMiningTimerSwitch(boolean v){
        blockMiningSwitch = v;
    }
    public static boolean getBlockMiningSwitch(){
        return blockMiningSwitch;
    }

    public static class PerspectiveSwitchData{
        private final CameraType previous;  // non-null = this mod currently owns the switch
        private final SwitchCauseType lastSwitchCauseType;

        public PerspectiveSwitchData(@Nullable CameraType camType, @NotNull SwitchCauseType switchType){
            previous = camType;
            lastSwitchCauseType = switchType;
        }
        public PerspectiveSwitchData from(SwitchCauseType type){
            return new PerspectiveSwitchData(Minecraft.getInstance().options.getCameraType(), type);
        }
        public PerspectiveSwitchData reset(){
            return new PerspectiveSwitchData(null, SwitchCauseType.NONE);
        }
        public static PerspectiveSwitchData init(){
            return new PerspectiveSwitchData(null, SwitchCauseType.NONE);
        }

    }

    public static void tickPerspectiveManager(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if(miningTimer == -1){
            miningTimer = ConfigClient.MAX_MINING_TIMER.get();
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) {
            reset();
            return;
        }
        CameraType cam = mc.options.getCameraType();
        SwitchCauseType switchCauseType = getObstruction(mc, player, cam);
        if (switchCauseType != SwitchCauseType.NONE) {
            ticksObstructed++;
            ticksClear = 0;
        } else {
            ticksClear++;
            ticksObstructed = 0;
        }

        if (switchData.previous == null) {
            // Not active
            if (suspended) {
                if (ticksClear >= switchData.lastSwitchCauseType.getExitTicksFromSwitchType()) suspended = false;
                return;
            }
            if (cam.isFirstPerson()) return;

            if (ticksObstructed >= ConfigClient.ENTER_TICKS.get()) {
                switchData = switchData.from(switchCauseType);
                syncPlayerRotationToCamera(mc, player, cam);
                mc.options.setCameraType(CameraType.FIRST_PERSON);

            }
        } else {
            // Active: we switched to first person
            if (!cam.isFirstPerson()) {
                // Player pressed F5 themselves, so give up ownership and don't fight them
                switchData = switchData.reset();
                suspended = true;
                return;
            }
            if (ticksClear >= switchData.lastSwitchCauseType.getExitTicksFromSwitchType()) {
                mc.options.setCameraType(switchData.previous);
                switchData = switchData.reset();
            }
        }
    }
    public static boolean isLookingAtBlock(Minecraft mc){

        if (mc.player != null) {
            // 20.0D is the maximum distance in blocks
            HitResult result = mc.player.pick(mc.player.getPickRadius(), 0.0F, false);
            return result.getType() == HitResult.Type.BLOCK;
        }
        return false;
    }

    public static boolean isBuildingMode(Minecraft mc){
        if(isEffortlessBuildngPresent() && ConfigClient.USE_EFFORTLESS_BUILDING_MODE.get()){
           return EffortlessBuildingCompat.isInBuildingMode(mc);
        }
        if(isSophistocatedBuildngPresent() && ConfigClient.USE_EFFORTLESS_BUILDING_MODE.get()){
            return SophistocatedBuildingCompat.isInBuildingMode(mc);
        }
        return false;
    }


    public static boolean isMiningBlock(Minecraft mc){
        if(mc.gameMode != null) {
            if (mc.gameMode.isDestroying() || (!blockMiningSwitch && mc.options.keyAttack.isDown() && isLookingAtBlock(mc))){
                if(miningTimer > 0)
                    miningTimer--;
            }
            else {
                miningTimer = ConfigClient.MAX_MINING_TIMER.get();
            }
        }
        return miningTimer <= 0 && ConfigClient.USE_MINING_TIMER.get();
    }


    private static SwitchCauseType getObstruction(Minecraft mc, Player player, CameraType cam) {
        CameraType ref = switchData.previous != null ? switchData.previous : cam;
        if (ref.isFirstPerson()) return SwitchCauseType.NONE;
        boolean isMiningBlock = isMiningBlock(mc);
        boolean isBuildingMode = isBuildingMode(mc);

        if(isMiningBlock)
            return SwitchCauseType.BLOCK_MINED;
        if(isBuildingMode)
            return SwitchCauseType.BUILD_MODE;

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 back = ref == CameraType.THIRD_PERSON_FRONT ? look : look.scale(-1.0);

        if(isUnderCeiling(mc, player, eye)){
            return SwitchCauseType.CLOSED_SPACE;
        }

        double clear = clearCameraDistance(mc, player, eye, back);
        if (clear / THIRD_PERSON_DIST < (switchData.previous != null ? EXIT_RATIO : ENTER_RATIO)) return SwitchCauseType.VIEW_OBSTRUCTED;

        // Where the third-person camera would be right now
        Vec3 camPos = eye.add(back.scale(clear));
        if (targetHiddenByBody(mc, player, eye, camPos)) return SwitchCauseType.VIEW_OBSTRUCTED;

        BlockPos eyePos = BlockPos.containing(eye);
        return room.get(mc.level, eyePos).enclosed() ? SwitchCauseType.CLOSED_SPACE : SwitchCauseType.NONE;
    }

    private static boolean isUnderCeiling(Minecraft mc, Player player, Vec3 eye) {
        if (!ConfigClient.USE_CEILING_CHECK.get()) return false;

        int maxDist = ConfigClient.MAX_CEILING_CHECK_DIST.get();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = 1; i <= maxDist; i++) {
            pos.set(Mth.floor(eye.x), Mth.floor(eye.y) + i, Mth.floor(eye.z));
            if (mc.level.isOutsideBuildHeight(pos)) return false;

            BlockState state = mc.level.getBlockState(pos);
            if (state.isAir()) continue;

            // Anything with collision counts: slabs, stairs, trapdoors, glass, etc.
            if (!state.getCollisionShape(mc.level, pos).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static boolean targetHiddenByBody(Minecraft mc, Player player, Vec3 eye, Vec3 camPos) {
        if (!(mc.hitResult instanceof BlockHitResult bhr) || bhr.getType() != HitResult.Type.BLOCK) return false;
        if (bhr.getLocation().distanceTo(eye) > OCCLUSION_MAX_DIST) return false;

        BlockPos bp = bhr.getBlockPos();
        AABB body = player.getBoundingBox();

        int blocked = 0;
        for (int i = 0; i < 9; i++) {
            double x, y, z;
            if (i == 8) { x = y = z = 0.5; }  // center
            else {
                x = (i & 1) == 0 ? 0.2 : 0.8;
                y = (i >> 1 & 1) == 0 ? 0.2 : 0.8;
                z = (i >> 2 & 1) == 0 ? 0.2 : 0.8;
            }
            Vec3 target = new Vec3(bp.getX() + x, bp.getY() + y, bp.getZ() + z);
            if (body.clip(camPos, target).isPresent()) blocked++;
        }

        double frac = blocked / 9.0;
        return frac >= (switchData.previous != null ? OCCLUDE_EXIT : OCCLUDE_ENTER);
    }

    private static double clearCameraDistance(Minecraft mc, Player player, Vec3 eye, Vec3 back) {
        double max = THIRD_PERSON_DIST;
        for (int i = 0; i < 8; i++) {
            double ox = ((i & 1) * 2 - 1) * 0.1;
            double oy = ((i >> 1 & 1) * 2 - 1) * 0.1;
            double oz = ((i >> 2 & 1) * 2 - 1) * 0.1;

            Vec3 from = eye.add(ox, oy, oz);
            Vec3 to = eye.add(back.scale(THIRD_PERSON_DIST)).add(ox + oz, oy, oz);

            HitResult hit = mc.level.clip(new ClipContext(
                    from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
            if (hit.getType() != HitResult.Type.MISS) {
                double d = hit.getLocation().distanceTo(eye);
                if (d < max) max = d;
            }
        }
        return max;
    }

    private static void syncPlayerRotationToCamera(Minecraft mc, Player player, CameraType cam) {
        Camera camera = mc.gameRenderer.getMainCamera();
        float yaw = camera.getYRot();
        float pitch = camera.getXRot();

        // Front view flips the camera 180 degrees and negates pitch, so undo that
        if (cam == CameraType.THIRD_PERSON_FRONT) {
            yaw += 180.0F;
            pitch = -pitch;
        }

        yaw = Mth.wrapDegrees(yaw);
        pitch = Mth.clamp(pitch, -90.0F, 90.0F);

        player.setYRot(yaw);
        player.setXRot(pitch);
        // Previous-tick values too, otherwise the render interpolates and you get a visible snap
        player.yRotO = yaw;
        player.xRotO = pitch;
        player.setYHeadRot(yaw);
        player.yHeadRotO = yaw;
    }

    public static int getLight(Player player) {
        return player.level().getBrightness(LightLayer.SKY, BlockPos.containing(player.getEyePosition()));
    }



    private static void reset() {
        switchData = switchData.reset();
        suspended = false;
        ticksObstructed = 0;
        ticksClear = 0;
        room.invalidate();
    }
}