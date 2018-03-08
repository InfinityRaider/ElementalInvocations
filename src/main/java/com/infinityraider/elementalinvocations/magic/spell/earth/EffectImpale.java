package com.infinityraider.elementalinvocations.magic.spell.earth;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class EffectImpale implements ISpellEffect {
    public static final List<Block> BLOCKS = EffectEarthquake.BLOCKS;

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        int amount = potencies.getPotency(Element.EARTH) / 3;
        int potency = potencies.getPotency(Element.DEATH);
        EnumFacing dir = caster.getHorizontalFacing();
        BlockPos pos = caster.getPosition().offset(dir, 9);
        if(pos != null) {
            EnumFacing back = dir.getOpposite();
            EnumFacing left = back.rotateY();
            EnumFacing right = left.getOpposite();
            switch(amount) {
                case 5:
                    placeSpike(caster, potency, pos.offset(back, 3).offset(left, 5));
                    placeSpike(caster, potency, pos.offset(back, 3).offset(right, 5));
                case 4:
                    placeSpike(caster, potency, pos.offset(back, 2).offset(left, 4));
                    placeSpike(caster, potency, pos.offset(back, 2).offset(right, 4));
                case 3:
                    placeSpike(caster, potency, pos.offset(back).offset(left, 3));
                    placeSpike(caster, potency, pos.offset(back).offset(right, 3));
                case 2:
                    placeSpike(caster, potency, pos.offset(back).offset(left, 2));
                    placeSpike(caster, potency, pos.offset(back).offset(right, 2));
                case 1:
                    placeSpike(caster, potency, pos.offset(left));
                    placeSpike(caster, potency, pos.offset(right));
                    placeSpike(caster, potency, pos);
            }
        }
        return false;
    }

    protected void placeSpike(EntityPlayer caster, int potency, BlockPos pos) {
        IBlockAccess world = caster.getEntityWorld();
        BlockPos actual = validateBlockPos(world, pos);
        if(actual != null) {
            caster.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos, pos.up(2)), e -> e != caster).forEach(entity ->
                    MagicDamageHandler.getInstance().dealDamage(entity, potency, caster, Element.EARTH, potency, caster.getLookVec()));
            caster.getEntityWorld().setBlockState(pos, BlockRegistry.getInstance().blockImpaleSpike.getDefaultState());
        }
    }

    protected BlockPos validateBlockPos(IBlockAccess world, BlockPos pos) {
        return this.validateBlockPos(world, pos, 5);
    }

    protected BlockPos validateBlockPos(IBlockAccess world, BlockPos pos, int index) {
        if(pos == null || index <= 0) {
            return null;
        }
        if(world.isAirBlock(pos)) {
            return validateBlockPos(world, pos.down(), index - 1);
        } else {
            BlockPos up = pos.up();
            if(world.isAirBlock(up)) {
                return isValidBaseBlock(world, pos) ? up : null;
            } else {
                return validateBlockPos(world, up, index - 1);
            }
        }
    }

    protected boolean isValidBaseBlock(IBlockAccess world, BlockPos pos) {
        return BLOCKS.contains(world.getBlockState(pos).getBlock());
    }
}