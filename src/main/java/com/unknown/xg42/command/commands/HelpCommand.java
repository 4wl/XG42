package com.unknown.xg42.command.commands;

import com.unknown.xg42.command.Command;
import com.unknown.xg42.command.syntax.SyntaxChunk;
import com.unknown.xg42.module.ModuleManager;

import java.util.Arrays;

public class HelpCommand extends Command {
   private static final Subject[] subjects = new Subject[]{new Subject(new String[]{"type", "int", "boolean", "double", "float"}, new String[]{"Every module has a value, and that value is always of a certain &btype.\n", "These types are displayed in kami as the ones java use. They mean the following:", "&bboolean&r: Enabled or not. Values &3true/false", "&bfloat&r: A number with a decimal point", "&bdouble&r: Like a float, but a more accurate decimal point", "&bint&r: A number with no decimal point"})};
   private static String subjectsList = "";

   public HelpCommand() {
      super("help", new SyntaxChunk[0]);
      this.setDescription("Delivers help on certain subjects. Use &b-help subjects&r for a list.");
   }

   public void call(String[] args) {
      if (args[0] == null) {
         Command.sendStringChatMessage(new String[]{"MelonHack+ ", "commands&7 to view all available commands", "bind <module> <key>&7 to bind mods", "&7Press &r" + ModuleManager.getModuleByName("ClickGUI").getBind() + "&7 to open GUI", "prefix <prefix>&r to change the command prefix.", "help <subjects:[subject]> &r for more help."});
      } else {
         String subject = args[0];
         if (subject.equals("subjects")) {
            Command.sendChatMessage("Subjects: " + subjectsList);
         } else {
            Subject subject1 = (Subject)Arrays.stream(subjects).filter((subject2) -> {
               String[] var2 = subject2.names;
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  String name = var2[var4];
                  if (name.equalsIgnoreCase(subject)) {
                     return true;
                  }
               }

               return false;
            }).findFirst().orElse(null);
            if (subject1 == null) {
               Command.sendChatMessage("No help found for &b" + args[0]);
               return;
            }

            Command.sendStringChatMessage(subject1.info);
         }
      }

   }

   static {
      Subject[] var0 = subjects;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Subject subject = var0[var2];
         subjectsList = subjectsList + subject.names[0] + ", ";
      }

      subjectsList = subjectsList.substring(0, subjectsList.length() - 2);
   }

   private static class Subject {
      String[] names;
      String[] info;

      public Subject(String[] names, String[] info) {
         this.names = names;
         this.info = info;
      }
   }
}
