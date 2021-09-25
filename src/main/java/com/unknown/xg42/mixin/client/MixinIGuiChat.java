package com.unknown.xg42.mixin.client;

import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChat.class)
public interface MixinIGuiChat {
    @Accessor("historyBuffer")
    void setHistoryBuffer(String historyBuffer);

    @Accessor("sentHistoryCursor")
    void setSentHistoryCursor(int sentHistoryCursor);

    @Accessor("historyBuffer")
    String getHistoryBuffer();

    @Accessor("sentHistoryCursor")
    int getSentHistoryCursor();
}
