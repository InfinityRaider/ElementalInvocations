package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.entity.EntityReplicate;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSwapPlayerPosition extends MessageBase<IMessage> {
    private EntityReplicate replica;

    public MessageSwapPlayerPosition() {
        super();
    }

    public MessageSwapPlayerPosition(EntityReplicate replica) {
        this();
        this.replica = replica;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.replica != null) {
            this.replica.swapWithPlayer();
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        Entity entity = this.readEntityFromByteBuf(buf);
        if(entity instanceof EntityReplicate) {
            this.replica = (EntityReplicate) entity;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writeEntityToByteBuf(buf, this.replica);
    }
}
