package com.teaminfinity.elementalinvocations.api;

import net.minecraft.util.text.TextFormatting;

public enum Element {
    FIRE("fire", (255 << 16) | (93 << 8), TextFormatting.RED),
    WATER("cold", (159 << 8) | 255, TextFormatting.BLUE, FIRE),
    AIR("lightning", (255 << 16) | (255 << 8), TextFormatting.YELLOW),
    EARTH("physical", (116 << 16) | (62 << 8), TextFormatting.GREEN, AIR),
    DEATH("dark", (80 << 16) | 114, TextFormatting.DARK_PURPLE),
    LIFE("rejuvinating", (29 << 16) | (132 << 8), TextFormatting.WHITE, DEATH);

    private String damageType;
    private TextFormatting formatting;
    private Element opposite;

    private int color;

    Element(String damageType, int color, TextFormatting formatting) {
        this.damageType = damageType;
        this.color = color;
        this.formatting = formatting;
    }

    Element(String damageType, int color, TextFormatting formatting, Element opposite) {
        this(damageType, color, formatting);
        this.opposite = opposite;
        opposite.opposite = this;
    }

    public String getDamageType() {
        return damageType;
    }

    public TextFormatting getTextFormat() {
        return formatting;
    }

    public Element getOpposite() {
        return this.opposite;
    }

    public int getColor() {
        return color;
    }

    public int getRed() {
        return (getColor() >> 16) & 255;
    }

    public int getGreen() {
        return (getColor() >> 8) & 255;
    }

    public int getBlue() {
        return getColor() & 255;
    }
}
