package com.teaminfinity.elementalinvocations.utility;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class TargetHelper {
    @Nullable
    public static RayTraceResult getTarget(Entity entity, int distance) {
        Vec3d eyes = new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
        Vec3d look = entity.getLookVec();
        if(look == null) {
            RayTraceResult result = new RayTraceResult(eyes, EnumFacing.UP, entity.getPosition());
            result.entityHit = entity;
            return result;
        }
        Vec3d trace = eyes.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        return entity.getEntityWorld().rayTraceBlocks(eyes, trace, false, false, true);
    }
}
