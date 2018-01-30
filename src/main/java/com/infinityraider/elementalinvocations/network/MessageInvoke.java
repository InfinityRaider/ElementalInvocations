package com.infinityraider.elementalinvocations.network;

import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageInvoke extends MessageBase<IMessage> {
    private EntityPlayer player;
    private boolean fizzle;

    public MessageInvoke() {
        super();
    }

    public MessageInvoke(EntityPlayer player, boolean fizzle) {
        this();
        this.player = player;
        this.fizzle = fizzle;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.player != null) {
            IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(this.player);
            if(properties != null) {
                if(this.fizzle) {
                    properties.fizzle();
                } else {
                    properties.invoke();
                }
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
