package net.kenji.auto_perspective.compat;

import net.kenji.auto_perspective.PerspectiveManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.TickEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class EpicFightCompat {





    public static void tickEpicFightCompat(TickEvent.ClientTickEvent event){
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
            return;
        }

        PerspectiveManager.setBlockMiningTimerSwitch(false);
    }


}
