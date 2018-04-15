package com.infinityraider.elementalinvocations.network;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.handler.HexHandler;
import com.infinityraider.elementalinvocations.render.entity.RenderHexedHandler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHexed extends MessageBase<IMessage> {
    private int dimension;
    private int id;
    private boolean hexed;

    public MessageHexed() {
        super();
    }

    public MessageHexed(int dimension, int id, boolean hexed) {
        this();
        this.dimension = dimension;
        this.id = id;
        this.hexed = hexed;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx != null && ctx.side == Side.CLIENT) {
            HexHandler.getInstance().setHexedClient(this.dimension, this.id, hexed);
            Entity entity = ElementalInvocations.proxy.getEntityById(this.dimension, this.id);
            if(this.hexed) {
                if (entity != null && entity instanceof EntityLivingBase) {
                    RenderHexedHandler.getInstance().addHexedEntity((EntityLivingBase) entity);
                }
            } else {
                RenderHexedHandler.getInstance().removeHexedEntity(this.id);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
