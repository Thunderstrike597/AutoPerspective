package net.kenji.first_person_auto_switch.mixins.compat.sophistocatedbuilding;

import net.kenji.first_person_auto_switch.api.CameraAim;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nl.requios.effortlessbuilding.capability.CapabilityHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sophisticated.building.attachment.AttachmentHandler;
import sophisticated.building.attachment.PowerLevel;
import sophisticated.building.utilities.ClientBlockUtilities;


@Mixin(value = ClientBlockUtilities.class, remap = false)
public abstract class SBFarLookMixin {
    @Inject(method = "getLookingAtFar", at = @At("HEAD"), cancellable = true)
    private static void camera$far(Player player, CallbackInfoReturnable<BlockHitResult> cir) {
        CameraAim.Ray ray = CameraAim.get(player);
        if (ray == null) return;
        PowerLevel powerLevel = AttachmentHandler.getOrCreatePowerLevel(player);

        float reach = powerLevel.getPlacementReach(player, false);
        Vec3 end = ray.origin().add(ray.dir().scale(reach));
        cir.setReturnValue(player.level().clip(new ClipContext(
                ray.origin(), end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)));

    }
}