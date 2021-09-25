package com.unknown.xg42.mixin.client;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.gui.mcguichat.XG42GUIChat;
import com.unknown.xg42.utils.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat {

    @Shadow
    protected GuiTextField inputField;

    @Shadow
    public String historyBuffer;

    @Shadow
    public int sentHistoryCursor;

    @Shadow
    public abstract void initGui();

    @Inject(method = "Lnet/minecraft/client/gui/GuiChat;keyTyped(CI)V", at = @At("RETURN"))
    public void returnKeyTyped(char typedChar, int keyCode, CallbackInfo info) {
        if (!(Wrapper.getMinecraft().currentScreen instanceof GuiChat) || Wrapper.getMinecraft().currentScreen instanceof XG42GUIChat)
            return;
        if (inputField.getText().startsWith(Command.getCommandPrefix())) {
            Wrapper.getMinecraft().displayGuiScreen(new XG42GUIChat(inputField.getText(), historyBuffer, sentHistoryCursor));
        }
    }

}
