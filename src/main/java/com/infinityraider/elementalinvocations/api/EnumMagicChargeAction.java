package com.infinityraider.elementalinvocations.api;

import java.util.function.Consumer;

public enum EnumMagicChargeAction {
    INVOKE(IChargeConfiguration::invoke),
    FADE(IChargeConfiguration::fade),
    FIZZLE(IChargeConfiguration::fizzle);

    private final Consumer<IChargeConfiguration> action;

    EnumMagicChargeAction(Consumer<IChargeConfiguration> action) {
        this.action = action;
    }

    public void execute(IChargeConfiguration configuration) {
        this.action.accept(configuration);
    }
}