package com.unknown.xg42.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class RenderHelper {

    public static void drawEntityOnScreen(int x, int y, int size, float yaw, float pitch, EntityLivingBase entity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, 50.0F);
        GlStateManager.scale((float)(-size), (float)size, (float)size);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float renderYawOffset = entity.renderYawOffset;
        float rotationYaw = entity.rotationYaw;
        float rotationPitch = entity.rotationPitch;
        float prevRotationYawHead = entity.prevRotationYawHead;
        float rotationYawHead = entity.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entity.renderYawOffset = (float)Math.atan((double)(yaw / 40.0F)) * 20.0F;
        entity.rotationYaw = (float)Math.atan((double)(yaw / 40.0F)) * 40.0F;
        entity.rotationPitch = -((float)Math.atan((double)(pitch / 40.0F))) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(180.0F);
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        renderManager.setRenderShadow(true);
        entity.renderYawOffset = renderYawOffset;
        entity.rotationYaw = rotationYaw;
        entity.rotationPitch = rotationPitch;
        entity.prevRotationYawHead = prevRotationYawHead;
        entity.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
