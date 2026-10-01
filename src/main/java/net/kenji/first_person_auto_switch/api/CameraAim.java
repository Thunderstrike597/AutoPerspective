package net.kenji.first_person_auto_switch.api;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public final class CameraAim {
    private CameraAim() {}

    public record Ray(Vec3 origin, Vec3 dir) {}

    /**
     * Returns the crosshair ray from the camera, or null when vanilla
     * behaviour (player head) should be left alone.
     */
    @Nullable
    public static Ray get(Player player) {
        Minecraft mc = Minecraft.getInstance();
        if (player != mc.player) return null;

        // Vanilla front-facing third person: vanilla raycasts from the head, so do we.
        if (mc.options.getCameraType().isMirrored()) return null;

        Camera cam = mc.gameRenderer.getMainCamera();
        Vector3f l = cam.getLookVector();
        Vec3 camDir = new Vec3(l.x(), l.y(), l.z());

        // Camera and head point the same way (vanilla first person / vanilla F5 back view):
        // nothing to fix.
        if (camDir.dot(player.getLookAngle()) > 0.99996) return null;

        // Slide the origin along the camera line to the point nearest the eyes,
        // so reach is measured from the player and blocks behind them can't be hit.
        double t = Math.max(0.0, player.getEyePosition().subtract(cam.getPosition()).dot(camDir));
        return new Ray(cam.getPosition().add(camDir.scale(t)), camDir);
    }
}