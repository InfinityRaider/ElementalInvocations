package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.entity.EntitySunstrike;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageRenderSunstrike extends MessageBase<IMessage> {
    private EntitySunstrike sunstrike;

    public MessageRenderSunstrike() {
        super();
    }

    public MessageRenderSunstrike(EntitySunstrike sunstrike) {
        this();
        this.sunstrike = sunstrike;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.sunstrike != null) {
            this.sunstrike.setShouldRender();
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
