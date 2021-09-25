package com.unknown.xg42.module.modules.render;

import com.unknown.xg42.XG42;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;
import com.unknown.xg42.setting.FloatSetting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(name = "CustomFov", description = "Custom Field of view", category = Category.RENDER)
public class CustomFov extends Module{

    FloatSetting fov = fsetting("Fov", 130F, 70F, 200F);

    @SubscribeEvent
    public void onFov(EntityViewRenderEvent.FOVModifier event){
        event.setFOV(fov.getValue());
    }

    @Override
    public void onEnable(){
        XG42.EVENT_BUS.subscribe(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable(){
        XG42.EVENT_BUS.unsubscribe(this);
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
