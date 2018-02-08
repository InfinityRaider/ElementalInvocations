package com.infinityraider.elementalinvocations.api;

import java.util.function.Consumer;

/**
 * Enum used as a reference for the different actions related to magic charges
 * Useful in network messages between client and server, as well as in switch/case statements
 */
public enum EnumMagicChargeAction {
    /** Represents an invoke action, invokes a spell from the currently conjured charges */
    INVOKE(IChargeConfiguration::invoke),
    /** Represents a fade action, happens when a combination of charges is too stable (has no physical effect) */
    FADE(IChargeConfiguration::fade),
    /** Represents a fizzle action, happens when a combination of charges is too unstable (backfires on the caster) */
    FIZZLE(IChargeConfiguration::fizzle);

    private final Consumer<IChargeConfiguration> action;

    EnumMagicChargeAction(Consumer<IChargeConfiguration> action) {
        this.action = action;
    }

    /**
     * Executes this action on a charge configuration
     * @param configuration the configuration to execute the action on
     */
    public void execute(IChargeConfiguration configuration) {
        this.action.accept(configuration);
    }
}