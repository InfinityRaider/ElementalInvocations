package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.entity.EntityReplicate;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageTrackPlayerUpdate extends MessageBase<IMessage> {
    private double deltaX;
    private double deltaY;
    private double deltaZ;

    public MessageTrackPlayerUpdate() {
        super();
    }

    public MessageTrackPlayerUpdate(double deltaX, double deltaY, double deltaZ) {
        this();
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.SERVER && ctx.getServerHandler().playerEntity != null) {
            EntityReplicate.updatePlayerMovement(ctx.getServerHandler().playerEntity, this.deltaX, this.deltaY, this.deltaZ);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.deltaX = buf.readDouble();
        this.deltaY = buf.readDouble();
        this.deltaZ = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.deltaX);
        buf.writeDouble(this.deltaY);
        buf.writeDouble(this.deltaZ);
    }
}
