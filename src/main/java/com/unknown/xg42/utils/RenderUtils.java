package com.unknown.xg42.utils;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    public static void setColor(Color color){
        glColor4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
    }

    public static void setColor(int color){
        setColor(new Color(color));
    }

    public static void setColor(int red, int green, int blue, int alpha){
        setColor(new Color(red, green, blue, alpha));
    }

    public static void setColor(int red, int green, int blue){
        setColor(red, green, blue, 255);
    }

    public static void setLineWidth(float width){
        glLineWidth(width);
    }

    public static void drawLine(double x1, double y1, double x2, double y2, float lineWidth, Color ColorStart, Color ColorEnd){
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glLineWidth(lineWidth);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_LINE_LOOP);{
            setColor(ColorStart);
            glVertex2d(x1, y1);
            setColor(ColorEnd);
            glVertex2d(x2, y2);
        }glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawLine(double x1, double y1, double x2, double y2, float lineWidth, Color color){
        drawLine(x1, y1, x2, y2, lineWidth, color, color);
    }

    public static void drawArc(double cx, double cy, double r, double start_angle, double end_angle, int num_segments) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBegin(GL_TRIANGLES);
        for (int i = (int) (num_segments/(360/start_angle))+1; i <= num_segments/(360/end_angle); i++) {
            double previousangle = 2*Math.PI*(i-1)/num_segments;
            double angle = 2*Math.PI*i/num_segments;
            glVertex2d(cx, cy);
            glVertex2d(cx+Math.cos(angle)*r, cy+Math.sin(angle)*r);
            glVertex2d(cx+Math.cos(previousangle)*r, cy+Math.sin(previousangle)*r);
        }
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawArcOutline(double cx, double cy, double r, double start_angle, double end_angle, int num_segments) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBegin(GL_LINE_STRIP);
        for (int i = (int) (num_segments/(360/start_angle))+1; i <= num_segments/(360/end_angle); i++) {
            double angle = 2*Math.PI*i/num_segments;
            glVertex2d(cx+Math.cos(angle)*r, cy+Math.sin(angle)*r);
        }
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawCircle(double cx, double cy, double radius){
        drawArc(cx , cy, radius, 0, 360, 16);
    }

    public static void drawCircleOutline(double cx, double cy, double radius){
        drawArcOutline(cx , cy, radius, 0, 360, 16);
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

    public static void drawTriangleOutline(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        glBegin(GL_LINE_LOOP);
        glVertex2d(x1, y1);
        glVertex2d(x2, y2);
        glVertex2d(x3, y3);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public static void drawGradientRectOutline(double x, double y, double width, double height, GradientDirection direction, Color startColor, Color endColor){
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        Color[] result = checkColorDirection(direction, startColor, endColor);
        glBegin(GL_LINE_LOOP);
        {
            glColor4f(result[2].getRed() / 255f, result[2].getGreen() / 255f, result[2].getBlue() / 255f, result[2].getAlpha() / 255f);
            glVertex2d(x + width  ,       y      );   // Right Bottom
            glColor4f(result[3].getRed() / 255f, result[3].getGreen() / 255f, result[3].getBlue() / 255f, result[3].getAlpha() / 255f);
            glVertex2d(       x      ,       y      );   // Left Bottom
            glColor4f(result[0].getRed() / 255f, result[0].getGreen() / 255f, result[0].getBlue() / 255f, result[0].getAlpha() / 255f);
            glVertex2d(       x      , y + height);  // Left Top
            glColor4f(result[1].getRed() / 255f, result[1].getGreen() / 255f, result[1].getBlue() / 255f, result[1].getAlpha() / 255f);
            glVertex2d(x + width  , y + height);  // Right Top
        }
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawGradientRect(double x, double y, double width, double height, GradientDirection direction, Color startColor, Color endColor){
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);

        Color[] result = checkColorDirection(direction, startColor, endColor);

        glBegin(GL_QUADS);
        {
            glColor4f(result[2].getRed() / 255f, result[2].getGreen() / 255f, result[2].getBlue() / 255f, result[2].getAlpha() / 255f);
            glVertex2d(x + width  ,       y      );   // Right Bottom
            glColor4f(result[3].getRed() / 255f, result[3].getGreen() / 255f, result[3].getBlue() / 255f, result[3].getAlpha() / 255f);
            glVertex2d(       x      ,       y      );   // Left Bottom
            glColor4f(result[0].getRed() / 255f, result[0].getGreen() / 255f, result[0].getBlue() / 255f, result[0].getAlpha() / 255f);
            glVertex2d(       x      , y + height);  // Left Top
            glColor4f(result[1].getRed() / 255f, result[1].getGreen() / 255f, result[1].getBlue() / 255f, result[1].getAlpha() / 255f);
            glVertex2d(x + width  , y + height);  // Right Top
        }
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
    }

    public static void drawRectOutline(double x, double y, double width, double height, Color color){
        drawGradientRectOutline(x, y, width, height, GradientDirection.Normal, color, color);
    }

    public static void drawRect(double x, double y, double width, double height, Color color){
        drawGradientRect(x, y, width, height, GradientDirection.Normal, color, color);
    }

    public static void drawRoundedRectangle(double x, double y, double width, double height, double radius, GradientDirection direction, Color startColor, Color endColor) {
        Color[] result = checkColorDirection(direction, startColor, endColor);
        glColor4f(result[0].getRed() / 255f, result[0].getGreen() / 255f, result[0].getBlue() / 255f, result[0].getAlpha() / 255f);
        drawArc( (x + width - radius),  (y + height - radius), radius, 0,90,16); // bottom right
        glColor4f(result[1].getRed() / 255f, result[1].getGreen() / 255f, result[1].getBlue() / 255f, result[1].getAlpha() / 255f);
        drawArc( (x+radius),  (y + height - radius), radius, 90,180,16); // bottom left
        glColor4f(result[2].getRed() / 255f, result[2].getGreen() / 255f, result[2].getBlue() / 255f, result[2].getAlpha() / 255f);
        drawArc(x+radius, y+radius, radius, 180,270,16); // top left
        glColor4f(result[3].getRed() / 255f, result[3].getGreen() / 255f, result[3].getBlue() / 255f, result[3].getAlpha() / 255f);
        drawArc( (x + width - radius),  (y+radius), radius, 270,360,16); // top right
        //top
        drawGradientRect(x + radius, y, width - (radius * 2), radius, GradientDirection.LeftToRight, result[2], result[3]);
        //bottom
        drawGradientRect(x + radius, y + height - radius, width - (radius * 2), radius, GradientDirection.LeftToRight, result[1], result[0]);
        //left
        drawGradientRect(x, y + radius, radius, height - (radius * 2), GradientDirection.DownToUp, result[1], result[2]);
        //right
        drawGradientRect(x + width - radius, y + radius, radius, height - (radius * 2), GradientDirection.DownToUp, result[0], result[3]);
        //center
        drawGradientRect(x + radius, y + radius, width - (radius * 2), height - (radius * 2), direction, startColor, endColor);
    }

    public static void drawRoundedRectangle(double x, double y, double width, double height, double radius, Color color) {
        drawRoundedRectangle(x, y, width, height, radius, GradientDirection.Normal, color, color);
    }

    public static void drawRoundedRectangleOutline( final double x, final double y, final double width, final double height, final double radius, final float lineWidth, final GradientDirection direction, final Color startColor, final Color endColor) {
        Color[] result = checkColorDirection(direction, startColor, endColor);
        glLineWidth(lineWidth);
        glColor4f(result[0].getRed() / 255f, result[0].getGreen() / 255f, result[0].getBlue() / 255f, result[0].getAlpha() / 255f);
        drawArcOutline( (x + width - radius),  (y + height - radius), radius, 0,90,50); // bottom right
        glColor4f(result[1].getRed() / 255f, result[1].getGreen() / 255f, result[1].getBlue() / 255f, result[1].getAlpha() / 255f);
        drawArcOutline( (x+radius),  (y + height - radius), radius, 90,180,50); // bottom left
        glColor4f(result[2].getRed() / 255f, result[2].getGreen() / 255f, result[2].getBlue() / 255f, result[2].getAlpha() / 255f);
        drawArcOutline(x+radius, y+radius, radius, 180,270,50); // top left
        glColor4f(result[3].getRed() / 255f, result[3].getGreen() / 255f, result[3].getBlue() / 255f, result[3].getAlpha() / 255f);
        drawArcOutline( (x + width - radius),  (y+radius), radius, 270,360,50); // top right
        //top
        drawLine(x + radius - 1, y, x + radius + (width - (radius * 2)) + 1, y, lineWidth, result[2], result[3]);
        //bottom
        drawLine(x + radius - 1, y + height, x + radius + (width - (radius * 2)) + 1, y + height, lineWidth,  result[1], result[0]);
        //left
        drawLine( x, y + radius - 1, x, y + radius + (height - (radius * 2)), lineWidth, result[1], result[2]);
        //right
        drawLine(x + width, y + radius, x + width, (y + radius) + (height - (radius * 2)) + 1, lineWidth, result[0], result[3]);
    }

    public static void drawRoundedRectangleOutline( final double x, final double y, final double width, final double height, final double radius, final float lineWidth, final Color color) {
        drawRoundedRectangleOutline(x, y, width, height, radius, lineWidth, GradientDirection.Normal, color, color);
    }

    public static void drawArcNBlend(double cx, double cy, double r, double start_angle, double end_angle, int num_segments) {
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);
        for (int i = (int) (num_segments/(360/start_angle))+1; i <= num_segments/(360/end_angle); i++) {
            double previousangle = 2*Math.PI*(i-1)/num_segments;
            double angle = 2*Math.PI*i/num_segments;
            glVertex2d(cx, cy);
            glVertex2d(cx+Math.cos(angle)*r, cy+Math.sin(angle)*r);
            glVertex2d(cx+Math.cos(previousangle)*r, cy+Math.sin(previousangle)*r);
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
    }
    public static void drawGradientRectNBlend(double x, double y, double width, double height, GradientDirection direction, Color startColor, Color endColor){
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);

        Color[] result = checkColorDirection(direction, startColor, endColor);

        glBegin(GL_QUADS);
        {
            glColor4f(result[2].getRed() / 255f, result[2].getGreen() / 255f, result[2].getBlue() / 255f, result[2].getAlpha() / 255f);
            glVertex2d(x + width  ,       y      );   // Right Bottom
            glColor4f(result[3].getRed() / 255f, result[3].getGreen() / 255f, result[3].getBlue() / 255f, result[3].getAlpha() / 255f);
            glVertex2d(       x      ,       y      );   // Left Bottom
            glColor4f(result[0].getRed() / 255f, result[0].getGreen() / 255f, result[0].getBlue() / 255f, result[0].getAlpha() / 255f);
            glVertex2d(       x      , y + height);  // Left Top
            glColor4f(result[1].getRed() / 255f, result[1].getGreen() / 255f, result[1].getBlue() / 255f, result[1].getAlpha() / 255f);
            glVertex2d(x + width  , y + height);  // Right Top
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
    }
    public static void drawRectNBlend(double x, double y, double width, double height, Color color){
        drawGradientRectNBlend(x, y, width, height, GradientDirection.Normal, color, color);
    }

    public static void drawHalfRoundedRectangle(double x, double y, double width, double height, double radius, HalfRoundedDirection direction, Color color) {
        setColor(color);
        if (direction == HalfRoundedDirection.Top){
            drawArc(x+radius-0.5, y+radius, radius, 180, 270, 50);// top left
            drawArc(x+width-radius, y+radius, radius, 275, 360, 50);// top right
            drawRect(x, y + radius, radius, height-radius, color);
            drawRect(x+width-radius, y + radius, radius, height-radius, color);
            drawRect(x+radius-1-0.5, y, width-(radius*2)+2+0.5, height + 2, color);
        }else if (direction == HalfRoundedDirection.Bottom){
            drawArc(x+radius, y+height-radius, radius, 90, 180, 50);// bottom left
            drawArc(x+width-radius, y+height-radius, radius, 0, 90, 50);// bottom right
            drawRect(x, y+radius, radius, height-(radius*2), color);
            drawRect(x+width-radius, y+radius, radius, height-(radius*2), color);
        }else if (direction == HalfRoundedDirection.Left){
            drawArc(x+radius, y+radius, radius, 180, 270, 50);// left top
            drawArc(x+radius, y+height-radius, radius, 90, 180, 50);// left bottom
            drawRect(x, y+radius, width, height - (radius*2), color);
            drawRect(x + radius, y, width-(radius*2), radius, color);
            drawRect(x + radius, (y+height)-radius, width-(radius*2), radius, color);
        }else if (direction == HalfRoundedDirection.Right){
            drawArc(x+width-radius, y+radius, radius, 270, 360, 50);// right top
            drawArc(x+width-radius, y+height-radius, radius, 0, 90, 50);// right bottom
            drawRect(x, y, width-radius, radius, color);
            drawRect(x, y+height-radius, width-radius, radius, color);
        }
    }

    private static Color[] checkColorDirection(GradientDirection direction, Color start, Color end){
        Color[] dir = new Color[4];
        if (direction == GradientDirection.Normal){
            for (int a = 0; a < dir.length; a++){
                dir[a] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            }
        }else if (direction == GradientDirection.DownToUp){
            dir[0] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[1] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[2] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[3] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
        }else if (direction == GradientDirection.UpToDown) {
            dir[0] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[1] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[2] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[3] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
        }else if (direction == GradientDirection.RightToLeft){
            dir[0] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[1] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[2] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[3] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
        }else if (direction == GradientDirection.LeftToRight){
            dir[0] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
            dir[1] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[2] = new Color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha());
            dir[3] = new Color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha());
        }else {
            for (int a = 0; a < dir.length; a++){
                dir[a] = new Color(255, 255, 255);
            }
        }
        return dir;
    }
    
    public enum HalfRoundedDirection {
        Top,
        Bottom,
        Left,
        Right;
    }
    public enum GradientDirection {
        LeftToRight,
        RightToLeft,
        UpToDown,
        DownToUp,
        Normal
    }
}
