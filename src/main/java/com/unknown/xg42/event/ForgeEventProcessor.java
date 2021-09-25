package com.unknown.xg42.event;

import com.unknown.xg42.XG42;
import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.commands.PeekCommand;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.notification.NotificationManager;
import com.unknown.xg42.utils.Utils;
import com.unknown.xg42.utils.Wrapper;
import com.unknown.xg42.utils.XG42Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class ForgeEventProcessor {

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled() || Utils.nullCheck()) return;
        try {
            ModuleManager.onWorldRender(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            Command.sendChatMessage("RuntimeException: onWorldRender");
            Command.sendChatMessage(ex.toString());
        }
    }

    @SubscribeEvent
    public void onKey(InputUpdateEvent event){
        try {
            ModuleManager.onKey(event);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            Command.sendChatMessage("RuntimeException: onKey");
            Command.sendChatMessage(ex.toString());
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (!event.isCanceled() || !Utils.nullCheck()) {
            try {
                RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;
                if (!Wrapper.getPlayer().isCreative() && Wrapper.getPlayer().getRidingEntity() instanceof AbstractHorse)
                    target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;

                if (event.getType() == target) {
                    ModuleManager.onRender(event);
                    NotificationManager.render();
                    XG42Tessellator.releaseGL();
                }

            } catch (RuntimeException ex) {
                ex.printStackTrace();
                Command.sendChatMessage("RuntimeException: onRender");
                Command.sendChatMessage(ex.toString());
            }
        }else {
            NotificationManager.render();
        }
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.isCanceled() || Utils.nullCheck()) return;

        try {
            ModuleManager.onTick();
            if (PeekCommand.sb != null) {
                ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
                int i = scaledresolution.getScaledWidth();
                int j = scaledresolution.getScaledHeight();
                GuiShulkerBox gui = new GuiShulkerBox(Wrapper.getPlayer().inventory, PeekCommand.sb);
                gui.setWorldAndResolution(Wrapper.getMinecraft(), i, j);
                Minecraft.getMinecraft().displayGuiScreen(gui);
                PeekCommand.sb = null;
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            Command.sendChatMessage("RuntimeException: onTick");
            Command.sendChatMessage(ex.toString());
        }
    }


    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event){
        if (Utils.nullCheck()) return;
        try {
            if (Keyboard.getEventKeyState()) {
                ModuleManager.onBind(Keyboard.getEventKey());
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            Command.sendChatMessage("RuntimeException: onWorldRender");
            Command.sendChatMessage(ex.toString());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                Wrapper.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    XG42.getInstance().getCommandManager().callCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendChatMessage("Please enter a command.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Command.sendChatMessage("Error occured while running command! (" + e.getMessage() + ")");
            }
            event.setMessage("");
        }
    }
}