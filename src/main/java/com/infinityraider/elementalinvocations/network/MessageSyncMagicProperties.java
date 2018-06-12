package com.infinityraider.elementalinvocations.network;

import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSyncMagicProperties extends MessageBase<IMessage> {
    private EntityPlayer player;
    private NBTTagCompound tag;

    public MessageSyncMagicProperties() {
        super();
    }

    public MessageSyncMagicProperties(EntityPlayer player, NBTTagCompound tag) {
        this();
        this.player = player;
        this.tag = tag;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if (this.player != null) {
            IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(this.player);
            if (properties != null) {
                properties.readFromNBT(this.tag);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
