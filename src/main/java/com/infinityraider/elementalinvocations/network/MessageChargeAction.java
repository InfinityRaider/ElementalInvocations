package com.infinityraider.elementalinvocations.network;

import com.infinityraider.elementalinvocations.api.EnumMagicChargeAction;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageChargeAction extends MessageBase<IMessage> {
    private EntityPlayer player;
    private EnumMagicChargeAction action;

    public MessageChargeAction() {
        super();
    }

    public MessageChargeAction(EntityPlayer player, EnumMagicChargeAction action) {
        this();
        this.player = player;
        this.action = action;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.player != null && this.action != null) {
            IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(this.player);
            if(properties != null) {
                this.action.execute(properties.getChargeConfiguration());
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

}
