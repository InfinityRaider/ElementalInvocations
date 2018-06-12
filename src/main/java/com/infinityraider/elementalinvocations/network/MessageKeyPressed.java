package com.infinityraider.elementalinvocations.network;

import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.elementalinvocations.handler.SpellCastingHandler;
import com.infinityraider.elementalinvocations.utility.KeyBindings;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageKeyPressed extends MessageBase<IMessage> {
    private int buttonId;

    public MessageKeyPressed() {
        super();
    }

    public MessageKeyPressed(int id) {
        this();
        this.buttonId = id;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        if (player != null) {
            switch (this.buttonId) {
                case KeyBindings.KEY_INVOKE:
                    IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(player);
                    if (properties != null) {
                        properties.getChargeConfiguration().invoke();
                    }
                    break;
                case KeyBindings.KEY_SPELL_ACTION:
                    SpellCastingHandler.getInstance().onSpellAction(player);
                    break;
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
