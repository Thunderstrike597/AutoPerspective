package net.kenji.auto_perspective.compat;

import net.kenji.auto_perspective.PerspectiveManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.api.client.event.types.camera.LockOnEvent;
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
        EpicFightClientEventHooks.Camera.LOCK_ON_TICK.registerEvent(EpicFightCompat::tickLockOn);
    }

    public static void tickEpicFightCompat(ClientTickEvent.Post event){
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

        hasLockonTarget = (clientTicks - lastLockOnTick) <= 2;

    }

    public static void tickLockOn(LockOnEvent.Tick event) {
        lastLockOnTick = clientTicks;
    }

}
