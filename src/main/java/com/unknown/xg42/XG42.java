package com.unknown.xg42;

import com.unknown.xg42.command.CommandManager;
import com.unknown.xg42.event.ForgeEventProcessor;
import com.unknown.xg42.gui.clickgui.GUIRender;
import com.unknown.xg42.gui.clickgui.HUDRender;
import com.unknown.xg42.manager.ConfigManager;
import com.unknown.xg42.manager.FriendManager;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.setting.Setting;
import com.unknown.xg42.setting.StringSetting;
import com.unknown.xg42.utils.Utils;
import com.unknown.xg42.utils.font.CFont;
import com.unknown.xg42.utils.font.CFontRenderer;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Mod(
        modid = XG42.MOD_ID,
        name = XG42.MOD_NAME,
        version = XG42.VERSION
)
public class XG42 {
    public static final String MOD_ID = "xg42";
    public static final String MOD_NAME = "XG42";
    public static final String VERSION = "1.0";
    public static final String VERSIONSUFFIX = "-Dev";
    public static final String KANJI = "$ XG42 $";
    public static Setting<String> commandPrefix = new StringSetting("CommandPrefix",null, ".");
    public static final EventBus EVENT_BUS = new EventManager();
    public static final Logger logger = LogManager.getLogger("XG42");
    private static CFontRenderer fontRenderer;
    public static boolean isFirstTime = true;
    public static CFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    private ForgeEventProcessor eventProcessor;
    private ModuleManager moduleManager;
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    private FriendManager friendManager;
    public FriendManager getFriendManager() {
        return friendManager;
    }
    private CommandManager commandManager;
    public CommandManager getCommandManager() {
        return commandManager;
    }
    private ConfigManager configManager;
    public ConfigManager getConfigManager() {
        return configManager;
    }
    private GUIRender guiRender;
    public GUIRender getGuiRender(){
        return guiRender;
    }
    private HUDRender hudEditor;
    public HUDRender getHudEditor(){ return hudEditor; }
    private GuiManager guiManager;
    public GuiManager getGuiManager(){ return guiManager; }

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static XG42 INSTANCE;
    public static XG42 getInstance(){
        return INSTANCE;
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        INSTANCE = this;
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        XG42.fontRenderer = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Goldman-Regular.ttf", 18f, Font.PLAIN), true, false);
        moduleManager = new ModuleManager();
        friendManager = new FriendManager();
        guiManager = new GuiManager();
        MinecraftForge.EVENT_BUS.register(eventProcessor = new ForgeEventProcessor());
        commandManager = new CommandManager();
        guiRender = new GUIRender();
        hudEditor = new HUDRender();
        configManager = new ConfigManager();
        ConfigManager.loadAll();
        setIcon();
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        setIcon();
    }

    private void setIcon() {
        Util.EnumOS util$enumos = Util.getOSType();
        if (util$enumos != Util.EnumOS.OSX) {
            try {
                InputStream inputstream = XG42.class.getResourceAsStream("/assets/xg42/icon/32x32.png");
                InputStream inputstream1 = XG42.class.getResourceAsStream("/assets/xg42/icon/16x16.png");
                if (inputstream != null && inputstream1 != null) {
                    Display.setIcon(new ByteBuffer[]{Utils.readImageToBuffer(inputstream), Utils.readImageToBuffer(inputstream1)});
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }
}
