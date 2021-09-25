package com.unknown.xg42.gui.clickgui.util;

import com.unknown.xg42.manager.GuiManager;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;

import static org.lwjgl.opengl.GL11.*;

public class SpecialRender {
    public static void draw(double x, double y, double width, double height, double spacing, float lineWidth, int change){
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        RenderUtils.setLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        {
            setsRainbow(0);
            glVertex2d(       x      ,       y      );   // Left top
            int d=0;
            for (int s=0; s <= height/spacing; s++){
                setsRainbow((long)change*s);
                glVertex2d(x, (y+(spacing*s)));
                d=s;
            }
            glVertex2d(       x      , y + height);  // Left Bottom
            glVertex2d(x + width  , y + height);  // Right Bottom
            for (int s=0; s <= height/spacing; s++){
                setsRainbow((long) change*(d-s));
                glVertex2d(x+width, (y+height)-(spacing*s));
            }
            setsRainbow(0);
            glVertex2d(x + width  ,       y      );   // Right top
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawSLine(double x, double y1, double y2, int height, float lineWidth, int start, int change){
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        RenderUtils.setLineWidth(lineWidth);
        double miny = Math.min(y1,y2);
        double maxy = Math.max(y1,y2);
        glBegin(GL_LINE_STRIP);
        {
            setsRainbow(start);
            glVertex2d(x, miny); // Left top
            int d = 0;
            for (int s=0; s <= (maxy-miny)/height; s++){
                setsRainbow(start + (change*s));
                glVertex2d(x, (miny+(height*s)));
                d=s;
            }
            setsRainbow(start+((long) change *(d+1)));
            glVertex2d(x,maxy);  // Left Bottom
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    private static void setsRainbow(long add){
        RenderUtils.setColor(Rainbow.getRainbow(GuiManager.getINSTANCE().getColorINSTANCE().rainbowSpeed.getValue(), GuiManager.getINSTANCE().getColorINSTANCE().rainbowSaturation.getValue(), GuiManager.getINSTANCE().getColorINSTANCE().rainbowBrightness.getValue(), add));
    }
}
