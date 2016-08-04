package com.teaminfinity.elementalinvocations.api;

public enum Element {
    FIRE,
    WATER(FIRE),
    AIR,
    EARTH(AIR),
    DEATH,
    LIFE(DEATH);

    private Element opposite;

    Element() {}

    Element(Element opposite) {
        this.opposite = opposite;
        opposite.opposite = this;
    }

    public Element getOpposite() {
        return this.opposite;
    }
}
