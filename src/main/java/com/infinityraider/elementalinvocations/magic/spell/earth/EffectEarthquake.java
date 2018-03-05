package com.infinityraider.elementalinvocations.magic.spell.earth;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.block.tile.TileEarthquake;
import com.infinityraider.elementalinvocations.registry.BlockRegistry;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EffectEarthquake implements ISpellEffect {
    public static final int RANGE = 4;

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        int range = potencies.getPotency(Element.EARTH) / 5;
        RayTraceResult result = RayTraceHelper.getTargetBlock(caster, 32);
        if(result != null && result.getBlockPos() != null) {
            for(int x = -range; x <= range; x++) {
                for(int z = -range; z <= range; z++) {
                    this.putEarthquake(caster, caster.getEntityWorld(), result.getBlockPos().add(x, 0, z));
                }
            }
        }
        return false;
    }

    protected void putEarthquake(EntityPlayer caster, World world, BlockPos pos) {
        if(world.isAirBlock(pos)) {
            this.putEarthQuakeDown(caster, world, pos.down(), 0);
        } else {
            this.putEarthQuakeUp(caster, world, pos.up(), 0);
        }
    }

    protected void putEarthQuakeDown(EntityPlayer caster, World world, BlockPos pos, int index) {
        if(index >= RANGE) {
            return;
        }
        if(world.isAirBlock(pos)) {
            this.putEarthQuakeDown(caster, world, pos.down(), index + 1);
        } else {
            this.makeEarthQuake(caster, world, pos);
        }
    }

    protected void putEarthQuakeUp(EntityPlayer caster, World world, BlockPos pos, int index) {
        if(index >= RANGE) {
            return;
        }
        if(world.isAirBlock(pos)) {
            this.makeEarthQuake(caster, world, pos.down());
        } else {
            this.putEarthQuakeUp(caster, world, pos.up(), index + 1);
        }
    }

    protected void makeEarthQuake(EntityPlayer caster, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if(BLOCKS.contains(state.getBlock())) {
            world.setBlockState(pos, BlockRegistry.getInstance().blockEarthQuake.getDefaultState());
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileEarthquake) {
                ((TileEarthquake) tile).setOriginalState(state).setCaster(caster);
            } else {
                world.setBlockState(pos, state);
            }
        }
    }

    public static final List<Block> BLOCKS = ImmutableList.of(
            Blocks.STONE,
            Blocks.COBBLESTONE,
            Blocks.DIRT,
            Blocks.SAND,
            Blocks.SANDSTONE,
            Blocks.SOUL_SAND,
            Blocks.NETHERRACK,
            Blocks.COAL_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.EMERALD_ORE,
            Blocks.GOLD_ORE,
            Blocks.IRON_ORE,
            Blocks.LAPIS_ORE,
            Blocks.LIT_REDSTONE_ORE,
            Blocks.QUARTZ_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.GRASS,
            Blocks.GRAVEL,
            Blocks.RED_SANDSTONE,
            Blocks.BEDROCK,
            Blocks.OBSIDIAN,
            Blocks.MOSSY_COBBLESTONE,
            Blocks.SANDSTONE_STAIRS,
            Blocks.RED_SANDSTONE_STAIRS,
            Blocks.STONE_STAIRS,
            Blocks.STONE_SLAB,
            Blocks.STONE_SLAB2
    );
}