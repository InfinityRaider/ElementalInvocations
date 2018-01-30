package com.infinityraider.elementalinvocations.api;

import net.minecraft.util.text.TextFormatting;

public enum Element {
    FIRE("fire", (255 << 16) | (93 << 8), 150, TextFormatting.RED),
    WATER("cold", (159 << 8) | 255, 330, TextFormatting.BLUE, FIRE),
    AIR("lightning", (255 << 16) | (255 << 8), 90, TextFormatting.YELLOW),
    EARTH("physical", (116 << 16) | (62 << 8), 270, TextFormatting.GREEN, AIR),
    DEATH("dark", (80 << 16) | 114, 210, TextFormatting.DARK_PURPLE),
    LIFE("rejuvinating", (29 << 16) | (132 << 8), 30, TextFormatting.WHITE, DEATH);

    private String damageType;
    private int polarAngle;
    private Element opposite;
    private TextFormatting formatting;

    private int color;

    Element(String damageType, int color, int angle, TextFormatting formatting) {
        this.damageType = damageType;
        this.polarAngle = angle;
        this.color = color;
        this.formatting = formatting;
    }

    Element(String damageType, int color, int angle, TextFormatting formatting, Element opposite) {
        this(damageType, color, angle, formatting);
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
