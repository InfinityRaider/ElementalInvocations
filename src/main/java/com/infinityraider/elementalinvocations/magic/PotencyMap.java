package com.infinityraider.elementalinvocations.magic;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;

import java.util.HashMap;
import java.util.Map;

public class PotencyMap implements IPotencyMap {
    /** Map with potencies */
    private final Map<Element, Integer> potencies;
    /** Total count */
    private int count;
    /** Color values */
    private float red;
    private float green;
    private float blue;

    public PotencyMap() {
        this.potencies = new HashMap<>();
        for(Element element : Element.values()) {
            potencies.put(element, 0);
        }
        this.count = 0;
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }

    protected void addPotency(Element element, int potency) {
        this.potencies.put(element, this.potencies.get(element) + potency);
        int n = this.count + potency;
        this.red = (this.red*255*this.count + element.getRed()*potency)/(255*n);
        this.green = (this.green*255*this.count + element.getGreen()*potency)/(255*n);
        this.blue = (this.blue*255*this.count + element.getBlue()*potency)/(255*n);
        this.count = n;
    }

    @Override
    public int getTotalPotency() {
        return this.count;
    }

    @Override
    public int getPotency(Element element) {
        return this.potencies.get(element);
    }

    @Override
    public float getRed() {
        return this.count == 0 ? 1.0F : this.red;
    }

    @Override
    public float getBlue() {
        return this.count == 0 ? 1.0F : this.blue;
    }

    @Override
    public float getGreen() {
        return this.count == 0 ? 1.0F : this.green;
    }
}
