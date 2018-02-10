package com.infinityraider.elementalinvocations.handler;


import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.infinitylib.utility.DamageDealer;

import java.util.HashMap;
import java.util.Map;

public class DamageHandler {
    private static final DamageHandler INSTANCE = new DamageHandler();

    public static DamageHandler getInstance() {
        return INSTANCE;
    }

    private final Map<Element, DamageDealer> damageDealers;

    private DamageHandler() {
        this.damageDealers = new HashMap<>();
        this.damageDealers.put(Element.LIFE, new DamageDealer("ei_life").setMagicDamage(true));
        this.damageDealers.put(Element.AIR, new DamageDealer("ei_air").setMagicDamage(true).setBypassArmor(true));
        this.damageDealers.put(Element.FIRE, new DamageDealer("ei_fire").setMagicDamage(true).setFireDamage(true));
        this.damageDealers.put(Element.DEATH, new DamageDealer("ei_death").setMagicDamage(true).setAbsolute(true));
        this.damageDealers.put(Element.EARTH, new DamageDealer("ei_earth"));
        this.damageDealers.put(Element.WATER, new DamageDealer("ei_water").setMagicDamage(true));
    }

    public DamageDealer getDamageDealer(Element element) {
        return this.damageDealers.get(element);
    }


}