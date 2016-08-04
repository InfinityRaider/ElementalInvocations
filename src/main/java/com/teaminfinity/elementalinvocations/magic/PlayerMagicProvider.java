package com.teaminfinity.elementalinvocations.magic;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.reference.Capabilities;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class PlayerMagicProvider implements ICapabilitySerializable<NBTTagCompound> {
    public static ResourceLocation KEY = new ResourceLocation(Reference.MOD_ID, "player_magic_properties");

    private IPlayerMagicProperties magicProperties;

    public PlayerMagicProvider(EntityPlayer player) {
        this.magicProperties = Capabilities.PLAYER_MAGIC_PROPERTIES != null ? Capabilities.PLAYER_MAGIC_PROPERTIES.getDefaultInstance().setPlayer(player) : null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return Capabilities.PLAYER_MAGIC_PROPERTIES != null && capability == Capabilities.PLAYER_MAGIC_PROPERTIES;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? (T) magicProperties : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) Capabilities.PLAYER_MAGIC_PROPERTIES.getStorage().writeNBT(Capabilities.PLAYER_MAGIC_PROPERTIES, magicProperties, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Capabilities.PLAYER_MAGIC_PROPERTIES.getStorage().readNBT(Capabilities.PLAYER_MAGIC_PROPERTIES, magicProperties, null, nbt);
    }

    public static class Storage implements Capability.IStorage<IPlayerMagicProperties> {
        @Override
        public NBTBase writeNBT(Capability<IPlayerMagicProperties> capability, IPlayerMagicProperties instance, EnumFacing side) {
            return instance != null ? instance.writeToNBT() : null;
        }

        @Override
        public void readNBT(Capability<IPlayerMagicProperties> capability, IPlayerMagicProperties instance, EnumFacing side, NBTBase nbt) {
            if(instance != null && (nbt instanceof NBTTagCompound)) {
                instance.readFromNBT((NBTTagCompound) nbt);
            }
        }
    }

    public static IPlayerMagicProperties getMagicProperties(EntityPlayer player) {
        return player.hasCapability(Capabilities.PLAYER_MAGIC_PROPERTIES, null) ? player.getCapability(Capabilities.PLAYER_MAGIC_PROPERTIES, null) : null;
    }
}
