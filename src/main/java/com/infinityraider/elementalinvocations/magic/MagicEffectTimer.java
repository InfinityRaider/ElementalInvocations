package com.infinityraider.elementalinvocations.magic;

import com.infinityraider.elementalinvocations.api.EnumMagicChargeAction;
import com.infinityraider.elementalinvocations.api.IChargeConfiguration;
import com.infinityraider.elementalinvocations.api.IMagicCharge;
import com.infinityraider.elementalinvocations.api.IPotencyMap;

import java.util.List;

public class MagicEffectTimer {
    public static final int DEFAULT_DURATION = 20;

    private final List<IMagicCharge> charges;
    private final IPotencyMap potencies;
    private final EnumMagicChargeAction type;

    private int timer;

    public static MagicEffectTimer Fizzle(IChargeConfiguration charges) {
        return Fizzle(charges, DEFAULT_DURATION);
    }
    public static MagicEffectTimer Fade(IChargeConfiguration charges) {
        return Fade(charges, DEFAULT_DURATION);
    }

    public static MagicEffectTimer Fizzle(IChargeConfiguration charges, int ticks) {
        return new MagicEffectTimer(charges.getCharges(), charges.getPotencyMap(), EnumMagicChargeAction.FIZZLE, ticks);
    }
    public static MagicEffectTimer Fade(IChargeConfiguration charges, int ticks) {
        return new MagicEffectTimer(charges.getCharges(), charges.getPotencyMap(), EnumMagicChargeAction.FADE, ticks);
    }

    public MagicEffectTimer(List<IMagicCharge> charges, IPotencyMap map, EnumMagicChargeAction type, int ticks) {
        this.charges = charges;
        this.potencies = map.copy();
        this.type = type;
        this.timer = Math.max(0, ticks);
    }

    public EnumMagicChargeAction getType() {
        return this.type;
    }

    public boolean decrement() {
        this.timer -= timer > 0 ? 1 : 0;
        return this.timer <= 0;
    }

}
