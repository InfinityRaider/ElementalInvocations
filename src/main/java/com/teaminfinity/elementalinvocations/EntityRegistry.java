package com.teaminfinity.elementalinvocations;

import com.teaminfinity.elementalinvocations.entity.EntityMagicProjectile;
import com.teaminfinity.elementalinvocations.entity.EntityRegistryEntry;

public class EntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    private EntityRegistry() {
        entityMagicProjectile = new EntityRegistryEntry<>(EntityMagicProjectile.class, "entity.magic_missile")
                .setTrackingDistance(32)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityMagicProjectile.RenderFactory.getInstance());
    }

    public EntityRegistryEntry<EntityMagicProjectile> entityMagicProjectile;
}