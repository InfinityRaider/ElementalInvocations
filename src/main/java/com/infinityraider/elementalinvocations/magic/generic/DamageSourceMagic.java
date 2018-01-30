package com.infinityraider.elementalinvocations.magic.generic;

import com.infinityraider.elementalinvocations.api.Element;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceMagic extends EntityDamageSource {

    public DamageSourceMagic(Element element, EntityPlayer caster) {
        super(element.getDamageType(), caster);
        this.setElementalEffect(element);
    }

    private void setElementalEffect(Element element) {
        this.setDamageIsAbsolute();
        this.setDamageBypassesArmor();
        this.setMagicDamage();
        if(element == Element.FIRE) {
            this.setFireDamage();
        }
    }
}
