package net.kenji.first_person_auto_switch.events;

import net.kenji.first_person_auto_switch.CompatManager;
import net.kenji.first_person_auto_switch.FirstPersonAutoSwitch;
import net.kenji.first_person_auto_switch.PerspectiveManager;
import net.kenji.first_person_auto_switch.compat.EpicFightCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = FirstPersonAutoSwitch.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents extends CompatManager {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        PerspectiveManager.tickPerspectiveManager(event);

        if(isEpicFightPresent()){
            EpicFightCompat.tickEpicFightCompat(event);
        }
    }
    @SubscribeEvent
    public static void cancelHand(RenderHandEvent event){
        if(!PerspectiveManager.shouldRenderFirstPersonHand())
            event.cancel();
    }
}
