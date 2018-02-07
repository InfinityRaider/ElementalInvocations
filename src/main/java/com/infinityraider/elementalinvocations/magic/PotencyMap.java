package com.infinityraider.elementalinvocations.magic;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import net.minecraft.nbt.NBTTagCompound;

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

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        for(Element element : Element.values()) {
            tag.setInteger(element.name(), this.getPotency(element));
        }
        return tag;
    }

    @Override
    public IPotencyMap readFromNBT(NBTTagCompound tag) {
        int n = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        for(Element element : Element.values()) {
            int potency = tag.hasKey(element.name()) ? tag.getInteger(element.name()) : 0;
            this.potencies.put(element, potency);
            n = n + potency;
            r = r + potency*element.getRed();
            g = g + potency*element.getGreen();
            b = b + potency*element.getBlue();
        }
        this.count = n;
        this.red = ((float) r)/(255.0F*n);
        this.green = ((float) g)/(255.0F*n);
        this.blue = ((float) b)/(255.0F*n);
        return this;
    }
}
