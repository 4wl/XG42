package com.unknown.xg42.event.events.entity;

import com.unknown.xg42.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class UpdateWalkingPlayerEvent extends EventStage {
    public UpdateWalkingPlayerEvent(int stage) {
        super(stage);
    }
}
