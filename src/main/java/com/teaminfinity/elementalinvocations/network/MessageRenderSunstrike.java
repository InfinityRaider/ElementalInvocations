package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.entity.EntitySunstrike;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
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
        if(ctx.side == Side.CLIENT && this.sunstrike != null) {
            this.sunstrike.setShouldRender();
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        Entity e = this.readEntityFromByteBuf(buf);
        if(e instanceof EntitySunstrike) {
            this.sunstrike = (EntitySunstrike) e;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writeEntityToByteBuf(buf, this.sunstrike);
    }
}
