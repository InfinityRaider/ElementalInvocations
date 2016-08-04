package com.teaminfinity.elementalinvocations.proxy.base;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IClientProxyBase extends IProxyBase {
    @Override
    default EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    default World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    default World getWorldByDimensionId(int dimension) {
        Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();
        if(effectiveSide == Side.SERVER) {
            return FMLClientHandler.instance().getServer().worldServerForDimension(dimension);
        } else {
            return getClientWorld();
        }
    }

    @Override
    default Side getPhysicalSide() {
        return Side.CLIENT;
    }

    @Override
    default Side getEffectiveSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    @Override
    default void queueTask(Runnable task) {
        if(getEffectiveSide() == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(task);
        } else {
            FMLClientHandler.instance().getServer().addScheduledTask(task);
        }
    }
}