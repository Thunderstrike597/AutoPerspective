package net.kenji.auto_perspective.compat;

import net.kenji.auto_perspective.PerspectiveManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.TickEvent;
import org.jline.utils.Log;
import yesman.epicfight.api.client.event.EpicFightClientHooks;
import yesman.epicfight.api.client.event.types.LockOnEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class EpicFightCompat {
    private static long clientTicks = 0;
    private static long lastLockOnTick = Long.MIN_VALUE / 2;
    private static boolean hasLockonTarget = false;
    public static boolean getHasLockonTarget(){
        return hasLockonTarget;
    };

    public static void init(){
        EpicFightClientHooks.Camera.LOCK_ON_START.registerEvent(EpicFightCompat::onLockonStart);
        EpicFightClientHooks.Camera.LOCK_ON_RELEASED.registerEvent(EpicFightCompat::onLockonEnd);
    }

    public static void tickEpicFightCompat(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();

        Player player = mc.player;
        if(player == null)return;

        PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
        if(patch == null)return;

        if(patch.isEpicFightMode()){
            if(patch.getOriginal().getMainHandItem().getItem() instanceof SwordItem swordItem){
                PerspectiveManager.setBlockMiningTimerSwitch(true);
            }
            else PerspectiveManager.setBlockMiningTimerSwitch(false);

        }
        else PerspectiveManager.setBlockMiningTimerSwitch(false);

        clientTicks++;
    }

    public static void onLockonStart(LockOnEvent.Tick.Start event) {
        hasLockonTarget = true;
    }

    public static void onLockonEnd(LockOnEvent.Tick.Release event) {
        hasLockonTarget = false;
    }

}
