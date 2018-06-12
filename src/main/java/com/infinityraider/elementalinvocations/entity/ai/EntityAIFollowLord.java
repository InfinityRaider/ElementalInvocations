package com.infinityraider.elementalinvocations.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowLord  extends EntityAIBase {
    private final EntityCreature entity;
    private EntityPlayer boss;
    World theWorld;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;

    public EntityAIFollowLord(EntityPlayer boss, EntityCreature entity, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.boss = boss;
        this.entity = entity;
        this.theWorld = entity.getEntityWorld();
        this.followSpeed = followSpeedIn;
        this.petPathfinder = entity.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);
        if (!(entity.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (boss == null) {
            return false;
        } else if (boss.isSpectator()) {
            return false;
        }
        else if (this.entity.getDistanceSq(boss) < (double)(this.minDist * this.minDist)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return !this.petPathfinder.noPath() && this.entity.getDistanceSq(this.boss) > (double)(this.maxDist * this.maxDist);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.boss = null;
        this.petPathfinder.clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    private boolean isEmptyBlock(BlockPos pos) {
        IBlockState iblockstate = this.theWorld.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR || !iblockstate.isFullCube();
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        this.entity.getLookHelper().setLookPositionWithEntity(this.boss, 10.0F, (float) this.entity.getVerticalFaceSpeed());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.boss, this.followSpeed)) {
                if (!this.entity.getLeashed()) {
                    if (this.entity.getDistanceSq(this.boss) >= 144.0D) {
                        int i = MathHelper.floor(this.boss.posX) - 2;
                        int j = MathHelper.floor(this.boss.posZ) - 2;
                        int k = MathHelper.floor(this.boss.getEntityBoundingBox().minY);
                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isOpaqueCube() && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1))) {
                                    this.entity.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.entity.rotationYaw, this.entity.rotationPitch);
                                    this.petPathfinder.clearPath();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
