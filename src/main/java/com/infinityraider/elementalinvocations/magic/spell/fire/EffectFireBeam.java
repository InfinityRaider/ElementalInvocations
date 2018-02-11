package com.infinityraider.elementalinvocations.magic.spell.fire;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.magic.spell.SpellEffectBeamAbstract;
import com.infinityraider.infinitylib.utility.DamageDealer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutableTriple;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectFireBeam extends SpellEffectBeamAbstract {
    private static final DamageDealer DMG = MagicDamageHandler.getInstance().getDamageDealer(Element.FIRE);

    private Map<UUID, MutableTriple<BlockPos, MeltableBlockState, Integer>> meltProgress = new HashMap<>();

    @Override
    protected boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick, @Nullable RayTraceResult target) {
        if(!caster.getEntityWorld().isRemote && target != null) {
            //damage and set entities on fire
            int potency = potencies.getPotency(Element.FIRE) / 5;
            if (target.entityHit != null) {
                if (potency >= 5 || channelTick % (6 - potency) == 0) {
                    int hurtResistTime = target.entityHit.hurtResistantTime;
                    target.entityHit.hurtResistantTime = 0;
                    DMG.apply(target.entityHit, caster, 2);
                    target.entityHit.hurtResistantTime = hurtResistTime;
                }
                if (target.entityHit instanceof EntityLivingBase && !target.entityHit.isBurning()) {
                    target.entityHit.setFire(potency);
                }
            } else {
                //melt blocks
                BlockPos pos = target.getBlockPos();
                if(pos == null) {
                    meltProgress.remove(caster.getUniqueID());
                } else {
                    if(meltProgress.containsKey(caster.getUniqueID())) {
                        MutableTriple<BlockPos, MeltableBlockState, Integer> melting = meltProgress.get(caster.getUniqueID());
                        if(melting.getLeft().equals(pos)) {
                            melting.setRight(melting.getRight() + 1);
                            if(checkMeltProgressForCompletion(melting.getMiddle(), channelTick, potency)) {
                                meltProgress.remove(caster.getUniqueID());
                                melting.getMiddle().apply(caster.getEntityWorld(), pos);
                            }
                        } else {
                            meltProgress.remove(caster.getUniqueID());
                        }
                    } else {
                        IBlockState state = caster.getEntityWorld().getBlockState(pos);
                        if(MeltableBlockState.isMeltable(state)) {
                            MeltableBlockState meltableBlockState = MeltableBlockState.getMeltableBlock(state);
                            if(!checkMeltProgressForCompletion(meltableBlockState, channelTick, potency)) {
                                meltProgress.put(caster.getUniqueID(), new MutableTriple<>(pos, meltableBlockState, 0));
                            } else {
                                meltableBlockState.apply(caster.getEntityWorld(), pos);
                            }
                        } else if(target.sideHit != null && caster.getEntityWorld().isAirBlock(pos.offset(target.sideHit))) {
                            caster.getEntityWorld().setBlockState(pos.offset(target.sideHit), Blocks.FIRE.getDefaultState(), 11);
                        }
                    }
                }
            }
        }
        return true;
    }

    protected boolean checkMeltProgressForCompletion(MeltableBlockState melting, int channelTick, int potency) {
        return potency > 0 && channelTick >= melting.getBaseMeltTicks() / potency;
    }

    @Override
    protected void afterPlayerStoppedChanneling(EntityPlayer caster, IPotencyMap potencies, int channelTick) {}

    @Override
    protected double getBeamRange(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        return 32;
    }

    public static class MeltableBlockState {
        private static final Map<IBlockState, MeltableBlockState> MELTABLE_BLOCKS = new HashMap<>();

        public static boolean isMeltable(IBlockState state) {
            return MELTABLE_BLOCKS.containsKey(state);
        }

        public static MeltableBlockState getMeltableBlock(IBlockState state) {
            return MELTABLE_BLOCKS.get(state);
        }

        private final IBlockState baseState;
        private final IBlockState moltenState;
        private final int baseMeltTicks;

        public MeltableBlockState(IBlockState baseState, IBlockState moltenState, int baseMeltTicks) {
            this.baseState = baseState;
            this.moltenState = moltenState;
            this.baseMeltTicks = baseMeltTicks;
            MELTABLE_BLOCKS.put(this.getMeltableState(), this);
        }

        public IBlockState getMeltableState() {
            return this.baseState;
        }
        public IBlockState getMoltenState() {
            return this.moltenState;
        }

        public int getBaseMeltTicks() {
            return this.baseMeltTicks;
        }

        public void apply(World world, BlockPos pos) {
            world.setBlockState(pos, this.getMoltenState(), 3);
            this.getMoltenState().neighborChanged(world, pos, this.getMoltenState().getBlock());
            for(EnumFacing facing : EnumFacing.values()) {
            }
        }
    }

    static {
        //TODO: add config file
        new MeltableBlockState(Blocks.SAND.getDefaultState(), Blocks.GLASS.getDefaultState(), 50);
        new MeltableBlockState(Blocks.STONE.getDefaultState(), Blocks.LAVA.getDefaultState(), 100);
        new MeltableBlockState(Blocks.COBBLESTONE.getDefaultState(), Blocks.LAVA.getDefaultState(), 100);
        new MeltableBlockState(Blocks.OBSIDIAN.getDefaultState(), Blocks.LAVA.getDefaultState(), 500);
    }
}
