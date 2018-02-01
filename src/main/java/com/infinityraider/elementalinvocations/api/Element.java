package com.infinityraider.elementalinvocations.api;

import javafx.util.Pair;
import net.minecraft.util.text.TextFormatting;

public enum Element {
    LIFE("rejuvinating", (29 << 16) | (132 << 8), 30, TextFormatting.WHITE),
    AIR("lightning", (255 << 16) | (255 << 8), 90, TextFormatting.YELLOW),
    FIRE("fire", (255 << 16) | (93 << 8), 150, TextFormatting.RED),
    DEATH("dark", (80 << 16) | 114, 210, TextFormatting.DARK_PURPLE, LIFE),
    EARTH("physical", (116 << 16) | (62 << 8), 270, TextFormatting.GREEN, AIR),
    WATER("cold", (159 << 8) | 255, 330, TextFormatting.BLUE, FIRE);

    private String damageType;
    private double polarAngle;
    private double cosAngle;
    private double sinAngle;
    private Element opposite;
    private TextFormatting formatting;

    private int color;

    Element(String damageType, int color, int angle, TextFormatting formatting) {
        this.damageType = damageType;
        this.polarAngle = Math.PI*angle/180;
        this.cosAngle = Math.cos(this.polarAngle);
        this.sinAngle = Math.sin(this.polarAngle);
        this.color = color;
        this.formatting = formatting;
    }

    Element(String damageType, int color, int angle, TextFormatting formatting, Element opposite) {
        this(damageType, color, angle, formatting);
        this.opposite = opposite;
        opposite.opposite = this;
    }

    public double getPolarAngle() {
        return this.polarAngle;
    }

    public double getCos() {
        return this.cosAngle;
    }

    public double getSin() {
        return this.sinAngle;
    }

    public double calculateX(double radius) {
        return radius * this.getCos();
    }

    public double calculateY(double radius) {
        return radius * this.getSin();
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

    public static Pair<Element, Element> getElementsForAngle(double angle) {
        int max = Element.values().length - 1;
        for(Element element : Element.values()) {
            if(angle <= element.getPolarAngle()) {
                int index = element.ordinal() - 1;
                index = index < 0 ? max : index;
                return new Pair<>(element, Element.values()[index]);
            }
        }
        return new Pair<>(Element.values()[0], Element.values()[max]);
    }
}
