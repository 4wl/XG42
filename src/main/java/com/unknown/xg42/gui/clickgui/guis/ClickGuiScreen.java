package com.unknown.xg42.gui.clickgui.guis;

import com.unknown.xg42.XG42;
import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.utils.Wrapper;
import com.unknown.xg42.utils.particle.ParticleSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;

public class ClickGuiScreen extends GuiScreen {


    private final ParticleSystem particleSystem = new ParticleSystem(100, 100);

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {

        if (GuiManager.getINSTANCE().getBackground().equals(GuiManager.Background.Blur) || GuiManager.getINSTANCE().getBackground().equals(GuiManager.Background.Both)) {
            if(Wrapper.getMinecraft().entityRenderer.getShaderGroup() != null) {
                Wrapper.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            Wrapper.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }


    @Override
    public void onGuiClosed() {
        if(Wrapper.getMinecraft().entityRenderer.getShaderGroup() != null) {
            Wrapper.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        if (ModuleManager.getModuleByName("ClickGUI").isEnabled()) {
            ModuleManager.getModuleByName("ClickGUI").disable();
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (GuiManager.getINSTANCE().getBackground().equals(GuiManager.Background.Shadow) || GuiManager.getINSTANCE().getBackground().equals(GuiManager.Background.Both)) {
            drawDefaultBackground();
        }
        if(mc.player == null) Gui.drawRect(0,0,9999,9999,new Color(0,0,0,255).getRGB());
        if (GuiManager.getINSTANCE().isParticle()) {
            particleSystem.tick(10);
            particleSystem.render();
        }

        XG42.getInstance().getGuiRender().drawScreen( mouseX,  mouseY,  partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        XG42.getInstance().getGuiRender(). mouseClicked( mouseX,  mouseY,  mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        XG42.getInstance().getGuiRender().keyTyped( typedChar,  keyCode);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        XG42.getInstance().getGuiRender().mouseReleased( mouseX,  mouseY,  state);
    }
}
