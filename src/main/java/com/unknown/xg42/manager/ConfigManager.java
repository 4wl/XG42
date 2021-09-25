package com.unknown.xg42.manager;

import com.google.gson.*;
import com.unknown.xg42.XG42;
import com.unknown.xg42.command.commands.BindCommand;
import com.unknown.xg42.gui.clickgui.GUIRender;
import com.unknown.xg42.gui.clickgui.HUDRender;
import com.unknown.xg42.gui.clickgui.Panel;
import com.unknown.xg42.module.HUDModule;
import com.unknown.xg42.module.IModule;
import com.unknown.xg42.module.ModuleManager;
import com.unknown.xg42.module.modules.client.NullHUD;
import com.unknown.xg42.module.modules.client.NullModule;
import com.unknown.xg42.setting.*;
import com.unknown.xg42.utils.other.Friend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public static ConfigManager INSTANCE;

    public ConfigManager() {
        INSTANCE = this;
        init();
    }

    private static final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();

    private static final String CONFIG_PATH = "XG42/config/";
    public static final String NOTEBOT_PATH = "XG42/notebot/";

    public static final String CLIENT_CONFIG = CONFIG_PATH + "XG42.json";
    public static final String FRIEND_CONFIG = CONFIG_PATH + "XG42-Friend.json";
    public static final String GUI_CONFIG = CONFIG_PATH + "/XG42-Gui.json";
    public static final String HUD_CONFIG = CONFIG_PATH + "modules/XG42-HUDModule.json";
    public static final String MODULE_CONFIG = CONFIG_PATH + "modules/";

    private File CLIENT_FILE;
    private File FRIEND_FILE;
    private File GUI_FILE;
    private File HUD_FILE;
    private final List<File> initializedConfig = new ArrayList<>();

    private void init() {
        if (!tryLoad()) deleteFiles();
    }

    private boolean tryLoad() {
        try {
            initializedConfig.add(CLIENT_FILE = new File(CLIENT_CONFIG));
            initializedConfig.add(FRIEND_FILE = new File(FRIEND_CONFIG));
            initializedConfig.add(GUI_FILE = new File(GUI_CONFIG));
            initializedConfig.add(HUD_FILE = new File(HUD_CONFIG));
        } catch (Exception e) {
            XG42.logger.error("Config files aren't exist or are broken!");
            return false;
        }
        return true;
    }

    public void deleteFiles() {
        try {
            initializedConfig.forEach(File::delete);
            XG42.logger.info("All config files deleted successfully!\n");
        } catch (Exception e) {
            XG42.logger.error("Error while deleting config files!");
            e.printStackTrace();
        }
    }

    public static void saveAll() {
        INSTANCE.saveClient();
        INSTANCE.saveFriend();
        //INSTANCE.saveGUI();
        INSTANCE.saveHUD();
        INSTANCE.saveModule();
    }

    public static void loadAll() {
        INSTANCE.loadClient();
        INSTANCE.loadFriend();
        //INSTANCE.loadGUI();
        INSTANCE.loadHUD();
        INSTANCE.loadModule();
    }

    private void saveModule() {
        try {
            JsonObject father = new JsonObject();
            for (IModule module : ModuleManager.getModules()) {
                File MODULE = new File(MODULE_CONFIG + "/" + module.getName() + ".json");
                if (!MODULE.exists()) {
                    MODULE.getParentFile().mkdirs();
                    try {
                        MODULE.createNewFile();
                    } catch (Exception ignored) {
                    }
                }
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enable", module.toggled);
                jsonModule.addProperty("Bind", module.getBind());
                if (!module.getSettingList().isEmpty()) {
                    for (Setting setting : module.getSettingList()) {
                        if (setting instanceof StringSetting) {
                            jsonModule.addProperty(setting.getName(), (String) setting.getValue());
                        }
                        if (setting instanceof BooleanSetting) {
                            jsonModule.addProperty(setting.getName(), (boolean) setting.getValue());
                        }
                        if (setting instanceof IntegerSetting) {
                            jsonModule.addProperty(setting.getName(), (int) setting.getValue());
                        }
                        if (setting instanceof FloatSetting) {
                            jsonModule.addProperty(setting.getName(), (float) setting.getValue());
                        }
                        if (setting instanceof DoubleSetting) {
                            jsonModule.addProperty(setting.getName(), (double) setting.getValue());
                        }
                        if (setting instanceof ModeSetting) {
                            ModeSetting modeValue = (ModeSetting) setting;
                            for (ModeSetting.Mode mode : modeValue.getModes()) {
                                jsonModule.addProperty(modeValue.getName() + "-" + mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                PrintWriter saveJSon = new PrintWriter(new FileWriter(MODULE));
                saveJSon.println(gsonPretty.toJson(jsonModule));
                saveJSon.close();
                module.onConfigSave();
                father.add(module.getName(), jsonModule);
            }
        } catch (Exception e) {
            XG42.logger.error("Error while saving module config!");
            e.printStackTrace();
        }
    }

    private void saveHUD() {
        try {
            if (!HUD_FILE.exists()) {
                HUD_FILE.getParentFile().mkdirs();
                try {
                    HUD_FILE.createNewFile();
                } catch (Exception ignored) {
                }
            }
            JsonObject father = new JsonObject();
            for (IModule module : ModuleManager.getHUDModules()) {
                JsonObject jsonModule = new JsonObject();
                jsonModule.addProperty("Enable", module.toggled);
                jsonModule.addProperty("HUDPosX", module.x);
                jsonModule.addProperty("HUDPosY", module.y);
                jsonModule.addProperty("Bind", module.getBind());
                if (!module.getSettingList().isEmpty()) {
                    for (Setting setting : module.getSettingList()) {
                        if (setting instanceof StringSetting) {
                            jsonModule.addProperty(setting.getName(), (String) setting.getValue());
                        }
                        if (setting instanceof BooleanSetting) {
                            jsonModule.addProperty(setting.getName(), (boolean) setting.getValue());
                        }
                        if (setting instanceof IntegerSetting) {
                            jsonModule.addProperty(setting.getName(), (int) setting.getValue());
                        }
                        if (setting instanceof FloatSetting) {
                            jsonModule.addProperty(setting.getName(), (float) setting.getValue());
                        }
                        if (setting instanceof DoubleSetting) {
                            jsonModule.addProperty(setting.getName(), (double) setting.getValue());
                        }
                        if (setting instanceof ModeSetting) {
                            ModeSetting modeValue = (ModeSetting) setting;
                            for (ModeSetting.Mode mode : modeValue.getModes()) {
                                jsonModule.addProperty(modeValue.getName() + "-" + mode.getName(), mode.isToggled());
                            }
                        }
                    }
                }
                module.onConfigSave();
                father.add(module.getName(), jsonModule);
            }
            PrintWriter saveJSon = new PrintWriter(new FileWriter(HUD_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e) {
            XG42.logger.error("Error while saving HUD config!");
            e.printStackTrace();
        }
    }

    private void saveGUI(){
        try{
            if(!GUI_FILE.exists()){
                GUI_FILE.getParentFile().mkdirs();
                try{
                    GUI_FILE.createNewFile();
                } catch (Exception ignored){}
            }
            JsonObject father = new JsonObject();

            //Click GUI
            for (Panel panel : GUIRender.getINSTANCE().panels){
                JsonObject jsonGui = new JsonObject();
                jsonGui.addProperty("X", panel.x);
                jsonGui.addProperty("Y", panel.y);
                jsonGui.addProperty("Extended", panel.extended);
                father.add(panel.category.getName(), jsonGui);
            }

            //HUD Editor
            for (Panel panel : HUDRender.getINSTANCE().panels){
                JsonObject jsonGui = new JsonObject();
                jsonGui.addProperty("X", panel.x);
                jsonGui.addProperty("Y", panel.y);
                jsonGui.addProperty("Extended", panel.extended);
                father.add(panel.category.getName(), jsonGui);
            }

            PrintWriter saveJSon = new PrintWriter(new FileWriter(GUI_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e){
            XG42.logger.error("Error while saving GUI config!");
            e.printStackTrace();
        }
    }

    private void saveClient() {
        try {
            if (!CLIENT_FILE.exists()) {
                CLIENT_FILE.getParentFile().mkdirs();
                try {
                    CLIENT_FILE.createNewFile();
                } catch (Exception ignored) {
                }
            }

            JsonObject father = new JsonObject();
            JsonObject stuff = new JsonObject();

            stuff.addProperty("AutoEz", 1);
            stuff.addProperty("CommandPrefix", XG42.commandPrefix.getValue());
            stuff.addProperty("ChatSuffix", 1);
            stuff.addProperty("FakePlayerName", 1);
            stuff.addProperty("WaterMark", 1);
            stuff.addProperty(BindCommand.modifiersEnabled.getName(), BindCommand.modifiersEnabled.getValue());

            father.add("Client", stuff);

            PrintWriter saveJson = new PrintWriter(new FileWriter(CLIENT_CONFIG));
            saveJson.println(gsonPretty.toJson(father));
            saveJson.close();
        } catch (Exception e) {
            XG42.logger.error("Error while saving Client stuff!");
            e.printStackTrace();
        }
    }

    private void saveFriend() {
        try {
            if (!FRIEND_FILE.exists()) {
                FRIEND_FILE.getParentFile().mkdirs();
                try {
                    FRIEND_FILE.createNewFile();
                } catch (Exception ignored) {
                }
            }

            JsonObject father = new JsonObject();

            for (Friend friend : FriendManager.getFriendList()) {
                JsonObject stuff = new JsonObject();
                stuff.addProperty("isFriend", friend.isFriend);
                father.add(friend.name, stuff);
            }

            PrintWriter saveJSon = new PrintWriter(new FileWriter(FRIEND_CONFIG));
            saveJSon.println(gsonPretty.toJson(father));
            saveJSon.close();
        } catch (Exception e) {
            XG42.logger.error("Error while saving friends!");
            e.printStackTrace();
        }
    }

    private void trySetClient(JsonObject json) {
        try {
            XG42.commandPrefix.setValue(json.get("CommandPrefix").getAsString());
            BindCommand.modifiersEnabled.setValue(json.get(BindCommand.modifiersEnabled.getName()).getAsBoolean());
            //HUD.waterMark = json.get("WaterMark").getAsString();
            //FakePlayer.customName = json.get("FakePlayerName").getAsString();
            //CustomChat.CHAT_SUFFIX = json.get("ChatSuffix").getAsString();
            //AutoEz.ezMsg = json.get("AutoEz").getAsString();

        } catch (Exception e) {
            XG42.logger.error("Error while setting Client!");
        }
    }

    private void loadClient() {
        if (CLIENT_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(CLIENT_FILE));
                JsonObject guiJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJason.entrySet()) {
                    if (entry.getKey().equals("Client")) {
                        JsonObject json = (JsonObject) entry.getValue();
                        trySetClient(json);
                    }
                }
            } catch (IOException e) {
                XG42.logger.error("Error while loading Client stuff!");
                e.printStackTrace();
            }
        }
    }

    private void loadFriend() {
        if (FRIEND_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(FRIEND_FILE));
                JsonObject friendJson = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                FriendManager.INSTANCE.friends.clear();
                for (Map.Entry<String, JsonElement> entry : friendJson.entrySet()) {
                    if (entry.getKey() == null) continue;
                    JsonObject json = (JsonObject) entry.getValue();
                    String name = entry.getKey();
                    boolean isFriend = false;
                    try {
                        isFriend = json.get("isFriend").getAsBoolean();
                    } catch (Exception e) {
                        XG42.logger.error("Can't set friend value for " + name + ", unfriended!");
                    }
                    FriendManager.INSTANCE.friends.add(new Friend(name, isFriend));
                }
            } catch (IOException e) {
                XG42.logger.error("Error while loading friends!");
                e.printStackTrace();
            }
        }
    }

    private void loadGUI(){
        if (GUI_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(GUI_FILE));
                JsonObject guiJson = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : guiJson.entrySet()) {
                    Panel panel = GUIRender.getPanelByName(entry.getKey());
                    if(panel == null) panel = HUDRender.getPanelByName(entry.getKey());
                    if (panel != null) {
                        JsonObject jsonGui = (JsonObject) entry.getValue();
                        panel.x = jsonGui.get("X").getAsInt();
                        panel.y = jsonGui.get("Y").getAsInt();
                        panel.extended = jsonGui.get("Extended").getAsBoolean();
                    }
                }
            } catch (IOException e) {
                XG42.logger.error("Error while loading GUI config!");
                e.printStackTrace();
            }
        }
    }

    private void loadModule() {
        try {
            for (IModule module : ModuleManager.getModules()) {
                File modulefile = new File(MODULE_CONFIG + "/" + module.getName() + ".json");
                if (modulefile.exists()) {
                    BufferedReader loadJson = new BufferedReader(new FileReader(modulefile));
                    JsonObject moduleJason = (JsonObject) jsonParser.parse(loadJson);
                    loadJson.close();
                    for (Map.Entry<String, JsonElement> ignored : moduleJason.entrySet()) {
                        IModule modul = ModuleManager.getModuleByName(module.getName());
                        if (!(modul instanceof NullModule)) {
                            boolean enabled = moduleJason.get("Enable").getAsBoolean();
                            if (modul.isEnabled() && !enabled) modul.disable();
                            if (modul.isDisabled() && enabled) modul.enable();
                            if (!modul.getSettingList().isEmpty()) {
                                trySet(modul, moduleJason);
                            }
                            modul.onConfigLoad();
                            modul.setBind(moduleJason.get("Bind").getAsInt());
                        }
                    }
                }
            }
        } catch (IOException e) {
            XG42.logger.info("Error while loading module config");
            e.printStackTrace();
        }

    }

    private void loadHUD() {
        if (HUD_FILE.exists()) {
            try {
                BufferedReader loadJson = new BufferedReader(new FileReader(HUD_FILE));
                JsonObject moduleJason = (JsonObject) jsonParser.parse(loadJson);
                loadJson.close();
                for (Map.Entry<String, JsonElement> entry : moduleJason.entrySet()) {
                    HUDModule module = ModuleManager.getHUDByName(entry.getKey());
                    if (!(module instanceof NullHUD)) {
                        JsonObject jsonMod = (JsonObject) entry.getValue();
                        boolean enabled = jsonMod.get("Enable").getAsBoolean();
                        if (module.isEnabled() && !enabled) module.disable();
                        if (module.isDisabled() && enabled) module.enable();
                        module.x = jsonMod.get("HUDPosX").getAsInt();
                        module.y = jsonMod.get("HUDPosY").getAsInt();
                        if (!module.getSettingList().isEmpty()) {
                            trySet(module, jsonMod);
                        }
                        module.onConfigLoad();
                        module.setBind(jsonMod.get("Bind").getAsInt());
                    }
                }
            } catch (IOException e) {
                XG42.logger.info("Error while loading module config");
                e.printStackTrace();
            }
        }
    }

    private void trySet(IModule mods, JsonObject jsonMod) {
        try {
            for (Setting<?> value : mods.getSettingList()) {
                tryValue(mods.name, value, jsonMod);
            }
        } catch (Exception e) {
            XG42.logger.error("Cant set value for " + (mods.isHUD ? "HUD " : " module ") + mods.getName() + "!");
        }
    }

    private void tryValue(String name, Setting<?> setting, JsonObject jsonMod) {
        try {
            if (setting instanceof StringSetting) {
                String sValue = jsonMod.get(setting.getName()).getAsString();
                ((StringSetting) setting).setValue(sValue);
            }
            if (setting instanceof BooleanSetting) {
                boolean bValue = jsonMod.get(setting.getName()).getAsBoolean();
                ((BooleanSetting) setting).setValue(bValue);
            }
            if (setting instanceof DoubleSetting) {
                double dValue = jsonMod.get(setting.getName()).getAsDouble();
                ((DoubleSetting) setting).setValue(dValue);
            }
            if (setting instanceof IntegerSetting) {
                int iValue = jsonMod.get(setting.getName()).getAsInt();
                ((IntegerSetting) setting).setValue(iValue);
            }
            if (setting instanceof FloatSetting) {
                float fValue = jsonMod.get(setting.getName()).getAsFloat();
                ((FloatSetting) setting).setValue(fValue);
            }
            if (setting instanceof ModeSetting) {
                ModeSetting modeValue = (ModeSetting) setting;
                for (ModeSetting.Mode mode : modeValue.getModes()) {
                    boolean mValue = jsonMod.get(modeValue.getName() + "-" + mode.getName()).getAsBoolean();
                    mode.setToggled(mValue);
                }
            }
        } catch (Exception e) {
            XG42.logger.error("Cant set value for " + name + ",loaded default!Value name: " + setting.getName());
        }
    }
}
