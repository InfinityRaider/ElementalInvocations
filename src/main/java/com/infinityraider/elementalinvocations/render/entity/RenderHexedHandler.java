package com.infinityraider.elementalinvocations.render.entity;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.GrabProtectedRenderDataHack;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class RenderHexedHandler {
    private static final RenderHexedHandler INSTANCE = new RenderHexedHandler();

    private Set<Integer> hexedEntities;

    public static RenderHexedHandler getInstance() {
        return INSTANCE;
    }

    private final Logger logger = LogManager.getLogger();

    private ResourceLocation chickenTexture = new ResourceLocation("textures/entity/chicken.png");
    private ModelChicken chickenModel = new ModelChicken();

    private RenderHexedHandler() {
        this.hexedEntities = Sets.newHashSet();
    }

    public void addHexedEntity(EntityLivingBase entity) {
        this.hexedEntities.add(entity.getEntityId());
    }

    public void removeHexedEntity(int id) {
        this.hexedEntities.remove(id);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderLiving(RenderLivingEvent.Pre event) {
        EntityLivingBase entity = event.getEntity();
        if(hexedEntities.contains(entity.getEntityId())) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            this.renderChicken(event.getRenderer(), entity, event.getX(), event.getY(), event.getZ(), Minecraft.getMinecraft().getRenderPartialTicks());
        }
    }

    //Mostly copied from vanilla at this point
    @SuppressWarnings("unchecked")
    protected void renderChicken(RenderLivingBase renderer, EntityLivingBase entity, double x, double y, double z, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.chickenModel.swingProgress = entity.getSwingProgress(partialTicks);
        boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        this.chickenModel.isRiding = shouldSit;
        this.chickenModel.isChild = entity.isChild();
        try {
            float renderYawOffset = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float rotationYawHead = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float yawHead = rotationYawHead - renderYawOffset;
            if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                renderYawOffset = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                yawHead = rotationYawHead - renderYawOffset;
                float yawHeadWrapped = MathHelper.wrapDegrees(yawHead);
                if (yawHeadWrapped < -85.0F) {
                    yawHeadWrapped = -85.0F;
                }
                if (yawHeadWrapped >= 85.0F) {
                    yawHeadWrapped = 85.0F;
                }
                renderYawOffset = rotationYawHead - yawHeadWrapped;
                if (yawHeadWrapped * yawHeadWrapped > 2500.0F) {
                    renderYawOffset += yawHeadWrapped * 0.2F;
                }
            }
            float rotationPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            GlStateManager.translate(x, y, z);
            float timeExisted = entity.ticksExisted + partialTicks;
            this.rotateCorpse(entity, timeExisted, renderYawOffset, partialTicks);
            float f4 = this.prepareScale(entity, partialTicks);
            float f5 = 0.0F;
            float f6 = 0.0F;
            if (!entity.isRiding()) {
                f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
                if (entity.isChild()) {
                    f6 *= 3.0F;
                }
                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }
            }
            GlStateManager.enableAlpha();
            this.chickenModel.setLivingAnimations(entity, f6, f5, partialTicks);
            this.chickenModel.setRotationAngles(f6, f5, timeExisted, yawHead, rotationPitch, f4, entity);

            if (GrabProtectedRenderDataHack.getRenderOutlines(renderer)) {
                boolean flag1 = GrabProtectedRenderDataHack.setScoreTeamColor(renderer, entity);
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(GrabProtectedRenderDataHack.getTeamColor(renderer, entity));
                if (!GrabProtectedRenderDataHack.getRenderMarker(renderer)) {
                    this.renderModel(renderer, entity, f6, f5, timeExisted, yawHead, rotationPitch, f4);
                }
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
                if (flag1) {
                    GrabProtectedRenderDataHack.unsetScoreTeamColor(renderer);
                }
            } else {
                boolean flag = GrabProtectedRenderDataHack.setDoRenderBrightness(renderer, entity, partialTicks);
                this.renderModel(renderer, entity, f6, f5, timeExisted, yawHead, rotationPitch, f4);
                if (flag) {
                    GrabProtectedRenderDataHack.unsetBrightness(renderer);
                }
                GlStateManager.depthMask(true);
            }
            GlStateManager.disableRescaleNormal();
        }
        catch (Exception exception) {
            logger.error("Couldn\'t render entity as pig", exception);
        }
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        if (!GrabProtectedRenderDataHack.getRenderOutlines(renderer)) {
            GrabProtectedRenderDataHack.renderName(renderer, entity, x, y, z);
        }
    }

    protected void renderModel(RenderLivingBase renderer, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        boolean doRender = !entity.isInvisible() || GrabProtectedRenderDataHack.getRenderOutlines(renderer);
        boolean renderGhostly = !doRender && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);
        if (doRender || renderGhostly) {
            renderer.getRenderManager().renderEngine.bindTexture(this.chickenTexture);
            if (renderGhostly) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            this.chickenModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (renderGhostly) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {}
        while (f >= 180.0F) {
            f -= 360.0F;
        }
        return prevYawOffset + partialTicks * f;
    }

    protected void rotateCorpse(EntityLivingBase entity, float timeExisted, float renderYawOffset, float partialTicks) {
        GlStateManager.rotate(180.0F - renderYawOffset, 0.0F, 1.0F, 0.0F);
        if (entity.deathTime > 0) {
            float f = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            GlStateManager.rotate(f * 90.0F, 0.0F, 0.0F, 1.0F);
        } else {
            String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());
            if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entity instanceof EntityPlayer) || ((EntityPlayer)entity).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0F, entity.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    protected float prepareScale(EntityLivingBase entity, float partialTicks) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.translate(0.0F, -1.501F, 0.0F);
        return 0.0625F;
    }
}
