package com.infinityraider.elementalinvocations.magic.thaum;

import com.infinityraider.elementalinvocations.config.ModConfiguration;
import net.minecraftforge.fml.common.Loader;

public abstract class ThaumInitializer {
    private static final ThaumInitializer IMPL = new Impl();
    private static final ThaumInitializer DUMMY = new Dummy();

    public static ThaumInitializer getInstance() {
        if (ModConfiguration.getInstance().useThaumcraft() && Loader.isModLoaded("thaumcraft")) {
            return IMPL;
        } else {
            return DUMMY;
        }
    }

    public abstract void init();

    private static final class Impl extends ThaumInitializer {
        @Override
        public void init() {
            FocusElementalCharge.init();
        }
    }

    private static final class Dummy extends ThaumInitializer {
        @Override
        public void init() {}
    }

}