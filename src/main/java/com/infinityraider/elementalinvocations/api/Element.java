package com.infinityraider.elementalinvocations.api;

import javafx.util.Pair;
import net.minecraft.util.text.TextFormatting;

/**
 * Enum representing the different elements used in ElementalInvocations
 */
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

    /**
     * @return the polar angle in radians of this element in the polar stability diagram
     */
    public double getPolarAngle() {
        return this.polarAngle;
    }

    /**
     * Cosine of the polar angle, pre-calculated to reduce overhead
     * @return cosine of the polar angle
     */
    public double getCos() {
        return this.cosAngle;
    }

    /**
     * Sine of the polar angle, pre-calculated to reduce overhead
     * @return cosine of the polar angle
     */
    public double getSin() {
        return this.sinAngle;
    }

    /**
     * Calculates the x-coordinate of a point along this element in the stability diagram
     * @param radius the distance along the line representing this element
     * @return the x-coordinate
     */
    public double calculateX(double radius) {
        return radius * this.getCos();
    }

    /**
     * Calculates the y-coordinate of a point along this element in the stability diagram
     * @param radius the distance along the line representing this element
     * @return the y-coordinate
     */
    public double calculateY(double radius) {
        return radius * this.getSin();
    }

    /**
     * @return a String representing the damage type of this element
     */
    public String getDamageType() {
        return damageType;
    }

    /**
     * @return the TextFormatting used when using this element in chat
     */
    public TextFormatting getTextFormat() {
        return formatting;
    }

    /**
     * @return the opposite Element to this one in the polar stability diagram
     */
    public Element getOpposite() {
        return this.opposite;
    }

    /**
     * @return the RGB value of this element
     */
    public int getColor() {
        return color;
    }

    /**
     * @return the RGB-red value of this element
     */
    public int getRed() {
        return (getColor() >> 16) & 255;
    }

    /**
     * @return the RGB-green value of this element
     */
    public int getGreen() {
        return (getColor() >> 8) & 255;
    }

    /**
     * @return the RGB-blue value of this element
     */
    public int getBlue() {
        return getColor() & 255;
    }

    /**
     * Gets the two elements neighbouring a certain angle in the polar stability diagram,
     * will always contain two different elements.
     * @param angle the angle
     * @return a Pair containing the two neighbouring elements
     */
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
