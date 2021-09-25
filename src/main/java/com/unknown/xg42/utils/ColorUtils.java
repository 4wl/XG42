package com.unknown.xg42.utils;

import java.awt.*;

public class ColorUtils {

    /**
     * @param oldColor start color
     * @param newColor change color
     * @param step step lol
     * @param currentStep
     */
    public static Color calculateChangeColor(Color oldColor, Color newColor, int step, int currentStep){
        int r,g,b,a;
        r = Math.max(0, Math.min(255, oldColor.getRed()   + (newColor.getRed()   - oldColor.getRed())   * currentStep / step));
        g = Math.max(0, Math.min(255, oldColor.getGreen() + (newColor.getGreen() - oldColor.getGreen()) * currentStep / step));
        b = Math.max(0, Math.min(255, oldColor.getBlue()  + (newColor.getBlue()  - oldColor.getBlue())  * currentStep / step));
        a = Math.max(0, Math.min(255, oldColor.getAlpha()  + (newColor.getAlpha()  - oldColor.getAlpha())  * currentStep / step));
        return new Color(r, g, b, a);
    }

    public static Integer calculateAlphaChangeColor(int oldAlpha, int newAlpha, int step, int currentStep){
        return Math.max(0, Math.min(255, oldAlpha  + (newAlpha  - oldAlpha)  * currentStep / step));
    }

}
