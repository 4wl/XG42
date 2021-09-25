package com.unknown.xg42.module.modules.chat;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.module.Category;
import com.unknown.xg42.module.Module;

@Module.Info(name = "CommandConfig", category = Category.CLIENT)
public class CommandConfig extends Module {

    @Override
    public void onEnable() {
        Command.sendChatMessage("AwA (?");
    }
}
