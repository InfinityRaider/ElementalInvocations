package com.infinityraider.elementalinvocations.proxy;

import com.infinityraider.elementalinvocations.handler.KeyInputHandler;
import com.infinityraider.elementalinvocations.handler.PlayerMovementTrackingHandler;
import com.infinityraider.elementalinvocations.potion.PotionConfusion;
import com.infinityraider.elementalinvocations.render.entity.RenderHexedHandler;
import com.infinityraider.elementalinvocations.render.player.RenderLayerPotionEffect;
import com.infinityraider.elementalinvocations.render.player.RenderThirdPersonHandler;
import com.infinityraider.elementalinvocations.render.player.RenderFirstPersonHandler;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.infinityraider.elementalinvocations.utility.KeyBindings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy implements IProxy, IClientProxyBase {
    @Override
    public void initEnd(FMLInitializationEvent event) {
        RenderLayerPotionEffect.init();
    }

    @Override
    public void registerKeyBindings() {
        KeyBindings.register();
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        this.registerEventHandler(KeyInputHandler.getInstance());
        this.registerEventHandler(RenderFirstPersonHandler.getInstance());
        this.registerEventHandler(RenderHexedHandler.getInstance());
        this.registerEventHandler(RenderThirdPersonHandler.getInstance());
        this.registerEventHandler(PlayerMovementTrackingHandler.getInstance());
    }

    @Override
    public boolean isShiftKeyPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    @Override
    public void performConfusionEffect(PotionConfusion potion, EntityLivingBase entity, int amplification) {
        potion.performClientEffect(entity);
    }
}
