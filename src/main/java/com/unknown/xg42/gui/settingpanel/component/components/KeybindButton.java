package com.unknown.xg42.gui.settingpanel.component.components;

import com.unknown.xg42.gui.settingpanel.component.ActionEventListener;
import com.unknown.xg42.gui.settingpanel.component.ValueChangeListener;
import com.unknown.xg42.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.function.Function;

public class KeybindButton extends Button {
    private ValueChangeListener<Integer> listener;
    private boolean listening;
    private Module module;
    private int value;

    public KeybindButton(int preferredWidth, int preferredHeight, Module module) {
        super("", preferredWidth, preferredHeight);
        this.module = module;

        initialize();
    }

    public KeybindButton(Module module) {
        super("");
        this.module = module;

        initialize();
    }

    private void initialize() {
        setOnClickListener(() -> {
            listening = !listening;

            updateState();
        });

        updateState();
    }

    @Override
    public void setOnClickListener(ActionEventListener listener) {
        if (getOnClickListener() != null) {
            ActionEventListener old = getOnClickListener();

            super.setOnClickListener(() -> {
                listener.onActionEvent();
                old.onActionEvent();
            });
        } else {
            super.setOnClickListener(listener);
        }

    }

    @Override
    public boolean keyPressed(int key, char c) {
        if (listening) {
            listening = false;

            if (Keyboard.getEventKey() != 256 && Keyboard.getEventCharacter() != 0) {
                int newValue = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();


                if (listener != null)
                    if (listener.onValueChange(newValue))
                        this.value = newValue;
            }

            updateState();
        }

        return super.keyPressed(key, c);
    }

    @Override
    public int getEventPriority() {
        return listening ? super.getEventPriority() + 1 : super.getEventPriority();
    }

    private void updateState() {
        if (listening) {
            setTitle("Press a button...");
        } else {
            setTitle(Keyboard.getKeyName(module.getBind()));
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        updateState();
    }

    public void setListener(ValueChangeListener<Integer> listener) {
        this.listener = listener;
    }
}
