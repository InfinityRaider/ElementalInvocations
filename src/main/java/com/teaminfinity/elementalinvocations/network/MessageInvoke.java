package com.teaminfinity.elementalinvocations.network;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageInvoke extends MessageBase<IMessage> {
    private EntityPlayer player;
    private int experience;

    public MessageInvoke() {
        super();
    }

    public MessageInvoke(EntityPlayer player, int experience) {
        this();
        this.player = player;
        this.experience = experience;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.player != null) {
            IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(this.player);
            if(properties != null) {
                properties.getCharges().clear();
                properties.addExperience(this.experience);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = readPlayerFromByteBuf(buf);
        this.experience = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writePlayerToByteBuf(buf, this.player);
        buf.writeInt(this.experience);
    }
}
