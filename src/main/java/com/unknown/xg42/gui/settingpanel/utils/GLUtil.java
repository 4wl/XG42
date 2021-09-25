package com.unknown.xg42.gui.settingpanel.utils;

import com.unknown.xg42.utils.font.CFont;
import com.unknown.xg42.utils.font.CFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GLUtil {

    public static CFontRenderer FontRenderer= new CFontRenderer(new CFont.CustomFont("/assets/minecraft/fonts/Goldman-Regular.ttf", 18f, Font.PLAIN), true, false);

    public static void setColor(Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
                color.getAlpha() / 255.0f);
    }

    public static void setColor(int rgba) {
        int r = rgba & 0xFF;
        int g = rgba >> 8 & 0xFF;
        int b = rgba >> 16 & 0xFF;
        int a = rgba >> 24 & 0xFF;
        GL11.glColor4b((byte) r, (byte) g, (byte) b, (byte) a);
    }

    public static int toRGBA(Color c) {
        return c.getRed() | c.getGreen() << 8 | c.getBlue() << 16 | c.getAlpha() << 24;
    }

    public static void drawRect(int mode, int x, int y, int width, int height, int color) {
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        float f3 = (float) (color >> 24 & 255) / 255.0F;

        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glColor4f(f, f1, f2, f3);
        glBegin(mode);
        {
            glVertex2d(x+width, y);
            glVertex2d(x, y);
            glVertex2d(x, y+height);
            glVertex2d(x+width, y+height);
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawRect(int mode, double x, double y, double width, double height, int color) {
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        float f3 = (float) (color >> 24 & 255) / 255.0F;

        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glColor4f(f, f1, f2, f3);
        glBegin(mode);
        {
            glVertex2d(x+width, y);
            glVertex2d(x, y);
            glVertex2d(x, y+height);
            glVertex2d(x+width, y+height);
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawGradientRect(int mode, double x, double y, double width, double height, int startColor, int endColor){
        float f3 = (float) (startColor >> 24 & 255) / 255.0F;
        float f = (float) (startColor >> 16 & 255) / 255.0F;
        float f1 = (float) (startColor >> 8 & 255) / 255.0F;
        float f2 = (float) (startColor & 255) / 255.0F;

        float f7 = (float) (endColor >> 24 & 255) / 255.0F;
        float f4= (float) (endColor >> 16 & 255) / 255.0F;
        float f5 = (float) (endColor >> 8 & 255) / 255.0F;
        float f6 = (float) (endColor & 255) / 255.0F;


        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glShadeModel(GL_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if (mode == 7 || mode == 8) {
            glBegin(GL_TRIANGLES);
            {
                glColor4f(f, f1, f2, f3);
                glVertex2d(x + width, y);// bottom right

                glColor4f(f4, f5, f6, f7);
                glVertex2d(x, y);// bottom left

                glColor4f(f4, f5, f6, f7);
                glVertex2d(x, y + height); // top left

                glColor4f(f4, f5, f6, f7);
                glVertex2d(x, y + height); // top left

                glColor4f(f, f1, f2, f3);
                glVertex2d(x + width, y + height); // top right

                glColor4f(f, f1, f2, f3);
                glVertex2d(x + width, y);// bottom right
            }
            glEnd();
        }else {
            glBegin(mode);
            {
                glColor4f(f4, f5, f6, f7);
                glVertex2d(x, y);
                glColor4f(f, f1, f2, f3);
                glVertex2d(x + width, y);
                glColor4f(f, f1, f2, f3);
                glVertex2d(x + width, y + height);
                glColor4f(f4, f5, f6, f7);
                glVertex2d(x, y + height);
            }
            glEnd();
        }

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(x1, y1);
        glVertex2d(x2, y2);
        glVertex2d(x3, y3);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }
}
