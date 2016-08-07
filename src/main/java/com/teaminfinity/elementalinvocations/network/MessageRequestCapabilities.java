package com.teaminfinity.elementalinvocations.network;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageRequestCapabilities extends MessageBase<MessageSyncCapabilities> {
    private EntityPlayer player;

    public MessageRequestCapabilities() {
        super();
    }

    public MessageRequestCapabilities(EntityPlayer player) {
        this();
        this.player = player;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {

    }

    @Override
    protected MessageSyncCapabilities getReply(MessageContext ctx) {
        if(ctx.side == Side.SERVER && this.player != null) {
            IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(this.player);
            if(properties != null) {
                return new MessageSyncCapabilities(this.player, properties.writeToNBT());
            }
        }
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = this.readPlayerFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writePlayerToByteBuf(buf, this.player);
    }
}
