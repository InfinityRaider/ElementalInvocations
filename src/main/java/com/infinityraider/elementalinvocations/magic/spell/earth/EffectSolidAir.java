package com.infinityraider.elementalinvocations.magic.spell.earth;

import com.infinityraider.elementalinvocations.ElementalInvocations;
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
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class EffectSolidAir implements ISpellEffect {
    private static final EffectSolidAir INSTANCE = new EffectSolidAir();

    public static EffectSolidAir getInstance() {
        return INSTANCE;
    }

    private static final Map<UUID, Set<TileSolidAir>> barriers = new HashMap<>();
    private static Map<Chunk, Set<TileSolidAir>> barriersPerChunk = new HashMap<>();

    public EffectSolidAir() {
        ElementalInvocations.proxy.registerEventHandler(this);
    }

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
        if(!world.isRemote && world.isAirBlock(pos)) {
            world.setBlockState(pos, BlockRegistry.getInstance().blockSolidAir.getDefaultState());
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileSolidAir) {
                TileSolidAir barrier = (TileSolidAir) tile;
                Chunk chunk = world.getChunkFromBlockCoords(pos);
                barrier.setTimer(time).setOwner(caster);
                if(!barriers.containsKey(caster.getUniqueID())) {
                    barriers.put(caster.getUniqueID(), new HashSet<>());
                }
                if(!barriersPerChunk.containsKey(chunk)) {
                    barriersPerChunk.put(chunk, new HashSet<>());
                }
                barriers.get(caster.getUniqueID()).add(barrier);
                barriersPerChunk.get(chunk).add(barrier);
            }
        }
    }

    public static void removeBarrier(TileSolidAir barrier) {
        if(!barrier.isRemote()) {
            UUID id = barrier.getOwnerId();
            if (barriers.containsKey(id)) {
                barriers.get(id).remove(barrier);
            } else {
                barriers.forEach((key, set) -> set.remove(barrier));
            }
            Chunk chunk = barrier.getChunk();
            if(barriersPerChunk.containsKey(chunk)) {
                barriersPerChunk.get(chunk).remove(barrier);
            }
        }
    }

    @Override
    public boolean isLingeringEffect() {
        return true;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onChunkUnload(ChunkEvent.Unload event) {
        Chunk chunk = event.getChunk();
        if(barriersPerChunk.containsKey(chunk)) {
            barriersPerChunk.forEach((key, set) -> set.forEach(TileSolidAir::removeThisBarrier));
        }
        barriersPerChunk.remove(chunk);
    }

    public boolean lingerUpdate(EntityPlayer caster) {
        return !barriers.containsKey(caster.getUniqueID()) || barriers.get(caster.getUniqueID()).isEmpty();
    }

    /**
     * Called when the caster presses the spell context key bind,
     * @param caster the player who cast this spell effect and has it as a lingering effect
     * @return true to end this effect
     */
    public boolean spellContextAction(EntityPlayer caster) {
        if(!caster.getEntityWorld().isRemote) {
            RayTraceResult result = RayTraceHelper.getTargetBlock(caster, 32);
            if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = result.getBlockPos();
                if(pos != null) {
                    TileEntity tile = caster.getEntityWorld().getTileEntity(pos);
                    if(tile instanceof TileSolidAir) {
                        ((TileSolidAir) tile).clearWholeBarrier();
                    }
                }
            }
        }
        return false;
    }
}
