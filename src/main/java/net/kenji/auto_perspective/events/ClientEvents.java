package net.kenji.auto_perspective.events;

import net.kenji.auto_perspective.AutoPerspective;
import net.kenji.auto_perspective.CompatManager;
import net.kenji.auto_perspective.PerspectiveManager;
import net.kenji.auto_perspective.compat.EpicFightCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;


@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AutoPerspective.MODID, value = Dist.CLIENT)
public class ClientEvents extends CompatManager {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        PerspectiveManager.tickPerspectiveManager(event);

        if(isEpicFightPresent()){
            EpicFightCompat.tickEpicFightCompat(event);
        }
    }
    @SubscribeEvent
    public static void cancelHand(RenderHandEvent event){
        if(!PerspectiveManager.shouldRenderFirstPersonHand())
            event.setCanceled(true);
    }
}
