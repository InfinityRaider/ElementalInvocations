package com.infinityraider.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.utility.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSetPlayerPosition extends MessageBase<IMessage> {
    private double x;
    private double y;
    private double z;

    public MessageSetPlayerPosition() {
        super();
    }

    public MessageSetPlayerPosition(double x, double y, double z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        EntityPlayer player = ElementalInvocations.proxy.getClientPlayer();
        if (player != null) {
            PlayerHelper.setPlayerPosition(player, this.x, this.y, this.z);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
