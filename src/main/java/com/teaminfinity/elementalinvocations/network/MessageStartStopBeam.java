package com.teaminfinity.elementalinvocations.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.teaminfinity.elementalinvocations.magic.spell.BeamHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageStartStopBeam extends MessageBase<IMessage> {
    private EntityPlayer player;
    private int[] potencies;
    private int channelTick;
    private double range;
    private boolean start;

    public MessageStartStopBeam() {
        super();
    }

    public MessageStartStopBeam(EntityPlayer player, int[] potencies, double range) {
        this(player, potencies, 0, range, true);
    }

    public MessageStartStopBeam(EntityPlayer player, int[] potencies, int channelTick) {
        this(player, potencies, channelTick, 0, false);
    }

    public MessageStartStopBeam(EntityPlayer player, int[] potencies, int channelTick, double range, boolean start) {
        this();
        this.player = player;
        this.potencies = potencies;
        this.channelTick = channelTick;
        this.range = range;
        this.start = start;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.player != null) {
            if(this.start) {
                BeamHandler.getInstance().startBeam(this.player, this.potencies, this.range);
            } else {
                BeamHandler.getInstance().stopBeam(this.player, this.channelTick);
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
        this.potencies = this.readIntArrayFromByteBuf(buf);
        this.channelTick = buf.readInt();
        this.range = buf.readDouble();
        this.start = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writePlayerToByteBuf(buf, this.player);
        this.writeIntArrayToByteBuf(buf, this.potencies);
        buf.writeInt(this.channelTick);
        buf.writeDouble(this.range);
        buf.writeBoolean(this.start);
    }
}
