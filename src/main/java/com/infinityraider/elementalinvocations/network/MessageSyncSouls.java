package com.infinityraider.elementalinvocations.network;

import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSyncSouls extends MessageBase<IMessage> {
    private EntityPlayer player;
    private NBTTagCompound tag;

    public MessageSyncSouls() {
        super();
    }

    public MessageSyncSouls(EntityPlayer player, NBTTagCompound tag) {
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
        if (ctx.side == Side.CLIENT && this.player != null) {
            ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(this.player);
            if (collection != null) {
                collection.readFromNBT(this.tag);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
