package com.unknown.xg42.mixin.client;

import com.unknown.xg42.event.events.entity.EventPlayerPostMotionUpdate;
import com.unknown.xg42.event.events.entity.EventPlayerPreMotionUpdate;
import com.unknown.xg42.event.events.entity.UpdateWalkingPlayerEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityPlayerSP.class}, priority=9998)
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer {

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    private void preMotion(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo callbackInfo) {
        EventPlayerPreMotionUpdate eventPlayerPreMotionUpdate = new EventPlayerPreMotionUpdate(0);
        MinecraftForge.EVENT_BUS.post(eventPlayerPreMotionUpdate);
        if (eventPlayerPreMotionUpdate.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="RETURN")}, cancellable=true)
    public void OnPostUpdateWalkingPlayer(CallbackInfo callbackInfo) {
        EventPlayerPostMotionUpdate eventPlayerPostMotionUpdate = new EventPlayerPostMotionUpdate(1);
        MinecraftForge.EVENT_BUS.post(eventPlayerPostMotionUpdate);
        if (eventPlayerPostMotionUpdate.isCanceled()) {
            callbackInfo.cancel();
        }
    }
}
