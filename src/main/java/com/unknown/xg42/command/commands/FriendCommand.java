package com.unknown.xg42.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.ChunkBuilder;
import com.unknown.xg42.command.syntax.parsers.EnumParser;
import com.unknown.xg42.manager.FriendManager;
import com.unknown.xg42.utils.other.Friend;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 086 on 14/12/2017.
 */
public class FriendCommand extends Command {

    public FriendCommand() {
        super("friend", new ChunkBuilder()
                .append("mode", true, new EnumParser("add", "del"))
                .append("name")
                .build(), "f");
        setDescription("Add someone as your friend!");
    }

    @Override
    public void call(String[] args) {
        if (args[0] == null) {
            if (FriendManager.INSTANCE.friends.isEmpty()) {
                Command.sendChatMessage("You currently don't have any friends added. &bfriend add <name>&r to add one.");
                return;
            }
            String f = "";
            for (Friend friend : FriendManager.INSTANCE.friends)
                f += friend.name + ", ";
            f = f.substring(0, f.length() - 2);
            Command.sendChatMessage("Your friends: " + f);
            return;
        } else {
            if (args[1] == null) {
                Command.sendChatMessage(String.format(FriendManager.isFriend(args[0]) ? "Yes, %s is your friend." : "No, %s isn't a friend of yours.", args[0]));
                return;
            }

            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("new")) {
                if (FriendManager.isFriend(args[1])) {
                    Command.sendChatMessage("That player is already your friend.");
                    return;
                }

                // New thread because of potential internet connection made
                new Thread(() -> {
                    Friend f = new Friend(args[1], true);
                    if (f == null) {
                        Command.sendChatMessage("Failed to find UUID of " + args[1]);
                        return;
                    }
                    FriendManager.INSTANCE.friends.add(f);
                    Command.sendChatMessage("&b" + f.name + "&r has been friended.");
                }).start();

                return;
            } else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                if (!FriendManager.isFriend(args[1])) {
                    Command.sendChatMessage("That player isn't your friend.");
                    return;
                }

                Friend friend = FriendManager.INSTANCE.friends.stream().filter(friend1 -> friend1.name.equalsIgnoreCase(args[1])).findFirst().get();
                FriendManager.INSTANCE.friends.remove(friend);
                Command.sendChatMessage("&b" + friend.name + "&r has been unfriended.");
                return;
            } else {
                Command.sendChatMessage("Please specify either &6add&r or &6remove");
                return;
            }
        }
    }
}
