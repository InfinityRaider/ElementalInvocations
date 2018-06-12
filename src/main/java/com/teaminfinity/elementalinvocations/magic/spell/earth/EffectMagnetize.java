package com.teaminfinity.elementalinvocations.magic.spell.earth;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntityMagnetizedRock;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class EffectMagnetize implements ISpellEffect {
    public static int SEARCH_RADIUS = 5;

    public static List<IBlockState> ALLOWED_STATES = Arrays.asList(
                    Blocks.DIRT.getDefaultState(),
                    Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE),
                    Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE),
                    Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE_SMOOTH),
                    Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE),
                    Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH),
                    Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE),
                    Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH),
                    Blocks.COBBLESTONE.getDefaultState(),
                    Blocks.SAND.getDefaultState(),
                    Blocks.GRAVEL.getDefaultState(),
                    Blocks.NETHERRACK.getDefaultState(),
                    Blocks.SOUL_SAND.getDefaultState());

    private static Map<UUID, List<EntityMagnetizedRock>> rocks = new HashMap<>();

    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        int potency = potencies[Element.EARTH.ordinal()]/5;
        Map<BlockPos, IBlockState> blocks = this.buildPossibleRocksMap(caster);
        List<BlockPos> positions = new ArrayList<>(blocks.keySet());
        List<EntityMagnetizedRock> rocks = new ArrayList<>();
        for(int i = 0; i < potency; i++) {
            if(positions.isEmpty()) {
                break;
            }
            int index = caster.getRNG().nextInt(positions.size());
            BlockPos pos = positions.get(index);
            EntityMagnetizedRock entity = new EntityMagnetizedRock(caster, this, blocks.get(pos), potency);
            entity.posX = pos.getX();
            entity.posY = pos.getY();
            entity.posZ = pos.getZ();
            rocks.add(entity);
            caster.getEntityWorld().setBlockToAir(pos);
            positions.remove(index);
        }
        if(!EffectMagnetize.rocks.containsKey(caster.getUniqueID())) {
            EffectMagnetize.rocks.put(caster.getUniqueID(), rocks);
        } else {
            EffectMagnetize.rocks.get(caster.getUniqueID()).addAll(rocks);
        }
        for(EntityMagnetizedRock rock : rocks) {
            caster.getEntityWorld().spawnEntity(rock);
        }
        return false;
    }

    protected Map<BlockPos, IBlockState> buildPossibleRocksMap(EntityPlayer caster) {
        Map<BlockPos, IBlockState> map = new HashMap<>();
        World world = caster.getEntityWorld();
        BlockPos casterPos = caster.getPosition();
        for (int x = casterPos.getX() - SEARCH_RADIUS; x <= casterPos.getX() + SEARCH_RADIUS; x++) {
            for (int z = casterPos.getZ() - SEARCH_RADIUS; z <= casterPos.getZ() + SEARCH_RADIUS; z++) {
                //start with blocks the caster can pull up
                BlockPos pos = new BlockPos(x, casterPos.getY(), z);
                IBlockState stateAt = world.getBlockState(pos);
                while(stateAt.getMaterial() == Material.AIR && (pos.getY() + SEARCH_RADIUS) >= caster.posY) {
                    pos = pos.down();
                    stateAt = world.getBlockState(pos);
                }
                if(ALLOWED_STATES.contains(stateAt)) {
                    map.put(pos, stateAt);
                }
                //blocks which the caster can pull down from the ceiling
                pos = new BlockPos(x, casterPos.getY() + caster.height, z);
                stateAt = world.getBlockState(pos);
                while(stateAt.getMaterial() == Material.AIR && (pos.getY() - SEARCH_RADIUS) <= caster.posY) {
                    pos = pos.up();
                    stateAt = world.getBlockState(pos);
                }
                if(ALLOWED_STATES.contains(stateAt)) {
                    map.put(pos, stateAt);
                }
            }
        }
        return map;
    }

    @Override
    public boolean isLingeringEffect() {
        return true;
    }

    public boolean lingerUpdate(EntityPlayer caster) {
        return !rocks.containsKey(caster.getUniqueID()) || rocks.get(caster.getUniqueID()).isEmpty();
    }

    @Override
    public boolean spellContextAction(EntityPlayer caster) {
        if(rocks.containsKey(caster.getUniqueID())) {
            List<EntityMagnetizedRock> list = rocks.get(caster.getUniqueID());
            if(list.isEmpty()) {
                return true;
            }
            list.get(0).throwStone(caster.getLookVec());
        }
        return false;
    }


    public void onEntityMagnetizedRockRemoved(EntityMagnetizedRock rock) {
        if(rocks.containsKey(rock.getThrower().getUniqueID())) {
            rocks.get(rock.getThrower().getUniqueID()).remove(rock);
        }
    }
}