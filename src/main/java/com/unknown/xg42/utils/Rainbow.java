package com.unknown.xg42.utils;

import java.awt.*;

public class Rainbow {
    public static int getRainbow(float speed, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % (360 * 32)) / (360f * 32) * speed;
        return Color.HSBtoRGB(hue , saturation, brightness);
    }

    public static Color getRainbowColor(float speed, float saturation, float brightness) {
        return new Color(getRainbow(speed, saturation, brightness));
    }

    public static Color getRainbowColor(float speed, float saturation, float brightness, long add){
        return new Color(getRainbow(speed, saturation, brightness, add));
    }

    public static int getRainbow(float speed, float saturation, float brightness, long add) {
        float hue = ((System.currentTimeMillis() + add) % (360 * 32)) / (360f * 32) * speed;
        return Color.HSBtoRGB(hue , saturation, brightness);
    }
}
