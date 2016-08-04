package com.teaminfinity.elementalinvocations.network;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAddCharge extends MessageBase<IMessage> {
    private IMagicCharge charge;
    private EntityPlayer player;

    public MessageAddCharge() {}

    public MessageAddCharge(EntityPlayer player, IMagicCharge charge) {
        this();
        this.charge = charge;
        this.player = player;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.charge != null && this.player != null) {
            IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(this.player);
            if(properties != null) {
                properties.addCharge(this.charge);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        Element element = Element.values()[buf.readInt()];
        int level = buf.readInt();
        this.charge = new IMagicCharge() {
            @Override
            public Element element() {
                return element;
            }

            @Override
            public int level() {
                return level;
            }
        };
        this.player = this.readPlayerFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.charge.element().ordinal());
        buf.writeInt(this.charge.level());
        this.writePlayerToByteBuf(buf, this.player);
    }
}
