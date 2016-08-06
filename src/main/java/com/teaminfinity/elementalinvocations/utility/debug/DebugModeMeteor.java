package com.teaminfinity.elementalinvocations.utility.debug;

import com.teaminfinity.elementalinvocations.entity.EntityMeteor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DebugModeMeteor extends DebugMode {
    @Override
    public String debugName() {
        return "meteor";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            EntityMeteor meteor = new EntityMeteor(player, 15, 10);
            Vec3d look = player.getLookVec();
            meteor.setThrowableHeading(look.xCoord, 0, look.zCoord, 0.1F, 0F);
            meteor.posX = player.posX + look.xCoord;
            meteor.posY = player.posY + player.getEyeHeight();
            meteor.posZ = player.posZ + look.zCoord;
            world.spawnEntityInWorld(meteor);
        }
    }
}
