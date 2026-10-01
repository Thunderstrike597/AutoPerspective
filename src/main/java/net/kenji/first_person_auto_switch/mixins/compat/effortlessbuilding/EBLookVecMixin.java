package net.kenji.first_person_auto_switch.mixins.compat.effortlessbuilding;

import net.kenji.first_person_auto_switch.api.CameraAim;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import nl.requios.effortlessbuilding.buildmode.BuildModes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = BuildModes.class, remap = false)
public abstract class EBLookVecMixin {
    @Redirect(method = "getPlayerLookVec",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;getLookAngle()Lnet/minecraft/world/phys/Vec3;"),
        remap = true)
    private static Vec3 camera$look(Player p) {
        CameraAim.Ray r = CameraAim.get(p);
        return r != null ? r.dir() : p.getLookAngle();
    }
}