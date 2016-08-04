package com.teaminfinity.elementalinvocations.api;

import net.minecraft.util.text.TextFormatting;

public enum Element {
    FIRE(TextFormatting.RED),
    WATER(TextFormatting.BLUE, FIRE),
    AIR(TextFormatting.YELLOW),
    EARTH(TextFormatting.GREEN, AIR),
    DEATH(TextFormatting.DARK_PURPLE),
    LIFE(TextFormatting.WHITE, DEATH);

    private TextFormatting formatting;
    private Element opposite;

    Element(TextFormatting formatting) {
        this.formatting = formatting;
    }

    Element(TextFormatting formatting, Element opposite) {
        this(formatting);
        this.opposite = opposite;
        opposite.opposite = this;
    }

    public TextFormatting getTextFormat() {
        return formatting;
    }

    public Element getOpposite() {
        return this.opposite;
    }
}
