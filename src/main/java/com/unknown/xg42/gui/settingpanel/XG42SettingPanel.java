package com.unknown.xg42.gui.settingpanel;

import com.unknown.xg42.XG42;
import com.unknown.xg42.gui.settingpanel.component.AbstractComponent;
import com.unknown.xg42.gui.settingpanel.component.ActionEventListener;
import com.unknown.xg42.gui.settingpanel.component.components.*;
import com.unknown.xg42.gui.settingpanel.component.components.Button;
import com.unknown.xg42.gui.settingpanel.component.components.Label;
import com.unknown.xg42.gui.settingpanel.component.components.ScrollPane;
import com.unknown.xg42.gui.settingpanel.component.components.TextField;
import com.unknown.xg42.gui.settingpanel.layout.FlowLayout;
import com.unknown.xg42.gui.settingpanel.layout.GridLayout;
import com.unknown.xg42.gui.settingpanel.utils.UserValueChangeListener;
import com.unknown.xg42.gui.settingpanel.utils.Utils;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.Module;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.setting.*;
import com.unknown.xg42.utils.Wrapper;
import com.unknown.xg42.utils.particle.ParticleSystem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class XG42SettingPanel extends GuiScreen {

    private final Pane spoilerPane;
    private final HashMap<Category, Pane> categoryPaneMap;
    private Window window;
    private final List<ActionEventListener> onRenderListeners = new ArrayList<>();
    private ParticleSystem particleSystem;

    public XG42SettingPanel() {
        window = new Window(XG42.MOD_NAME, 50, 50, 180, 400);

        Pane conentPane = new ScrollPane(new GridLayout(1));

        Pane buttonPane = new Pane(new GridLayout(1));

        HashMap<Category, List<Module>> moduleCategoryMap = new HashMap<>();
        categoryPaneMap = new HashMap<>();

        for (IModule module : ModuleManager.getModules()) {
            if (module instanceof Module) {
                if (!moduleCategoryMap.containsKey(module.category)) {
                    if (!module.category.isHidden())
                        moduleCategoryMap.put(module.category, new ArrayList<>());
                }
                if (!module.category.isHidden())
                    moduleCategoryMap.get(module.category).add((Module) module);
            }
        }

        List<Spoiler> spoilers = new ArrayList<>();
        List<Pane> paneList = new ArrayList<>();

        HashMap<Category, Pane> paneMap = new HashMap<>();

        for (Map.Entry<Category, List<Module>> moduleCategoryListEntry : moduleCategoryMap.entrySet()) {
            Pane spoilerPane = new Pane(new GridLayout(1));
            for (Module module : moduleCategoryListEntry.getValue()) {
                Pane settingPane = new Pane(new GridLayout(4));
                Spoiler spoiler = new Spoiler(module.getName(), 150, settingPane);
                Spoiler addPanelSpoiler = updateSpoiler(spoiler);
                paneList.add(addPanelSpoiler.getContentPane());
                spoilers.add(addPanelSpoiler);
                spoilerPane.addComponent(spoiler);
                paneMap.put(moduleCategoryListEntry.getKey(), spoilerPane);
            }
            categoryPaneMap.put(moduleCategoryListEntry.getKey(), spoilerPane);
        }

        spoilerPane = new Pane(new GridLayout(1));


        for (Category moduleCategory : categoryPaneMap.keySet()) {
            Button button;
            buttonPane.addComponent(button = new Button(moduleCategory.toString(), 120, 17));
            button.setOnClickListener(() -> setCurrentCategory(moduleCategory));
        }

        int maxWidth = Integer.MIN_VALUE;

        for (Pane pane : paneList) {
            maxWidth = Math.max(maxWidth, pane.getWidth() + buttonPane.getWidth());
        }

        window.setWidth(28 + maxWidth);

        for (Spoiler spoiler : spoilers) {
            spoiler.preferredWidth = maxWidth -buttonPane.getWidth();
            spoiler.setWidth(maxWidth -buttonPane.getWidth());
        }

        spoilerPane.setWidth(maxWidth);
        buttonPane.setWidth(maxWidth);
        conentPane.addComponent(spoilerPane);
        conentPane.updateLayout();
        window.setContentPane(buttonPane);
        window.setSpoilerPane(conentPane);

        if (categoryPaneMap.keySet().size() > 0)
            setCurrentCategory(categoryPaneMap.keySet().iterator().next());
    }

    @Override
    public void initGui() {
        particleSystem = new ParticleSystem(167, 87);
        if (OpenGlHelper.shadersSupported && Wrapper.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
            if (Wrapper.getMinecraft().entityRenderer.getShaderGroup() != null) {
                Wrapper.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            Wrapper.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }

    }

    @Override
    public void onGuiClosed() {
        if (Wrapper.getMinecraft().entityRenderer.getShaderGroup() != null) {
            Wrapper.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        }

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private Category CurrentCategory;

    private void setCurrentCategory(Category category) {
        spoilerPane.clearComponents();
        spoilerPane.addComponent(categoryPaneMap.get(category));
        CurrentCategory = category;
    }

    private Spoiler updateSpoiler(Spoiler spoiler) {
        String name = spoiler.getTitle();
        IModule module = ModuleManager.getModuleByName(name);

        Pane settingPane = new Pane(new GridLayout(4));

        {
            settingPane.addComponent(new Label("State"));
            CheckBox cb;
            settingPane.addComponent(cb = new CheckBox("Enabled"));
            onRenderListeners.add(() -> cb.setSelected(module.isEnabled()));
            cb.setListener(val -> {
                if (mc.player != null && mc.world != null) {
                    module.toggle();
                    return true;
                } else {
                    return false;
                }
            });
        }

        {
            settingPane.addComponent(new Label("Keybind"));
            KeybindButton kb;
            settingPane.addComponent(kb = new KeybindButton((Module) module));
            onRenderListeners.add(() -> kb.setValue(module.getBind()));

            kb.setListener(val -> {
                module.setBind(val);
                return true;
            });
        }

        if (!module.getSettingList().isEmpty()) {
            for (Setting value : module.getSettingList()) {
                if (!value.visible()) continue;
                if (value instanceof BooleanSetting) {
                    settingPane.addComponent(new Label(value.getName()));

                    CheckBox cb;
                    settingPane.addComponent(cb = new CheckBox(value.getName()));
                    cb.setListener(newValue -> {
                        value.setValue(newValue);
                        return true;
                    });
                    onRenderListeners.add(() -> cb.setSelected(((BooleanSetting) value).getValue()));
                }
                if (value instanceof ModeSetting) {
                    List<ModeSetting.Mode> type = ((ModeSetting) value).getModes();
                    String[] modes = type.stream().map(ModeSetting.Mode::getName).toArray(String[]::new);
                    settingPane.addComponent(new Label(value.getName()));
                    ComboBox cb;

                    int EnumValue = 0;

                    for (int i = 0; i < modes.length; i++) {
                        if (((ModeSetting) value).getValue() == type.get(i)) {
                            EnumValue = i;
                            continue;
                        }
                    }

                    settingPane.addComponent(cb = new ComboBox(modes, EnumValue));
                    cb.setListener(object -> {
                        value.setValue(type.get(object));
                        return true;
                    });
                    onRenderListeners.add(() -> cb.setSelectedIndex(modes, type, value));
                }
                if (value instanceof IntegerSetting || value instanceof FloatSetting || value instanceof DoubleSetting) {


                    settingPane.addComponent(new Label(value.getName()));
                    Slider cb;
                    Slider.NumberType type = Slider.NumberType.DECIMAL;
                    double max = 0;
                    double min = 0;
                    if (value instanceof IntegerSetting){
                        max = Double.parseDouble(((IntegerSetting) value).getMax().toString());
                        min = Double.parseDouble(((IntegerSetting) value).getMin().toString());
                    }else if (value instanceof FloatSetting){
                        max = Double.parseDouble(((FloatSetting) value).getMax().toString());
                        min = Double.parseDouble(((FloatSetting) value).getMin().toString());
                    }else if (value instanceof DoubleSetting){
                        max = Double.parseDouble(((DoubleSetting) value).getMax().toString());
                        min = Double.parseDouble(((DoubleSetting) value).getMin().toString());
                    }else {
                        max = Double.parseDouble(String.valueOf(Integer.MAX_VALUE));
                        min = Double.parseDouble(String.valueOf(Integer.MAX_VALUE));
                    }

                    if (value.getValue() instanceof Integer) {
                        type = Slider.NumberType.INTEGER;
                    } else if (value.getValue() instanceof Long) {
                        type = Slider.NumberType.TIME;
                    } else if (value.getValue() instanceof Float && (int) min == 0 && (int) max == 100) {
                        type = Slider.NumberType.PERCENT;
                    }

                    double NumberR = Double.parseDouble(value.getValue().toString());

                    settingPane.addComponent(cb = new Slider(NumberR, min, max, type));
                    cb.setListener(val -> {
                        if (value.getValue() instanceof Integer) {
                            value.setValue(val.intValue());
                        }
                        if (value.getValue() instanceof Float) {
                            value.setValue(val.floatValue());
                        }
                        if (value.getValue() instanceof Long) {
                            value.setValue(val.longValue());
                        }
                        if (value.getValue() instanceof Double) {
                            value.setValue(val.doubleValue());
                        }

                        return true;
                    });
                    onRenderListeners.add(() -> cb.setValue(Double.parseDouble(value.getValue().toString())));
                }
                if (value instanceof StringSetting) {
                    settingPane.addComponent(new Label(value.getName()));
                    TextField tf;
                    settingPane.addComponent(tf = new TextField(((StringSetting) value).getValue()));
                    tf.setListener(newValue -> {
                        value.setValue(String.valueOf(newValue));
                        return true;
                    });
                    onRenderListeners.add(() -> tf.setValue(((StringSetting) value).getValue()));
                }
            }
        }
        spoiler.setContentPane(settingPane);
        return spoiler;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        {
            if (UserValueChangeListener.isValueChange) {
                Pane i = categoryPaneMap.get(CurrentCategory);
                for (AbstractComponent ff : i.getComponent()) {
                    updateSpoiler(((Spoiler) ff));
                }
                UserValueChangeListener.reset();
            }
            for (ActionEventListener onRenderListener : onRenderListeners) {
                onRenderListener.onActionEvent();
            }
            particleSystem.tick(10);
            Point point = Utils.calculateMouseLocation();
            window.mouseMoved(point.x, point.y);
        }

        GL11.glPushMatrix();
        {
            particleSystem.render();
            window.render();
        }
        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        window.mouseMoved(mouseX, mouseY);
        window.mousePressed(mouseButton, mouseX, mouseY);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        window.mouseMoved(mouseX, mouseY);
        window.mouseReleased(state, mouseX, mouseY);

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        window.mouseMoved(mouseX, mouseY);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int eventDWheel = Mouse.getEventDWheel();

        window.mouseWheel(eventDWheel);

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        window.keyPressed(keyCode, typedChar);
        super.keyTyped(typedChar, keyCode);
    }
}