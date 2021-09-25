package com.unknown.xg42.command;

import com.unknown.xg42.XG42;
import com.unknown.xg42.command.syntax.SyntaxChunk;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {

    protected String label;
    protected String syntax;
    protected String description;
    protected List<String> aliases;

    public final Minecraft mc = Minecraft.getMinecraft();

    protected SyntaxChunk[] syntaxChunks;

    public static String commandPrefix = XG42.commandPrefix.getValue();
    public static final char SECTION_SIGN = '\u00A7';

    public Command(String label, SyntaxChunk[] syntaxChunks, String... aliases) {
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
        this.aliases = Arrays.asList(aliases);
    }

    public static void sendChatMessage(String message) {
        sendRawChatMessage("&7[&9" + XG42.KANJI + "&7] &r" + message);
    }

    public static void sendWarningMessage(String message) {
        sendRawChatMessage("&7[&6" + XG42.KANJI + "&7] &r" + message);
    }

    public static void sendErrorMessage(String message) {
        sendRawChatMessage("&7[&4" + XG42.KANJI + "&7] &r" + message);
    }

    public static void sendCustomMessage(String message, String colour) {
        sendRawChatMessage("&7[" + colour + XG42.KANJI + "&7] &r" + message);
    }

    public static void sendStringChatMessage(String[] messages) {
        sendChatMessage("");
        for (String s : messages) sendRawChatMessage(s);
    }

    public static void sendMessage(ITextComponent iTextComponent) {
        sendRawChatMessage("&c\u300e" + XG42.KANJI + "\u300f &r" + iTextComponent);
    }

    public static void sendMessage(String s) {
        sendRawChatMessage("&c\u300e" + XG42.KANJI + "\u300f &r" + s);
    }

    public static void sendDisableMessage(String moduleName) {
        sendErrorMessage("Error: The " + moduleName + " module is only for configuring the GUI element. In order to show the GUI element you need to hit the pin in the upper left of the GUI element");
        ModuleManager.getModuleByName(moduleName).enable();
    }

    public static void sendRawChatMessage(String message) {
        if (Minecraft.getMinecraft().player != null) {
            Wrapper.getPlayer().sendMessage(new ChatMessage(message));
        } else {
            LogWrapper.info(message);
        }
    }

    public static void sendServerMessage(String message) {
        if (Minecraft.getMinecraft().player != null) {
            Wrapper.getPlayer().connection.sendPacket(new CPacketChatMessage(message));
        } else {
            LogWrapper.warning("Could not send server message: \"" + message + "\"");
        }
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String getCommandPrefix() {
        return commandPrefix;
    }

    public String getLabel() {
        return label;
    }

    public abstract void call(String[] args);

    public SyntaxChunk[] getSyntaxChunks() {
        return syntaxChunks;
    }

    public static class ChatMessage extends TextComponentBase {

        String text;

        public ChatMessage(String text) {

            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(text);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String replacement = "\u00A7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }

            m.appendTail(sb);

            this.text = sb.toString();
        }

        public String getUnformattedComponentText() {
            return text;
        }

        @Override
        public ITextComponent createCopy() {
            return new ChatMessage(text);
        }

    }

    protected SyntaxChunk getSyntaxChunk(String name) {
        for (SyntaxChunk c : syntaxChunks) {
            if (c.getType().equals(name))
                return c;
        }
        return null;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public static char SECTIONSIGN() {
        return '\u00A7';
    }
}

