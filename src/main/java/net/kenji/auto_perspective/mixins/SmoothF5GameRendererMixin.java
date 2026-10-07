package net.kenji.auto_perspective.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.auto_perspective.api.SmoothCamera;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, remap = false)
public abstract class SmoothF5GameRendererMixin {
    @Shadow
    @Final
    private Camera mainCamera;

    @Inject(
            method = "renderLevel",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
                    shift = At.Shift.AFTER)
    )
    private void smooth_f5$afterCameraSetup(DeltaTracker p_348589_, CallbackInfo ci) {
        CameraType type = Minecraft.getInstance().options.getCameraType();
        ((SmoothCamera) this.mainCamera).smooth_f5$apply(!type.isFirstPerson(), type.isMirrored());
    }

}