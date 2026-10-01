package net.kenji.first_person_auto_switch.compat;

import net.kenji.first_person_auto_switch.PerspectiveManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
