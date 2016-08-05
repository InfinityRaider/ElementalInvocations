package com.teaminfinity.elementalinvocations.magic.generic.effect;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.magic.generic.MagicEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ElementEffect implements Comparable<ElementEffect> {

    private final Element element;

    protected ElementEffect(Element element) {
        this.element = element;
    }

    public final Element element() {
        return element;
    }

    public abstract void applyEffectPre(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary);

    public abstract void applyEffectPost(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary);

    @Override
    public int compareTo(ElementEffect o) {
		return this.element.compareTo(o.element);
    }
}
