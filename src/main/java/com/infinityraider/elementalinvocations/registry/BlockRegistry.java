package com.infinityraider.elementalinvocations.registry;

import com.infinityraider.elementalinvocations.block.*;
import net.minecraft.block.Block;

public class BlockRegistry {
    private static final BlockRegistry INSTANCE = new BlockRegistry();

    public static BlockRegistry getInstance() {
        return INSTANCE;
    }

    public final Block blockEarthQuake;
    public final Block blockImpaleSpike;
    public final Block blockSolidAir;

    private BlockRegistry() {
        this.blockEarthQuake = new BlockEarthquake();
        this.blockImpaleSpike = new BlockImpaleSpike();
        this.blockSolidAir = new BlockSolidAir();
    }
}
