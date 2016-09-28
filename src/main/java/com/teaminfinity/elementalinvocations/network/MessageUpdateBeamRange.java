package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.magic.spell.BeamHandler;
import com.teaminfinity.elementalinvocations.magic.spell.MagicBeam;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Optional;

public class MessageUpdateBeamRange extends MessageBase<IMessage> {
    private EntityPlayer player;
    private double range;

    public MessageUpdateBeamRange() {
        super();
    }

    public MessageUpdateBeamRange(EntityPlayer player, double range) {
        this();
        this.player = player;
        this.range = range;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.player != null) {
            Optional<MagicBeam> beam = BeamHandler.getInstance().getMagicBeam(this.player);
            if(beam.isPresent()) {
                beam.get().setRange(this.range);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = this.readPlayerFromByteBuf(buf);
        this.range = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writePlayerToByteBuf(buf, this.player);
        buf.writeDouble(this.range);
    }
}
