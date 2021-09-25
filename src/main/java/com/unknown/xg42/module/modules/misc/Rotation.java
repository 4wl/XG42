package com.unknown.xg42.module.modules.misc;

import com.unknown.xg42.event.events.entity.EventPlayerPostMotionUpdate;
import com.unknown.xg42.event.events.entity.EventPlayerPreMotionUpdate;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import net.minecraft.item.ItemExpBottle;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(name = "Rotation", category = Category.MISC)
public class Rotation extends Module{

    public boolean shouldRotate;
    private float rotationYaw;
    private boolean smoothRotatePitch;
    public float yaw;
    private boolean smoothRotated;
    private boolean smoothRotateYaw;
    public float pitch;
    private float smoothYaw;
    private float rotationPitch;
    private boolean shouldReset;
    private float smoothPitch;

    @Override
    public void onInit() {
        this.shouldRotate = false;
        this.shouldReset = false;
    }

    @SubscribeEvent
    private void onEventPlayerPreMotionUpdate(EventPlayerPreMotionUpdate eventPlayerPreMotionUpdate) {
        if (this.smoothRotated) {
            if (this.smoothRotateYaw) {
                this.yaw = this.smoothYaw;
                this.smoothRotateYaw = false;
            }
            if (this.smoothRotatePitch) {
                this.pitch = this.smoothPitch;
                this.smoothRotatePitch = false;
            }
        }
        if (mc.player.isHandActive() && mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            return;
        }
        if (this.shouldRotate) {
            this.rotationPitch = mc.player.rotationPitch;
            this.rotationYaw = mc.player.rotationYaw;
            mc.player.rotationPitch = this.pitch;
            mc.player.rotationYaw = this.yaw;
            this.shouldReset = true;
            this.smoothRotated = true;
        }
    }

    @SubscribeEvent
    private void onEventPlayerPostMotionUpdate(EventPlayerPostMotionUpdate eventPlayerPostMotionUpdate) {
        if (this.shouldReset) {
            mc.player.rotationPitch = this.rotationPitch;
            mc.player.rotationYaw = this.rotationYaw;
            this.shouldReset = false;
        }
    }

    public boolean setRotation(float setSmoothRotationYaw, float Rpitch, final boolean b) {
        this.smoothRotatePitch = false;
        this.smoothRotateYaw = false;
        this.smoothRotated = true;
        if (b) {
            if (!this.shouldRotate) {
                this.yaw = mc.player.prevRotationYaw;
                this.pitch = mc.player.prevRotationPitch;
            }
            if (calculateDirectionDifference(setSmoothRotationYaw + 180.0f, this.yaw + 180.0f) > 90.0) {
                setSmoothRotationYaw = this.setSmoothRotationYaw(setSmoothRotationYaw, this.yaw);
                this.smoothRotated = false;
            }
            if (Math.abs(Rpitch - this.pitch) > 90.0f) {
                this.smoothRotatePitch = true;
                this.smoothRotated = false;
                this.smoothPitch = pitch;
                if (Rpitch > this.pitch) {
                    Rpitch -= (Rpitch - this.pitch) / 2.0f;
                }
                else {
                    Rpitch += (this.pitch - Rpitch) / 2.0f;
                }
            }
        }
        this.yaw = setSmoothRotationYaw;
        this.pitch = Rpitch;
        this.shouldRotate = true;
        return !this.smoothRotatePitch && !this.smoothRotateYaw;
    }

    public float setSmoothRotationYaw(float smoothYaw, float n) {
        this.smoothRotateYaw = true;
        this.smoothYaw = smoothYaw;
        final int n2 = 0;
        int addedOriginYaw = n2;
        int addedInputYaw = n2;
        while (smoothYaw + 180.0f < 0.0f) {
            smoothYaw += 360.0f;
            ++addedInputYaw;
        }
        while (smoothYaw + 180.0f > 360.0f) {
            smoothYaw -= 360.0f;
            --addedInputYaw;
        }
        while (n + 180.0f < 0.0f) {
            n += 360.0f;
            ++addedOriginYaw;
        }
        while (n + 180.0f > 360.0f) {
            n -= 360.0f;
            --addedOriginYaw;
        }
        smoothYaw += 180.0f;
        n += 180.0f;
        final double n3 = n - smoothYaw;
        if (n3 >= -180.0 && n3 >= 180.0) {
            smoothYaw -= (float)(n3 / 2.0);
        }
        else {
            smoothYaw += (float)(n3 / 2.0);
        }
        smoothYaw -= 180.0f;
        if (addedInputYaw > 0) {
            for (int i = 0; i < addedInputYaw; ++i) {
                smoothYaw -= 360.0f;
            }
        }
        else if (addedInputYaw < 0) {
            for (int j = 0; j > addedInputYaw; --j) {
                smoothYaw += 360.0f;
            }
        }
        return smoothYaw;
    }

    public static double calculateDirectionDifference(double n, double n2) {
        while (n < 0.0) {
            n += 360.0;
        }
        while (n > 360.0) {
            n -= 360.0;
        }
        while (n2 < 0.0) {
            n2 += 360.0;
        }
        while (n2 > 360.0) {
            n2 -= 360.0;
        }
        final double n3 = Math.abs(n2 - n) % 360.0;
        return (n3 > 180.0) ? (360.0 - n3) : n3;
    }

    public void resetRotation() {
        this.shouldRotate = false;
        final float rotationYaw = mc.player.rotationYaw;
        this.smoothYaw = rotationYaw;
        this.yaw = rotationYaw;
        final float rotationPitch = mc.player.rotationPitch;
        this.smoothPitch = rotationPitch;
        this.pitch = rotationPitch;
        final boolean b = false;
        this.smoothRotateYaw = b;
        this.smoothRotatePitch = b;
        this.smoothRotated = true;
        mc.player.setRotationYawHead(mc.player.rotationYaw);
    }
}
