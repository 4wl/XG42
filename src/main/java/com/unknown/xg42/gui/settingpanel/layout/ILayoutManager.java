package com.unknown.xg42.gui.settingpanel.layout;

import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;

import java.util.List;

public interface ILayoutManager {
    int[] getOptimalDiemension(List<AbstractComponent> components, int maxWidth);

    Layout buildLayout(List<AbstractComponent> components, int width, int height);
}
