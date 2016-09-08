package com.teaminfinity.elementalinvocations.registry;

import com.infinityraider.infinitylib.entity.EntityRegistryEntry;
import com.teaminfinity.elementalinvocations.entity.*;
import net.minecraft.entity.monster.EntityMob;

public class EntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private EntityRegistry() {
        entityMagicProjectile = new EntityRegistryEntry<>(EntityMagicProjectile.class, "entity.magic_missile")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityMagicProjectile.RenderFactory.getInstance());

        entityBallLightning = new EntityRegistryEntry<>(EntityBallLightning.class, "entity.ball_lightning")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityBallLightning.RenderFactory.getInstance());

        entitySunstrike = new EntityRegistryEntry<>(EntitySunstrike.class, "entity.sunstrike")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(false)
                .setRenderFactory(EntitySunstrike.RenderFactory.getInstance());

        entityWaveForm = new EntityRegistryEntry<>(EntityWaveForm.class, "entity.wave_form")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityWaveForm.RenderFactory.getInstance());

        entityMeteor = new EntityRegistryEntry<>(EntityMeteor.class, "entity.meteor")
                .setTrackingDistance(128)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityMeteor.RenderFactory.getInstance());

        entityVacuum = new EntityRegistryEntry<>(EntityVacuum.class, "entity.vacuum")
                .setTrackingDistance(32)
                .setUpdateFrequency(1)
                .setVelocityUpdates(false)
                .setRenderFactory(EntityVacuum.RenderFactory.getInstance());

        entityReplicate = new EntityRegistryEntry<>(EntityReplicate.class, "entity.replicate")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityReplicate.RenderFactory.getInstance())
                .setEntityTargetedBy(EntityMob.class);

        entityMagnetizedRock = new EntityRegistryEntry<>(EntityMagnetizedRock.class, "entity.magnetized_rock")
                .setTrackingDistance(32)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityMagnetizedRock.RenderFactory.getInstance())
                .registerDataSerializers(EntityMagnetizedRock.EnumStage.dataSerializer);

    }

    public final EntityRegistryEntry<EntityMagicProjectile> entityMagicProjectile;
    public final EntityRegistryEntry<EntityBallLightning> entityBallLightning;
    public final EntityRegistryEntry<EntitySunstrike> entitySunstrike;
    public final EntityRegistryEntry<EntityWaveForm> entityWaveForm;
    public final EntityRegistryEntry<EntityMeteor> entityMeteor;
    public final EntityRegistryEntry<EntityVacuum> entityVacuum;
    public final EntityRegistryEntry<EntityReplicate> entityReplicate;
    public final EntityRegistryEntry<EntityMagnetizedRock> entityMagnetizedRock;
}
