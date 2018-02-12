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
        BlockPos pos = getStartPos(caster, 5);
        EnumFacing dir = caster.getHorizontalFacing();
        Vec3d look = caster.getLookVec();
        boolean horizontal = Math.abs(look.yCoord) > Math.max(Math.abs(look.xCoord), Math.abs(look.zCoord));
        int time = potencyEarth*20;
        int limX = horizontal || dir.getAxis() == EnumFacing.Axis.Z ? potencyAir/2 : 0;
        int limY = horizontal ? 0 : potencyAir/2;
        int limZ = horizontal || dir.getAxis() == EnumFacing.Axis.X ? potencyAir/2 : 0;
        for(int x = -limX; x <= limX; x++) {
            for(int y = -limY; y <= limY; y++) {
                for(int z = -limZ; z <= limZ; z++) {
                    this.placeBarrier(world, pos.add(x, y, z), caster, time);
                }
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
