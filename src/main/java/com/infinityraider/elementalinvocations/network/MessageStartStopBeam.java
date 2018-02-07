package com.infinityraider.elementalinvocations.network;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.magic.PotencyMap;
import com.infinityraider.elementalinvocations.magic.spell.BeamHandler;
import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.infinitylib.network.serialization.ByteBufUtil;
import com.infinityraider.infinitylib.network.serialization.IMessageReader;
import com.infinityraider.infinitylib.network.serialization.IMessageSerializer;
import com.infinityraider.infinitylib.network.serialization.IMessageWriter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class MessageStartStopBeam extends MessageBase<IMessage> {
    private EntityPlayer player;
    private IPotencyMap potencies;
    private int channelTick;
    private double range;
    private boolean start;

    public MessageStartStopBeam() {
        super();
    }

    public MessageStartStopBeam(EntityPlayer player, IPotencyMap potencies, double range) {
        this(player, potencies, 0, range, true);
    }

    public MessageStartStopBeam(EntityPlayer player, IPotencyMap potencies, int channelTick) {
        this(player, potencies, channelTick, 0, false);
    }

    public MessageStartStopBeam(EntityPlayer player, IPotencyMap potencies, int channelTick, double range, boolean start) {
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
        if(this.player != null) {
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
    public List<IMessageSerializer> getNecessarySerializers() {
        return ImmutableList.of(
                new IMessageSerializer<IPotencyMap>() {
                    @Override
                    public boolean accepts(Class clazz) {
                        return false;
                    }

                    @Override
                    public IMessageWriter<IPotencyMap> getWriter(Class<IPotencyMap> clazz) {
                        return (buf, data) -> ByteBufUtil.writeNBT(buf, data.writeToNBT());
                    }

                    @Override
                    public IMessageReader<IPotencyMap> getReader(Class<IPotencyMap> clazz) {
                        return (data) -> (new PotencyMap()).readFromNBT(ByteBufUtil.readNBT(data));
                    }
                }
        );
    }
}
