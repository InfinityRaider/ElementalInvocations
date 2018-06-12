package com.infinityraider.elementalinvocations.capability;

import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.magic.PlayerMagicProperties;
import com.infinityraider.elementalinvocations.reference.Reference;
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
    public PlayerMagicProperties createNewValue(EntityPlayer carrier) {
        return new PlayerMagicProperties(carrier);
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

    public static IPlayerMagicProperties getMagicProperties(EntityPlayer player) {
        return player.hasCapability(PLAYER_MAGIC_PROPERTIES, null) ? player.getCapability(PLAYER_MAGIC_PROPERTIES, null) : null;
    }
}
