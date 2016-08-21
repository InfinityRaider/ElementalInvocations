package com.teaminfinity.elementalinvocations.registry;

public class BlockRegistry {
    private static final BlockRegistry INSTANCE = new BlockRegistry();

    public static BlockRegistry getInstance() {
        return INSTANCE;
    }

    private BlockRegistry() {

    }
}
