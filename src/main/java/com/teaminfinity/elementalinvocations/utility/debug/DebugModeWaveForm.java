package com.teaminfinity.elementalinvocations.utility.debug;

import com.teaminfinity.elementalinvocations.entity.EntityWaveForm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModeWaveForm extends DebugMode {
    @Override
    public String debugName() {
        return "waveform";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            EntityWaveForm waveForm = new EntityWaveForm(player, 25);
            world.spawnEntityInWorld(waveForm);
        }
    }
}
