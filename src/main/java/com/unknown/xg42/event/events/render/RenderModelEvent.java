package com.unknown.xg42.event.events.render;

import com.unknown.xg42.event.MinecraftEvent;

public class RenderModelEvent extends MinecraftEvent {
    public boolean rotating = false;
    public float pitch = 0;

    public RenderModelEvent(){
        super();
    }
}
