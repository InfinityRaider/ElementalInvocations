package com.teaminfinity.elementalinvocations.api;

import net.minecraft.util.text.TextFormatting;

public enum Element {
    FIRE("fire", TextFormatting.RED),
    WATER("cold", TextFormatting.BLUE, FIRE),
    AIR("lightning", TextFormatting.YELLOW),
    EARTH("physical", TextFormatting.GREEN, AIR),
    DEATH("dark", TextFormatting.DARK_PURPLE),
    LIFE("rejuvinating", TextFormatting.WHITE, DEATH);

    private String damageType;
    private TextFormatting formatting;
    private Element opposite;

    Element(String damageType, TextFormatting formatting) {
        this.formatting = formatting;
    }

    Element(String damageType, TextFormatting formatting, Element opposite) {
        this(damageType, formatting);
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
}
