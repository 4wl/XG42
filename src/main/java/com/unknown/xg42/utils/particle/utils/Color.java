package com.unknown.xg42.utils.particle.utils;

public class Color {

    public static java.awt.Color rainbow(float speed, float off) {

        double time = (double) System.currentTimeMillis() / speed;
        time += off;
        time %= 255.0f;
        return java.awt.Color.getHSBColor((float) (time / 255.0f), 1.0f, 1.0f);

    }

}
