package com.teaminfinity.elementalinvocations.proxy;

import com.teaminfinity.elementalinvocations.ModBase;
import com.teaminfinity.elementalinvocations.handler.ArmSwingHandler;
import com.teaminfinity.elementalinvocations.handler.ConfigurationHandler;
import com.teaminfinity.elementalinvocations.handler.KeyInputHandler;
import com.teaminfinity.elementalinvocations.handler.MouseClickHandler;
import com.teaminfinity.elementalinvocations.potion.PotionConfusion;
import com.teaminfinity.elementalinvocations.proxy.base.IClientProxyBase;
import com.teaminfinity.elementalinvocations.render.model.ModelPlayerCustomized;
import com.teaminfinity.elementalinvocations.render.player.RenderPlayerCharges;
import com.teaminfinity.elementalinvocations.render.player.RenderPlayerHUD;
import com.teaminfinity.elementalinvocations.utility.KeyBindings;
import com.teaminfinity.elementalinvocations.utility.ModHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy implements IProxy, IClientProxyBase {
    @Override
    public void preInitStart(FMLPreInitializationEvent event) {
        IProxy.super.preInitStart(event);
        ConfigurationHandler.getInstance().initClientConfigs(event);
    }

    @Override
    public void registerRenderers(ModBase mod) {
        ModHelper.getInstance().initRenderers(mod);
    }

    @Override
    public void registerKeyBindings() {
        KeyBindings.register();
    }

    @Override
    public void registerEntities(ModBase mod) {
        ModHelper.getInstance().registerEntitiesClient(mod);
    }

    @Override
    public void overridePlayerModel() {
        ModelPlayerCustomized.replaceOldModel();
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(ArmSwingHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(MouseClickHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(RenderPlayerCharges.getInstance());
        MinecraftForge.EVENT_BUS.register(KeyInputHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(RenderPlayerHUD.getInstance());
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
