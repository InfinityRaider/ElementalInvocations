package com.infinityraider.elementalinvocations.network;

import com.infinityraider.elementalinvocations.handler.EnchantingHandler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateGuiEnchantment extends MessageBase<IMessage> {
    private BlockPos pos;
    private boolean needsReq;
    private boolean reqsMet;

    public MessageUpdateGuiEnchantment() {
        super();
    }

    public MessageUpdateGuiEnchantment(BlockPos pos, boolean needsReq, boolean reqsMet) {
        this();
        this.pos = pos;
        this.needsReq = needsReq;
        this.reqsMet = reqsMet;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        Gui gui = Minecraft.getMinecraft().currentScreen;
        if(gui instanceof EnchantingHandler.GuiEnchantOverride) {
            ((EnchantingHandler.GuiEnchantOverride) gui).updateStatus(this.pos, this.needsReq, this.reqsMet);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}