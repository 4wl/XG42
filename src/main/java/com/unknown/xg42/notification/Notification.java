package com.unknown.xg42.notification;

import com.unknown.xg42.XG42;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.font.CFontRenderer;
import net.minecraft.client.Minecraft;

import java.awt.*;

public abstract class Notification {

    protected final NotificationType type;
    protected final String title;
    protected final String message;
    protected long start;
    protected final long fadedIn;
    protected final long fadeOut;
    protected final long end;
    protected CFontRenderer font = XG42.getFontRenderer();

    public Notification(NotificationType type, String title, String message, int length) {
        this.type = type;
        this.title = title;
        this.message = message;
        fadedIn = 200L * length;
        fadeOut = fadedIn + 500L * length;
        end = fadeOut + fadedIn;
    }

    public void show() {
        start = System.currentTimeMillis();
    }
    public boolean isShown() {
        return getTime() <= end;
    }
    protected long getTime() {
        return System.currentTimeMillis() - start;
    }
    protected int getOffset(double maxWidth){
        if (getTime() < fadedIn) {
            return (int) (Math.tanh(getTime() / (double) (fadedIn) * 3.0) * maxWidth);
        } else if (getTime() > fadeOut) {
            return (int) (Math.tanh(3.0 - (getTime() - fadeOut) / (double) (end - fadeOut) * 3.0) * maxWidth);
        } else {
            return (int)maxWidth;
        }
    }
    protected Color getDefaultTypeColor(){
        if (type == NotificationType.INFO){
            return Color.BLUE;
        }else if (type == NotificationType.WARNING) {
            return new Color(218,165,32);
        }else if (type == NotificationType.RAINBOW) {
            return Rainbow.getRainbowColor(7, 0.75f, 1);
        }else if (type == NotificationType.ERROR) {
            return Color.RED;
        }else {
            return Color.RED;
        }
    }
    public abstract void render(int RealDisplayWidth, int RealDisplayHeight);
}
