package com.teaminfinity.elementalinvocations.utility;

import com.teaminfinity.elementalinvocations.api.Element;

public final class ColorHelper {
    public static int[] compileColors(int[] potencies) {
        int total = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        for(Element element : Element.values()) {
            total = total + potencies[element.ordinal()];
            red = red + element.getRed()*potencies[element.ordinal()];
            green = green + element.getGreen()*potencies[element.ordinal()];
            blue = blue + element.getBlue()*potencies[element.ordinal()];
        }
        return new int[] {red / total, green / total, blue / total};
    }
}
