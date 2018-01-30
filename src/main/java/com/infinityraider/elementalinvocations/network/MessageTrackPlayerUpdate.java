package com.infinityraider.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.elementalinvocations.entity.EntityReplicate;
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
        if(ctx.getServerHandler().playerEntity != null) {
            EntityReplicate.updatePlayerMovement(ctx.getServerHandler().playerEntity, this.deltaX, this.deltaY, this.deltaZ);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
