package com.infinityraider.elementalinvocations.magic.spell.earth;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.block.tile.TileSolidAir;
import com.infinityraider.elementalinvocations.registry.BlockRegistry;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EffectSolidAir implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        int potencyAir = potencies.getPotency(Element.AIR);
        int potencyEarth = potencies.getPotency(Element.EARTH);
        World world = caster.getEntityWorld();
        BlockPos pos = getStartPos(caster, 3);
        EnumFacing dir = caster.getHorizontalFacing();
        Vec3d look = caster.getLookVec();
        boolean horizontal = Math.abs(look.yCoord) > Math.max(Math.abs(look.xCoord), Math.abs(look.zCoord));
        int time = potencyEarth*20;
        this.placeBarrier(world, pos, caster, time);
        for(int i = 1; i <= potencyAir/2; i++) {
            if(!horizontal) {
                this.placeBarrier(world, pos.down(i), caster, time);
                this.placeBarrier(world, pos.up(i), caster, time);
            }
            if(horizontal || dir.getAxis() == EnumFacing.Axis.X) {
                this.placeBarrier(world, pos.south(i), caster, time);
                this.placeBarrier(world, pos.north(i), caster, time);
            }
            if(horizontal || dir.getAxis() == EnumFacing.Axis.Z) {
                this.placeBarrier(world, pos.east(i), caster, time);
                this.placeBarrier(world, pos.west(i), caster, time);
            }
        }
        return false;
    }

    private BlockPos getStartPos(Entity caster, int distance) {
        RayTraceResult result = RayTraceHelper.getTargetBlock(caster, distance);
        if(result.getBlockPos() != null) {
            if(result.sideHit != null) {
                return result.getBlockPos().offset(result.sideHit);
            }
            return result.getBlockPos();
        }
        Vec3d eyes = new Vec3d(caster.posX, caster.posY + (double)caster.getEyeHeight(), caster.posZ);
        Vec3d look = caster.getLookVec();
        return new BlockPos(eyes.xCoord + distance*look.xCoord, eyes.yCoord + distance*look.yCoord, eyes.zCoord + distance*look.zCoord);
    }

    private void placeBarrier(World world, BlockPos pos, EntityPlayer caster, int time) {
        if(world.isAirBlock(pos)) {
            world.setBlockState(pos, BlockRegistry.getInstance().blockSolidAir.getDefaultState());
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileSolidAir) {
                ((TileSolidAir) tile).setTimer(time).setOwner(caster);
            }
        }
    }
}
