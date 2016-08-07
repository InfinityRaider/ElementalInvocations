package com.teaminfinity.elementalinvocations.network;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
        if (ctx.side == Side.CLIENT && this.player != null) {
            IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(this.player);
            if (properties != null) {
                properties.readFromNBT(this.tag);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = this.readPlayerFromByteBuf(buf);
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writePlayerToByteBuf(buf, this.player);
        ByteBufUtils.writeTag(buf, this.tag);
    }
}
