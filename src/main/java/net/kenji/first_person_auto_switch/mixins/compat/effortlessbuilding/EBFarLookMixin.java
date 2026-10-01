package net.kenji.first_person_auto_switch.mixins.compat.effortlessbuilding;

import net.kenji.first_person_auto_switch.api.CameraAim;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nl.requios.effortlessbuilding.capability.CapabilityHandler;
import nl.requios.effortlessbuilding.utilities.ClientBlockUtilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = ClientBlockUtilities.class, remap = false)
public abstract class EBFarLookMixin {
    @Inject(method = "getLookingAtFar", at = @At("HEAD"), cancellable = true)
    private static void camera$far(Player player, CallbackInfoReturnable<BlockHitResult> cir) {
        CameraAim.Ray ray = CameraAim.get(player);
        if (ray == null) return;
        float reach = CapabilityHandler.getPlacementReach(player, false);
        Vec3 end = ray.origin().add(ray.dir().scale(reach));
        cir.setReturnValue(player.level().clip(new ClipContext(
                ray.origin(), end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)));
    }
}