package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityReplicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

public class RenderEntityReplicate extends RenderLivingBase<EntityReplicate> {
    public RenderEntityReplicate(RenderManager renderManager) {
        super(renderManager, new ModelPlayer(0.0F, true), 0.5F);
    }

    @Override
    public void doRender(EntityReplicate replica, double x, double y, double z, float entityYaw, float partialTicks) {
        AbstractClientPlayer player = replica.getClientPlayer();
        Render<? extends AbstractClientPlayer> renderer = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(player);
        if(renderer instanceof RenderPlayer) {
            RenderPlayer renderPlayer = (RenderPlayer) renderer;
            renderPlayer.doRender(player, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    public ModelPlayer getMainModel() {
        return (ModelPlayer)super.getMainModel();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityReplicate entity) {
        AbstractClientPlayer player = entity.getClientPlayer();
        return player.getLocationSkin();
    }
}
