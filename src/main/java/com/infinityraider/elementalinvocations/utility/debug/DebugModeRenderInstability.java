package com.infinityraider.elementalinvocations.utility.debug;

import com.infinityraider.infinitylib.utility.debug.DebugMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DebugModeRenderInstability extends DebugMode {
    @SideOnly(Side.CLIENT)
    private static boolean flag;

    @Override
    public String debugName() {
        return "render instability";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(player.getEntityWorld().isRemote) {
            toggleInstabilityRender(player);
        }
    }

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(player.getEntityWorld().isRemote) {
            toggleInstabilityRender(player);
        }
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(player.getEntityWorld().isRemote) {
            toggleInstabilityRender(player);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void toggleInstabilityRender(EntityPlayer player) {
        flag = !flag;
        player.addChatMessage(new TextComponentString((flag ? "En" : "Dis") + "abled instability rendering on HUD"));
    }

    @SideOnly(Side.CLIENT)
    public static boolean doRender() {
        return flag;
    }
}