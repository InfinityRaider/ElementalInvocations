package com.infinityraider.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IMagicCharge;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAddCharge extends MessageBase<IMessage> {
    private Element element;
    private int level;
    private EntityPlayer player;

    public MessageAddCharge() {}

    public MessageAddCharge(EntityPlayer player, IMagicCharge charge) {
        this();
        this.element = charge.element();
        this.level = charge.potency();
        this.player = player;
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
                properties.getChargeConfiguration().addCharge(new IMagicCharge() {
                    @Override
                    public Element element() {
                        return element;
                    }

                    @Override
                    public int potency() {
                        return level;
                    }
                });
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
