package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.entity.EntityReplicate;
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
        if(this.replica != null) {
            this.replica.swapWithPlayer();
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
