package com.teaminfinity.elementalinvocations.magic.spell.death;

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
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;

public class PlayerSoulCollectionProvider implements ICapabilitySerializable<NBTTagCompound> {

	public static ResourceLocation KEY = new ResourceLocation(Reference.MOD_ID, "player_soul_collections");

	private final ISoulCollection collection;

	public PlayerSoulCollectionProvider(EntityPlayer player) {
		this.collection = new PlayerSoulCollection(player);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return Capabilities.PLAYER_SOUL_COLLECTION != null && capability == Capabilities.PLAYER_SOUL_COLLECTION;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return hasCapability(capability, facing) ? (T) collection : null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) Capabilities.PLAYER_SOUL_COLLECTION.getStorage().writeNBT(Capabilities.PLAYER_SOUL_COLLECTION, collection, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		Capabilities.PLAYER_SOUL_COLLECTION.getStorage().readNBT(Capabilities.PLAYER_SOUL_COLLECTION, collection, null, nbt);
	}

	public static class Storage implements Capability.IStorage<ISoulCollection> {

		@Override
		public NBTBase writeNBT(Capability<ISoulCollection> capability, ISoulCollection instance, EnumFacing side) {
			return instance != null ? instance.writeToNBT() : null;
		}

		@Override
		public void readNBT(Capability<ISoulCollection> capability, ISoulCollection instance, EnumFacing side, NBTBase nbt) {
			if (instance != null && (nbt instanceof NBTTagCompound)) {
				instance.readFromNBT((NBTTagCompound) nbt);
			}
		}
	}

	public static ISoulCollection getSoulCollection(EntityPlayer player) {
		return player.hasCapability(Capabilities.PLAYER_SOUL_COLLECTION, null) ? player.getCapability(Capabilities.PLAYER_SOUL_COLLECTION, null) : null;
	}
}
