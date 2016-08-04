package com.teaminfinity.elementalinvocations.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityMagicProjectile extends EntityThrowable {
    public EntityMagicProjectile(World world) {
        super(world);
    }

    public EntityMagicProjectile(World world, EntityPlayer caster) {
        super(world, caster);
    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }
}
