package net.kenji.first_person_auto_switch.mixins.compat.epicfight;

import net.kenji.first_person_auto_switch.PerspectiveManager;
import net.minecraftforge.client.event.RenderHandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.RenderEngine;

@Mixin(value = RenderEngine.Events.class, remap = false)
public class RenderEngineMixin {

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private static void onRenderHand(RenderHandEvent event, CallbackInfo ci){
        if(!PerspectiveManager.shouldRenderFirstPersonHand()){
            ci.cancel();
        }
    }
}
