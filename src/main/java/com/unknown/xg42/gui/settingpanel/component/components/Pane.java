package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.layout.ILayoutManager;
import com.unknown.xg42.gui.settingpanel.layout.Layout;

import java.util.*;

public class Pane extends AbstractComponent {
    protected List<AbstractComponent> components = new ArrayList<>();
    protected Map<AbstractComponent, int[]> componentLocations = new HashMap<>();
    protected Layout layout;
    private ILayoutManager layoutManager;

    public Pane(ILayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void render() {
        if (isSizeChanged()) {
            updateLayout();
            resetSizeChanged();
        }

        updateComponentLocation();

        for (AbstractComponent component : components) {
            component.render();
        }
    }

    @Override
    public boolean isSizeChanged() {
        for (AbstractComponent component : components) {
            if (component.isSizeChanged()) {
                return true;
            }
        }

        return super.isSizeChanged();
    }

    private void resetSizeChanged() {
        for (AbstractComponent component : components) {
            component.setSizeChanged(false);
        }
    }

    protected void updateComponentLocation() {
        for (AbstractComponent component : components) {
            int[] ints = componentLocations.get(component);

            if (ints == null) {
                updateLayout();
                updateComponentLocation();

                return;
            }

            component.setX(getX() + ints[0]);
            component.setY(getY() + ints[1]);
        }
    }

    public void updateLayout() {
        updateLayout(getWidth(), getHeight(), true);
    }

    protected void updateLayout(int width, int height, boolean changeHeight) {
        layout = layoutManager.buildLayout(components, width, height);
        componentLocations = layout.getComponentLocations();

        if (changeHeight) setHeight(layout.getMaxHeight());
    }

    public void addComponent(AbstractComponent component) {
        components.add(component);

        updateLayout(super.getWidth(), super.getHeight(), true);
    }

    public List<AbstractComponent> getComponent() {
        return components;
    }

    public void removeComponent(AbstractComponent component) {
        components.remove(component);

        updateLayout(super.getWidth(), super.getHeight(), true);
    }

    @Override
    public boolean mouseMove(int x, int y, boolean offscreen) {
        final boolean[] consumed = {false};

        components.stream().sorted(Comparator.comparingInt(AbstractComponent::getEventPriority)).forEach(component -> {
            if (!consumed[0])
                if (component.mouseMove(x, y, offscreen))
                    consumed[0] = true;
        });

        return consumed[0];
    }

    @Override
    public boolean mousePressed(int button, int x, int y, boolean offscreen) {
        final boolean[] consumed = {false};

        components.stream().sorted(Comparator.comparingInt(AbstractComponent::getEventPriority)).forEach(component -> {
            if (!consumed[0])
                if (component.mousePressed(button, x, y, offscreen))
                    consumed[0] = true;
        });

        return consumed[0];
    }

    @Override
    public boolean mouseReleased(int button, int x, int y, boolean offscreen) {
        final boolean[] consumed = {false};

        components.stream().sorted(Comparator.comparingInt(AbstractComponent::getEventPriority)).forEach(component -> {
            if (!consumed[0])
                if (component.mouseReleased(button, x, y, offscreen))
                    consumed[0] = true;
        });

        return consumed[0];
    }

    @Override
    public boolean keyPressed(int key, char c) {
        final boolean[] consumed = {false};

        components.stream().sorted(Comparator.comparingInt(AbstractComponent::getEventPriority)).forEach(component -> {
            if (!consumed[0])
                if (component.keyPressed(key, c))
                    consumed[0] = true;
        });

        return consumed[0];
    }

    @Override
    public int getWidth() {
        if (super.getWidth() <= 0) updateSize();

        return super.getWidth();
    }

    @Override
    public int getHeight() {
        if (super.getHeight() <= 0) updateSize();

        return super.getHeight();
    }

    private void updateSize() {
        for (AbstractComponent component : components) {
            if (component instanceof Pane) {
                ((Pane) component).updateSize();
            }
        }
        int[] optimalDiemension = layoutManager.getOptimalDiemension(components, Integer.MAX_VALUE);

        if (super.getWidth() <= 0) setWidth(optimalDiemension[0]);
        if (super.getHeight() <= 0) setHeight(optimalDiemension[1]);
    }

    public void clearComponents() {
        components.clear();

        updateLayout(super.getWidth(), super.getHeight(), true);
    }

}
