package net.kenji.auto_perspective.mixins;

import net.countered.smoothf5.SmoothF5ConfigState;
import net.countered.smoothf5.mixin.CameraAccessor;
import net.kenji.auto_perspective.PerspectiveManager;
import net.kenji.auto_perspective.api.SmoothCamera;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

// New: GameRendererMixin
@Mixin(Camera.class)
public abstract class SmoothCameraMixin implements SmoothCamera {
    @Shadow private Vec3 position;
    @Shadow private float xRot;
    @Shadow private float yRot;
    @Shadow protected abstract void setPosition(Vec3 pos);
    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Unique
    private Vec3 kenji$posLag = Vec3.ZERO;
    @Unique private float kenji$yawLag, kenji$pitchLag;
    @Unique private long kenji$startMs;
    @Unique private boolean kenji$inTransition, kenji$wasDetached, kenji$wasMirrored, kenji$init;
    @Unique private Vec3 kenji$lastPos = Vec3.ZERO;
    @Unique private float kenji$lastYaw, kenji$lastPitch;

    @Override
    public void smooth_f5$apply(boolean detached, boolean reverse) {
        Camera self = (Camera)(Object)this;
        CameraAccessor acc = (CameraAccessor)self;
        Vec3 targetPos = acc.getPosition();
        float targetYaw = acc.getYRot();
        float targetPitch = acc.getXRot();
        if (!this.kenji$init) {
            this.kenji$init = true;
            this.kenji$wasDetached = detached;
            this.kenji$wasMirrored = reverse;
            this.kenji$lastPos = targetPos;
            this.kenji$lastYaw = targetYaw;
            this.kenji$lastPitch = targetPitch;
        } else {
            if (this.kenji$wasDetached != detached || this.kenji$wasMirrored != reverse) {
                this.kenji$posLag = this.kenji$lastPos.subtract(targetPos);
                this.kenji$yawLag = Mth.wrapDegrees(this.kenji$lastYaw - targetYaw);
                this.kenji$pitchLag = this.kenji$lastPitch - targetPitch;
                this.kenji$startMs = System.currentTimeMillis();
                this.kenji$inTransition = true;
            }

            this.kenji$wasDetached = detached;
            this.kenji$wasMirrored = reverse;
            if (!this.kenji$inTransition) {
                this.kenji$lastPos = targetPos;
                this.kenji$lastYaw = targetYaw;
                this.kenji$lastPitch = targetPitch;
                PerspectiveManager.setRenderFirstPersonHand(true);
            } else {
                long durationMs = Math.max(1L, (long) SmoothF5ConfigState.transitionDurationMs);
                long elapsed = System.currentTimeMillis() - this.kenji$startMs;
                if (elapsed >= durationMs) {
                    this.kenji$inTransition = false;
                    this.kenji$posLag = Vec3.ZERO;
                    this.kenji$yawLag = 0.0F;
                    this.kenji$pitchLag = 0.0F;
                    this.kenji$lastPos = targetPos;
                    this.kenji$lastYaw = targetYaw;
                    this.kenji$lastPitch = targetPitch;
                } else {
                    float t = (float)elapsed / (float)durationMs;
                    float invT = 1.0F - t;
                    float lagFactor = invT * invT * invT;
                    Vec3 finalPos = targetPos.add(this.kenji$posLag.scale((double)lagFactor));
                    float finalYaw = targetYaw + this.kenji$yawLag * lagFactor;
                    float finalPitch = targetPitch + this.kenji$pitchLag * lagFactor;
                    acc.callSetPosition(finalPos);
                    acc.callSetRotation(finalYaw, finalPitch);
                    this.kenji$lastPos = finalPos;
                    this.kenji$lastYaw = finalYaw;
                    this.kenji$lastPitch = finalPitch;
                }
                PerspectiveManager.setRenderFirstPersonHand(false);
            }
        }
    }

}

