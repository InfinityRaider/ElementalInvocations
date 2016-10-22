package com.teaminfinity.elementalinvocations.capability;

import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollection;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityPlayerSoulCollection implements ICapabilityImplementation<EntityPlayer, PlayerSoulCollection> {
    private static final CapabilityPlayerSoulCollection INSTANCE = new CapabilityPlayerSoulCollection();

    public static CapabilityPlayerSoulCollection getInstance() {
        return INSTANCE;
    }

    public static ResourceLocation KEY = new ResourceLocation(Reference.MOD_ID, "player_soul_collections");

    @CapabilityInject(PlayerSoulCollection.class)
    public static Capability<PlayerSoulCollection> PLAYER_SOUL_COLLECTION = null;

    private CapabilityPlayerSoulCollection() {}

    @Override
    public Capability<PlayerSoulCollection> getCapability() {
        return PLAYER_SOUL_COLLECTION;
    }

    @Override
    public boolean shouldApplyCapability(EntityPlayer carrier) {
        return true;
    }

    @Override
    public PlayerSoulCollection onValueAddedToCarrier(PlayerSoulCollection value, EntityPlayer carrier) {
        return value.setPlayer(carrier);
    }

    @Override
    public ResourceLocation getCapabilityKey() {
        return KEY;
    }

    @Override
    public Class<EntityPlayer> getCarrierClass() {
        return EntityPlayer.class;
    }

    @Override
    public Class<PlayerSoulCollection> getCapabilityClass() {
        return PlayerSoulCollection.class;
    }

    @Override
    public PlayerSoulCollection call() {
        return new PlayerSoulCollection();
    }

    public static ISoulCollection getSoulCollection(EntityPlayer player) {
        return player.hasCapability(PLAYER_SOUL_COLLECTION, null) ? player.getCapability(PLAYER_SOUL_COLLECTION, null) : null;
    }
}
