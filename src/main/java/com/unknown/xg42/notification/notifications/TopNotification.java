package com.unknown.xg42.notification.notifications;

import com.unknown.xg42.notification.Notification;
import com.unknown.xg42.notification.NotificationType;
import com.unknown.xg42.utils.ColorUtils;
import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TopNotification extends Notification {

    public TopNotification(NotificationType type, String title, String message, int length) {
        super(type, title, message, length);
    }

    private final Map<Integer, Integer> offsetDirLeft = new HashMap<>();
    private final Map<Integer, Integer> offsetDirRight = new HashMap<>();
    private int arrAmount = 0;
    private boolean shouldAdd = true;

    private int delaydir = 0;
    private int delay = 3;

    @Override
    public void render(int RealDisplayWidth, int RealDisplayHeight) {
        delaydir++;
        int height = font.getHeight() * 3;
        int width = RealDisplayWidth / 4;
        int offset = getOffset(width);
        Color color = type == NotificationType.INFO ? Color.BLACK: getDefaultTypeColor();
        boolean shouldEffect;
        shouldEffect = offset >= width - 5;
        int cx = RealDisplayWidth/2;
        int cy = (RealDisplayHeight/8);
        int x = cx - offset;
        int dWidth = offset * 2;
        if(shouldEffect){
            if(shouldAdd){
                offsetDirLeft.put(arrAmount, -16-((32/3)*arrAmount));
                offsetDirRight.put(arrAmount,-16-((32/3)*arrAmount));
                arrAmount++;
                if(arrAmount >= 3){
                    arrAmount = 0;
                    shouldAdd = false;
                }
            }
            GL11.glLineWidth(2.0f);
            for(Map.Entry<Integer, Integer> offsetdir : offsetDirLeft.entrySet()){
                int value = offsetdir.getValue();
                int alpha = ColorUtils.calculateAlphaChangeColor(255, 10, 50, value);
                RenderUtils.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                drawC(x-value, cy, height, height);
            }
            for(Map.Entry<Integer, Integer> offsetdir : offsetDirRight.entrySet()){
                int value = offsetdir.getValue();
                int alpha = ColorUtils.calculateAlphaChangeColor(255, 10, 50, value);
                RenderUtils.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                drawBC(x + dWidth + value, cy, height, height);
            }
        }
        if (delaydir >= delay){
            delaydir=0;
            for(Map.Entry<Integer, Integer> offsetdir : offsetDirLeft.entrySet()){
                if(offsetdir.getValue() >= 50){ offsetdir.setValue(-16); }else{ offsetdir.setValue(offsetdir.getValue()+1); }
            }
            for(Map.Entry<Integer, Integer> offsetdir : offsetDirRight.entrySet()){
                if(offsetdir.getValue() >= 50){ offsetdir.setValue(-16); }else{ offsetdir.setValue(offsetdir.getValue()+1); }
            }
        }

        RenderUtils.drawRect(x, cy, dWidth, height, color);
        RenderUtils.drawTriangle(x, cy, x-15-1,cy+(height/2D), x,cy+height, color);
        RenderUtils.drawTriangle(x+dWidth,cy+height,x+15+dWidth,cy+(height/2D),x+dWidth, cy, color);
        int fx = x + (dWidth / 2);
        int alpha = ColorUtils.calculateAlphaChangeColor(10, 255, width, offset);
        font.drawCenteredString(title, fx, cy + (font.getHeight()/2F) - 1, new Color(255, 255, 255, alpha).getRGB());
        font.drawCenteredString(message, fx, cy + ((font.getHeight()/2F) * 2) + 6, new Color(255, 255, 255, alpha).getRGB());
        if(!shouldAdd && !shouldEffect){
            offsetDirLeft.remove(arrAmount);
            offsetDirRight.remove(arrAmount);
            arrAmount++;
            if(arrAmount >= 3){
                arrAmount = 0;
                shouldAdd = true;
            }
        }
    }

    private void drawBC(int cx, int cy, int height, int margin){
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBegin(GL11.GL_LINE_STRIP); {
            GL11.glVertex2d(cx, cy);
            GL11.glVertex2d(cx + margin, cy + (height / 2D));
            GL11.glVertex2d(cx, cy + height);
        }GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    private void drawC(int cx, int cy, int height, int margin){
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBegin(GL11.GL_LINE_STRIP); {
            GL11.glVertex2d(cx, cy);
            GL11.glVertex2d(cx - margin, cy + (height / 2D));
            GL11.glVertex2d(cx, cy + height);
        }GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
