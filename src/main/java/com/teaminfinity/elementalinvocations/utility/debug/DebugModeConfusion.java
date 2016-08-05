package com.teaminfinity.elementalinvocations.utility.debug;

import com.teaminfinity.elementalinvocations.potion.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModeConfusion extends DebugMode {
    @Override
    public String debugName() {
        return "confusion";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            player.addPotionEffect(new PotionEffect(PotionRegistry.getInstance().POTION_CONFUSION, 400));
        }
    }
}
