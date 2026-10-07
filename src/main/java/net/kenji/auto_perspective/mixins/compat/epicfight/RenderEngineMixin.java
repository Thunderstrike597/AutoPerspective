package net.kenji.auto_perspective.mixins.compat.epicfight;

import net.kenji.auto_perspective.PerspectiveManager;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.RenderEngine;

@Mixin(value = RenderEngine.class, remap = false)
public class RenderEngineMixin {

    @Inject(method = "epicfight$renderHand", at = @At("HEAD"), cancellable = true)
    private static void onRenderHand(RenderHandEvent event, CallbackInfo ci){
        if(!PerspectiveManager.shouldRenderFirstPersonHand()){
            ci.cancel();
        }
    }
}
