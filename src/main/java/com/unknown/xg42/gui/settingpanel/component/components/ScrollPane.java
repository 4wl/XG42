package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.layout.ILayoutManager;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;
import com.unknown.xg42.utils.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL41.glClearDepthf;

public class ScrollPane extends Pane {
    private static final double SCROLL_AMOUNT = 0.25;
    private int scrollOffset = 0;
    private boolean hovered = false;
    private int realHeight;

    public ScrollPane(ILayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public void updateLayout() {
        updateLayout(getWidth(), Integer.MAX_VALUE, true);
    }

    @Override
    protected void updateLayout(int width, int height, boolean changeHeight) {
        super.updateLayout(width, height, false);

        realHeight = layout.getMaxHeight();
        validateOffset();
    }

    @Override
    public void render() {
        glClearDepthf(1.0f);
        glClear(GL_DEPTH_BUFFER_BIT);
        glColorMask(false, false, false, false);
        glDepthFunc(GL_LESS);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);

        GLUtil.drawRect(GL11.GL_QUADS, x, y, getWidth(), getHeight(), new Color(255,255,255).getRGB());

        glColorMask(true, true, true, true);
        glDepthMask(true);
        glDepthFunc(GL_EQUAL);

        super.render();

        glClearDepthf(1.0f);
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glDepthMask(false);

        int maxY = realHeight - getHeight();

        if (maxY > 0) {
            int sliderHeight = (int) (getHeight() / (double) realHeight * (double) getHeight());
            int sliderWidth = 3;
            RenderUtils.drawRoundedRectangle(x + getWidth() - sliderWidth, y + (getHeight() - sliderHeight) * (scrollOffset / (double)maxY), sliderWidth, sliderHeight, 1.0f, Color.white);
        }
    }


    @Override
    protected void updateComponentLocation() {
        for (AbstractComponent component : components) {
            int[] ints = componentLocations.get(component);

            if (ints == null) {
                updateLayout();
                updateComponentLocation();

                return;
            }

            component.setX(x + ints[0]);
            component.setY(y + ints[1] - scrollOffset);
        }
    }


    private void updateHovered(int x, int y, boolean offscreen) {
        hovered = !offscreen && x >= this.x && y >= this.y && x <= this.x + getWidth() && y <= this.y + getHeight();
    }

    @Override
    public boolean mouseWheel(int change) {
        scrollOffset -= change * SCROLL_AMOUNT;

        validateOffset();

        return super.mouseWheel(change);
    }

    private void validateOffset() {
        if (scrollOffset > realHeight - getHeight()) {
            scrollOffset = realHeight - getHeight();
        }

        if (scrollOffset < 0) {
            scrollOffset = 0;
        }
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        updateHovered(x, y, offscreen);

        return super.mouseMove(x, y, offscreen || x < this.x || y < this.y || x > this.x + getWidth() || y > this.y + getHeight());
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        return super.mousePressed(button, x, y, offscreen || x < this.x || y < this.y || x > this.x + getWidth() || y > this.y + getHeight());
    }

    @Override
    public boolean mouseReleased(int button, int x, int y, boolean offscreen) {
        return super.mouseReleased(button, x, y, offscreen || x < this.x || y < this.y || x > this.x + getWidth() || y > this.y + getHeight());
    }

    @Override
    public void addComponent(AbstractComponent component) {
        super.addComponent(component);
    }
}
