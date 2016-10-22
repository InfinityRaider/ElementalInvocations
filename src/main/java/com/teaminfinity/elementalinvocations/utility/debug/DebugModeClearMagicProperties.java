package com.teaminfinity.elementalinvocations.utility.debug;

import com.infinityraider.infinitylib.utility.debug.DebugMode;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import net.minecraft.entity.EntityLivingBase;
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
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {}

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(player);
        ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(player);
        if(!world.isRemote) {
            if(properties != null) {
                properties.reset();
            }
            if(collection != null) {
                collection.releaseSouls();
            }
            player.addChatComponentMessage(new TextComponentString("Reset player magic properties"));
        }
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {}
}
