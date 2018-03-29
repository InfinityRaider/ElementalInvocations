package com.infinityraider.elementalinvocations.magic.thaum;

import com.infinityraider.elementalinvocations.api.Element;
import thaumcraft.api.aspects.Aspect;

import java.util.Arrays;
import java.util.HashMap;

public final class ElementToAspect {
    private ElementToAspect() {}

    private static final HashMap<Aspect, Element> ASPECT_TO_ELEMENT = new HashMap<>();

    public static Aspect getAspect(Element element) {
        switch(element) {
            case LIFE:
                return Aspect.ORDER;
            case AIR:
                return Aspect.AIR;
            case FIRE:
                return Aspect.FIRE;
            case DEATH:
                return Aspect.ENTROPY;
            case EARTH:
                return Aspect.EARTH;
            case WATER:
                return Aspect.WATER;
            default:
                return null;
        }
    }

    public static Element getElement(Aspect aspect) {
        return ASPECT_TO_ELEMENT.get(aspect);
    }

    static {
        Arrays.stream(Element.values()).forEach(e -> ASPECT_TO_ELEMENT.put(getAspect(e), e));
    }

}