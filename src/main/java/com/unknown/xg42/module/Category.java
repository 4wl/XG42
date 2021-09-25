package com.unknown.xg42.module;

/**
 * Created by B_312 on 01/10/21
 */
public enum Category {
    //Module
    COMBAT("Combat", false),
    MISC("Misc", false),
    MOVEMENT("Movement", false),
    PLAYER("Player", false),
    RENDER("Render", false),
    CLIENT("Client", false),
    //HUD
    HUD("HUD", true),
    INFO("InfoHUD", true),
    WORLD("WorldHUD", true),
    //Hidden
    HIDDEN("Hidden", false, true);

    private final String name;
    private final boolean isHUDCategory;
    private final boolean isHidden;

    Category(String name, boolean isHUDCategory) {
        this.name = name;
        this.isHUDCategory = isHUDCategory;
        isHidden = false;
    }

    Category(String name, boolean isHUDCategory, boolean isHidden) {
        this.name = name;
        this.isHUDCategory = isHUDCategory;
        this.isHidden = isHidden;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isHUD() {
        return isHUDCategory;
    }

    public String getName() {
        return name;
    }
}