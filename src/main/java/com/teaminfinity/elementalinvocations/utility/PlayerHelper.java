package com.teaminfinity.elementalinvocations.utility;

import com.teaminfinity.elementalinvocations.network.MessageSetPlayerPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerHelper {
    public static void setPlayerPosition(EntityPlayer player, double x, double y, double z) {
        if(!player.getEntityWorld().isRemote && player instanceof EntityPlayerMP) {
            new MessageSetPlayerPosition(x, y, z).sendTo((EntityPlayerMP) player);
        } else {
            player.setPosition(x, y, z);
            player.prevPosX = x;
            player.prevPosY = y;
            player.prevPosZ = z;
        }
    }
}
