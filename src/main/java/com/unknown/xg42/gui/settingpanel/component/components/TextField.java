package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ValueChangeListener;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.utils.Rainbow;
import com.unknown.xg42.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class TextField extends AbstractComponent {

    private static final int PREFERRED_WIDTH = 180;
    private static final int PREFERRED_HEIGHT = 22;

    private boolean typing;
    private String title;
    private String value;
    private int preferredWidth;
    private int preferredHeight;
    private boolean hovered;
    private ValueChangeListener<String> listener;

    public TextField(String title, int preferredWidth, int preferredHeight) {

        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        setWidth(preferredWidth);
        setHeight(preferredHeight);

        setTitle(title);
    }

    public TextField(String title) {
        this(title, PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }

    @Override
    public void render() {
        RenderUtils.drawRoundedRectangle(x ,y, getWidth(), getHeight(), 7.0f, hovered ? Window.SECONDARY_FOREGROUND : Window.TERTIARY_FOREGROUND);
        if (Window.isRainbow){
            RenderUtils.drawRoundedRectangleOutline( x, y, getWidth(), getHeight(), 7.0f, 1.0f, RenderUtils.GradientDirection.LeftToRight, Rainbow.getRainbowColor(10, 0.8f, 1f), Rainbow.getRainbowColor(10, 0.8f, 1f, 1000));
        }else {
            RenderUtils.drawRoundedRectangleOutline(x, y, getWidth(), getHeight(), 7.0f, 1.0f,  hovered ? Window.SECONDARY_OUTLINE : Window.SECONDARY_FOREGROUND);
        }
        Minecraft.getMinecraft().fontRenderer.drawString(title, x + getWidth() / 2 - GLUtil.FontRenderer.getStringWidth(title) / 2, (y + getHeight() / 2) - GLUtil.FontRenderer.getHeight() / 2, Window.FOREGROUND.getRGB());
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);
        return false;
    }

    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + getHeight();
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        if (button == 0) {
            updateHovered(x, y, offscreen);
            if (hovered) {
                typing = !typing;
                TypeDir = value;
                update();
                return true;
            }
        }

        return typing;
    }

    private String TypeDir = "";

    @Override
    public boolean keyPressed(int key, char c) {
        if (typing) {
            if (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK){
                TypeDir = removeLastChar(TypeDir);
            }else if (key == Keyboard.KEY_RETURN){
                typing = false;
            }else if (!String.valueOf(c).contains("\u0000")){
                TypeDir += c;
            }
            update();
        }

        return typing;
    }

    public void changeValue(String newValue) {
        boolean change;
        change = listener.onValueChange(newValue);
        if (change) {
            value = newValue;
        }
    }

    public void update() {
        if (typing)
            setTitle(TypeDir + "...");
        else
            setTitle(value);
        changeValue(TypeDir);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        setWidth(Math.max(GLUtil.FontRenderer.getStringWidth(title), preferredWidth));
        setHeight(Math.max(GLUtil.FontRenderer.getHeight() * 5 / 4, preferredHeight));
    }

    public static String removeLastChar(final String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setListener(ValueChangeListener<String> listener) {
        this.listener = listener;
    }
}
