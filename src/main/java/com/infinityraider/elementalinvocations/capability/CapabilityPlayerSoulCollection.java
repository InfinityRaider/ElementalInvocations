package com.infinityraider.elementalinvocations.capability;

import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.magic.spell.death.PlayerSoulCollection;
import com.infinityraider.elementalinvocations.reference.Reference;
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
    public PlayerSoulCollection createNewValue(EntityPlayer carrier) {
        return new PlayerSoulCollection(carrier);
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

    public static ISoulCollection getSoulCollection(EntityPlayer player) {
        return player.hasCapability(PLAYER_SOUL_COLLECTION, null) ? player.getCapability(PLAYER_SOUL_COLLECTION, null) : null;
    }
}
