package net.kenji.auto_perspective.events;

import net.kenji.auto_perspective.CompatManager;
import net.kenji.auto_perspective.AutoPerspective;
import net.kenji.auto_perspective.PerspectiveManager;
import net.kenji.auto_perspective.compat.EpicFightCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = AutoPerspective.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents extends CompatManager {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        PerspectiveManager.tickPerspectiveManager(event);

        if(isEpicFightPresent()){
            EpicFightCompat.tickEpicFightCompat(event);
        }
    }
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        PerspectiveManager.onPlayerAttack(event);
    }
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if(isEpicFightPresent()){
            EpicFightCompat.onPlayerJoin(event);
        }
    }

    @SubscribeEvent
    public static void cancelHand(RenderHandEvent event){
        if(!PerspectiveManager.shouldRenderFirstPersonHand())
            event.setCanceled(true);
    }
}
