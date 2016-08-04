package com.teaminfinity.elementalinvocations.utility.debug;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugModeClearMagicProperties extends DebugMode {
    @Override
    public String debugName() {
        return "clear magic properties";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(player);
        if(properties != null) {
            properties.reset();
            if(player.worldObj.isRemote) {
                player.addChatComponentMessage(new TextComponentString(
                        "Reset player magic properties"));
            }
        }
    }
}
