package com.teaminfinity.elementalinvocations.proxy;

import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.teaminfinity.elementalinvocations.handler.ConfigurationHandler;
import com.teaminfinity.elementalinvocations.handler.KeyInputHandler;
import com.teaminfinity.elementalinvocations.potion.PotionConfusion;
import com.teaminfinity.elementalinvocations.render.player.RenderPlayerCharges;
import com.teaminfinity.elementalinvocations.render.player.RenderPlayerHUD;
import com.teaminfinity.elementalinvocations.utility.KeyBindings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy implements IProxy, IClientProxyBase {
    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        IProxy.super.initConfiguration(event);
        ConfigurationHandler.getInstance().initClientConfigs(event);
    }

    @Override
    public void registerKeyBindings() {
        KeyBindings.register();
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        this.registerEventHandler(RenderPlayerCharges.getInstance());
        this.registerEventHandler(KeyInputHandler.getInstance());
        this.registerEventHandler(RenderPlayerHUD.getInstance());
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
