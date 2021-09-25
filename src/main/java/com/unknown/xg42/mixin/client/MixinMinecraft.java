package com.unknown.xg42.mixin.client;

import com.unknown.xg42.XG42;
import com.unknown.xg42.gui.xg42guistart.XG42GuiStart;
import com.unknown.xg42.manager.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow
    public GuiScreen currentScreen;

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        save();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdown(CallbackInfo info) {
        save();
    }

    @Inject(method = "displayGuiScreen", at = @At("HEAD"))
    public void displayGuiScreen(GuiScreen p_displayGuiScreen_1_, CallbackInfo ci){
        if (p_displayGuiScreen_1_ instanceof GuiMainMenu){
            new GuiMainMenu();
            if (XG42.isFirstTime){
                //currentScreen = new XG42GuiStart();
                XG42.isFirstTime = false;
            }
        }
    }

    private void save() {
        XG42.logger.warn("Saving XG42 configuration please wait...");
        ConfigManager.saveAll();
        XG42.logger.warn("Configuration saved!");
    }
}
