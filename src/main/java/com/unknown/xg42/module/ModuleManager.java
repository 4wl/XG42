package com.unknown.xg42.module;

import com.unknown.xg42.XG42;
import com.unknown.xg42.event.events.render.RenderEvent;
import com.unknown.xg42.gui.clickgui.guis.HUDEditorScreen;
import com.unknown.xg42.module.modules.client.NullHUD;
import com.unknown.xg42.module.modules.client.NullModule;
import com.unknown.xg42.utils.ClassFinder;
import com.unknown.xg42.utils.EntityUtil;
import com.unknown.xg42.utils.Wrapper;
import com.unknown.xg42.utils.XG42Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {

    public static ModuleManager INSTANCE;
    private final List<IModule> modules = new ArrayList<>();

    public ModuleManager(){
        INSTANCE = this;
        init();
    }

    private void init(){
        loadModules();
        loadHUDs();
        modules.sort(Comparator.comparing(IModule::getName));
    }

    public static void onKey(InputUpdateEvent event){
        INSTANCE.modules.forEach( mod -> {
            if (mod.isEnabled()) mod.onKey(event);
        });
    }

    private void loadModules(){
        Set<Class> classList = ClassFinder.findClasses(IModule.class.getPackage().getName(), Module.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(aClass -> {
            try {
                Module mod = (Module) aClass.newInstance();
                modules.add(mod);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate Module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
    }

    private void loadHUDs(){
        Set<Class> classList = ClassFinder.findClasses(IModule.class.getPackage().getName(), HUDModule.class);
        classList.stream().sorted(Comparator.comparing(Class::getSimpleName)).forEach(aClass -> {
            try {
                HUDModule mod = (HUDModule) aClass.newInstance();
                modules.add(mod);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Couldn't initiate HUD Module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
        });
    }

    public static List<IModule> getAllModules(){
        return INSTANCE.modules;
    }

    public static List<IModule> getModules(){
        return INSTANCE.modules.stream().filter(module -> !module.isHUD).collect(Collectors.toList());
    }

    public static List<IModule> getHUDModules(){
        return INSTANCE.modules.stream().filter(module -> module.isHUD).collect(Collectors.toList());
    }

    public static IModule getModuleByName(String targetName) {
        for(IModule iModule : getAllModules()){
            if(iModule.name.equalsIgnoreCase(targetName)){
                return iModule;
            }
        }
        XG42.logger.fatal("Module "+ targetName +" is not exist.Please check twice!");
        return new NullModule();
    }

    public static HUDModule getHUDByName(String targetName) {
        for(IModule iModule : getHUDModules()){
            if(iModule.name.equalsIgnoreCase(targetName)){
                return (HUDModule)iModule;
            }
        }
        XG42.logger.fatal("HUD "+ targetName +" is not exist.Please check twice!");
        return new NullHUD();
    }

    public static void onBind(int bind) {
        if (bind == 0) return;
        INSTANCE.modules.forEach(module -> {
            if (module.getBind() == bind) {
                module.toggle();
            }
        });
    }

    public static void onTick() {
        INSTANCE.modules.forEach(mod -> {
            if (mod.isEnabled()) mod.onTick();
        });
    }


    public static void onRender(RenderGameOverlayEvent.Post event) {
        INSTANCE.modules.forEach( mod -> {
            if (mod.isEnabled()) mod.onRender2D(event);
        });
        onRenderHUD();
    }

    public static void onRenderHUD() {
        //TODO: Fix
        if(!(Minecraft.getMinecraft().currentScreen instanceof HUDEditorScreen)) {
            getHUDModules().forEach( mod -> {
                if (mod.isEnabled()) mod.onRender();
            });
        }
    }

    public static void onWorldRender(RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("xg42");

        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        Vec3d renderPos = getInterpolatedPos(Objects.requireNonNull(Wrapper.getMinecraft().getRenderViewEntity()), event.getPartialTicks());

        RenderEvent e = new RenderEvent(XG42Tessellator.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();

        INSTANCE.modules.forEach(mod -> {
            if (mod.isEnabled()) {
                Minecraft.getMinecraft().profiler.startSection(mod.getName());
                mod.onWorldRender(e);
                Minecraft.getMinecraft().profiler.endSection();
            }
        });

        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        XG42Tessellator.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(EntityUtil.getInterpolatedAmount(entity, ticks));
    }

    public static List<IModule> getModulesByCategory(Category category){
        return getAllModules().stream().filter(module -> module.category.equals(category)).collect(Collectors.toList());
    }
}
