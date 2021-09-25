package com.unknown.xg42.manager;

import com.unknown.xg42.module.modules.client.Colors;
import com.unknown.xg42.utils.ColorUtil;
import com.unknown.xg42.utils.Rainbow;

import java.awt.*;

public class GuiManager {

    static GuiManager INSTANCE;

    public static GuiManager getINSTANCE(){
        return INSTANCE;
    }

    public GuiManager(){
        INSTANCE = this;
    }

    public Colors getColorINSTANCE(){
        return Colors.getINSTANCE();
    }

    public boolean isNull(){
        return Colors.getINSTANCE() == null;
    }

    public int getNormalRed(){
        return isNull() ? 255 : getColorINSTANCE().red.getValue();
    }

    public int getNormalGreen(){
        return isNull() ? 0 : getColorINSTANCE().green.getValue();
    }

    public int getNormalBlue(){
        return isNull() ? 0 : getColorINSTANCE().blue.getValue();
    }

    public boolean isRainbow(){
        return !isNull() && getColorINSTANCE().rainbow.getValue();
    }

    public int getGradientIntensity(){
        return getColorINSTANCE().GradientIntensity.getValue();
    }

    public int getRainbowColor(){
        final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32) * getColorINSTANCE().rainbowSpeed.getValue()};
        return Color.HSBtoRGB(hue[0], getColorINSTANCE().rainbowSaturation.getValue(), getColorINSTANCE().rainbowBrightness.getValue());
    }

    public int getRainbowColorAdd(long add, int alpha){
        Color c = new Color(Rainbow.getRainbow(getColorINSTANCE().rainbowSpeed.getValue(), getColorINSTANCE().rainbowSaturation.getValue(), getColorINSTANCE().rainbowBrightness.getValue(), add));
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB();
    }

    public int getRainbowColorAdd(long add){
        return getRainbowColorAdd(add, 255);
    }

    public int getRainbowRed(){
        return ColorUtil.getRed(getRainbowColor());
    }
    public int getRainbowGreen(){
        return ColorUtil.getGreen(getRainbowColor());
    }
    public int getRainbowBlue(){
        return ColorUtil.getBlue(getRainbowColor());
    }

    public int getNormalRGB(){
        return new Color(getNormalRed(),getNormalGreen(),getNormalBlue()).getRGB();
    }

    public int getRed(){ return isRainbow() ? getRainbowRed() : getNormalRed(); }

    public int getGreen(){ return isRainbow() ? getRainbowGreen() : getNormalGreen(); }

    public int getBlue(){ return isRainbow() ? getRainbowBlue() : getNormalBlue(); }

    public int getRGB(){
        return new Color(getRed(),getGreen(),getBlue()).getRGB();
    }

    public static int getBGColor(int alpha) { return new Color(64, 64, 64, alpha).getRGB(); }

    public boolean isParticle(){
        return !isNull() && getColorINSTANCE().particle.getValue();
    }

    public boolean isSettingRect() { return !isNull() && getColorINSTANCE().setting.getMode("Rect").isToggled(); }

    public boolean isSettingSide() { return !isNull() && getColorINSTANCE().setting.getMode("Side").isToggled(); }

    public Background getBackground(){
        switch (getColorINSTANCE().background.getToggledMode().getName()){
            case "Shadow" :{
                return Background.Shadow;
            }
            case "Blur" :{
                return Background.Blur;
            }
            case "Both" :{
                return Background.Both;
            }
            default:
                return Background.None;
        }
    }

    public enum Background{
        Shadow,
        Blur,
        Both,
        None;
    }
}
