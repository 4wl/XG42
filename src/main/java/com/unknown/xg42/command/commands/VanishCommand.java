package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;
import com.unknown.xg42.command.syntax.parsers.EnumParser;
import com.unknown.xg42.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

/**
 * Created by d1gress/Qther on 25/11/2017.
 */

public class VanishCommand extends Command {

    private static Entity vehicle;
    Minecraft mc = Minecraft.getMinecraft();

    public VanishCommand() {
        super("vanish", new ChunkBuilder()
                .append("vanish", true, new EnumParser(new String[]{"dismount", "remount"}))
                .build());
        setDescription("Allows you to vanish using an entity");
    }

    @Override
    public void call(String[] args) {
        if (args.length == 0) {
            Command.sendChatMessage("vanish <dismount/remount>");
            return;
        }

        try {
            String key = args[0];
            if (key.equalsIgnoreCase("dismount")) ModuleManager.getModuleByName("EntityDeSync").enable();
            if (key.equalsIgnoreCase("remount")) ModuleManager.getModuleByName("EntityDeSync").disable();
        } catch (Exception e) {
            Command.sendChatMessage("vanish <dismount/remount>");
        }
        if (mc.player.getRidingEntity() != null && vehicle == null) {
            vehicle = mc.player.getRidingEntity();
            mc.player.dismountRidingEntity();
            mc.world.removeEntityFromWorld(vehicle.getEntityId());
            Command.sendChatMessage("Vehicle " + vehicle.getName() + " removed.");
        } else {
            if (vehicle != null) {
                vehicle.isDead = false;
                mc.world.addEntityToWorld(vehicle.getEntityId(), vehicle);
                mc.player.startRiding(vehicle, true);
                Command.sendChatMessage("Vehicle " + vehicle.getName() + " created.");
                vehicle = null;
            } else {
                Command.sendChatMessage("No Vehicle.");
            }
        }
    }
}
