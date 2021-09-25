package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.Window;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.utils.GLUtil;

public class Label extends AbstractComponent {
    private String text;

    public Label(String text) {
        setText(text);
    }

    @Override
    public void render() {
//        glClearDepthf(1.0f);
//        glClear(GL_DEPTH_BUFFER_BIT);
//        glColorMask(false, false, false, false);
//        glDepthFunc(GL_LESS);
//        glEnable(GL_DEPTH_TEST);
//        glDepthMask(true);

//        renderer.drawRect(x, y, getWidth() / 1.5, getHeight(), Color.white);


//        glColorMask(true, true, true, true);
//        glDepthMask(true);
//        glDepthFunc(GL_EQUAL);

          GLUtil.FontRenderer.drawString(text, x, y, Window.FOREGROUND.getRGB());

//        glDisable(GL_DEPTH_TEST);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        setWidth(GLUtil.FontRenderer.getStringWidth(text));
        setHeight(GLUtil.FontRenderer.getHeight());

        this.text = text;
    }
}
