package com.unknown.xg42.gui.mcguichat;

import com.unknown.xg42.mixin.client.MixinIGuiChat;
import net.minecraft.client.gui.GuiChat;

import java.io.IOException;

public class DirGuiChat extends GuiChat {

    public DirGuiChat(String chatLine, int cursor) {
        super(chatLine);
        this.cursor = cursor;
    }

    int cursor;

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        ((MixinIGuiChat) this).setSentHistoryCursor(cursor);
        super.keyTyped(typedChar, keyCode);
        cursor = ((MixinIGuiChat)this).getSentHistoryCursor();
    }
}
