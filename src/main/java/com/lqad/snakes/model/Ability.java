package com.lqad.snakes.model;

public enum Ability {  //enum because we have known specific abilities and not random 100 other ability
    DOUBLE_MOVE("Double Speed", "x2"),
    BLOCK_LADDER("Ladder Trap", "🚫");

    private final String name;
    private final String icon;

    Ability(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() { return name; }
    public String getIcon() { return icon; }
}