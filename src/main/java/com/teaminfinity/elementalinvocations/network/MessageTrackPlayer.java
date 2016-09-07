package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.handler.PlayerMovementTrackingHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageTrackPlayer extends MessageBase<IMessage> {
    private boolean status;

    public MessageTrackPlayer() {
        super();
    }

    public MessageTrackPlayer(boolean status) {
        this();
        this.status = status;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT) {
            if(status) {
                PlayerMovementTrackingHandler.getInstance().startTracking();
            } else {
                PlayerMovementTrackingHandler.getInstance().stopTracking();
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.status = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.status);
    }
}
