package com.teaminfinity.elementalinvocations.capability;

import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProperties;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityPlayerMagicProperties implements ICapabilityImplementation<EntityPlayer, PlayerMagicProperties> {
    private static final CapabilityPlayerMagicProperties INSTANCE = new CapabilityPlayerMagicProperties();

    public static CapabilityPlayerMagicProperties getInstance() {
        return INSTANCE;
    }

    public static final ResourceLocation KEY = new ResourceLocation(Reference.MOD_ID, "player_magic_properties");

    @CapabilityInject(PlayerMagicProperties.class)
    public static Capability<PlayerMagicProperties> PLAYER_MAGIC_PROPERTIES = null;

    private CapabilityPlayerMagicProperties() {}

    @Override
    public Capability<PlayerMagicProperties> getCapability() {
        return PLAYER_MAGIC_PROPERTIES;
    }

    @Override
    public boolean shouldApplyCapability(EntityPlayer carrier) {
        return true;
    }

    @Override
    public PlayerMagicProperties onValueAddedToCarrier(PlayerMagicProperties value, EntityPlayer carrier) {
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
    public Class<PlayerMagicProperties> getCapabilityClass() {
        return PlayerMagicProperties.class;
    }

    @Override
    public PlayerMagicProperties call() {
        return new PlayerMagicProperties();
    }

    public static IPlayerMagicProperties getMagicProperties(EntityPlayer player) {
        return player.hasCapability(PLAYER_MAGIC_PROPERTIES, null) ? player.getCapability(PLAYER_MAGIC_PROPERTIES, null) : null;
    }
}
