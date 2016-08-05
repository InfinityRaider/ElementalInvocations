package com.teaminfinity.elementalinvocations.render.player;

import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Reference;
import com.teaminfinity.elementalinvocations.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public final class RenderPlayerCharges extends RenderUtil {
    private static final RenderPlayerCharges INSTANCE = new RenderPlayerCharges();

    public static RenderPlayerCharges getInstance() {
        return INSTANCE;
    }

    private static final int MAX_BLURS = 5;
    private static final double RADIUS = 1.0;

    private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/charge_fire.png");
    private static final ResourceLocation TEXTURE_WATER = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/charge_water.png");
    private static final ResourceLocation TEXTURE_AIR = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/charge_air.png");
    private static final ResourceLocation TEXTURE_EARTH = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/charge_earth.png");
    private static final ResourceLocation TEXTURE_DEATH = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/charge_death.png");
    private static final ResourceLocation TEXTURE_LIFE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/charge_life.png");

    private static final ResourceLocation[] CHARGE_TEXTURES = new ResourceLocation[] {
            TEXTURE_FIRE,
            TEXTURE_WATER,
            TEXTURE_AIR,
            TEXTURE_EARTH,
            TEXTURE_DEATH,
            TEXTURE_LIFE
    };

    private RenderPlayerCharges() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void renderPlayerCharges(RenderPlayerEvent.Post event) {
        IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(event.getEntityPlayer());
        if(properties == null) {
            return;
        }

        List<IMagicCharge> chargeList = properties.getCharges();
        if(chargeList == null ||chargeList.size() <= 0) {
            return;
        }

        GlStateManager.pushMatrix();

        float newAngle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        for(int orb = 0; orb < chargeList.size(); orb++) {
            //GlStateManager.pushAttrib();

            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

            for(int blur = 0; blur < MAX_BLURS; blur++) {
                float[] position = calculateOrbPosition(newAngle, orb, chargeList.size(), blur);
                renderCharge(chargeList.get(orb), position[0], position[1], position[2], blur);
            }

            //GlStateManager.popAttrib();
        }

        GlStateManager.popMatrix();
    }

    private float[] calculateOrbPosition(float newAngle, int orbNumber, int totalOrbs, int blurIndex) {
        int orbitNumber = orbNumber % 3;
        int orbIndex = orbNumber / 3;

        //calculate trajectories
        float angle = (10 * (newAngle + 360 - (2.5F * blurIndex) / MAX_BLURS)) % 360;
        double tilt = Math.toRadians(45);

        float[] position = new float[3];
        switch(orbitNumber) {
            case 0:
                double a = Math.toRadians(angle - 120 + (360 * orbIndex / getOrbsPerOrbit(orbitNumber, totalOrbs)));
                position[0] = (float) (RADIUS * Math.cos(a));
                position[1] = 0;
                position[2] = (float) (RADIUS * Math.sin(a));
                break;
            case 1:
                double b = Math.toRadians(-angle + 120 + (360 * orbIndex / getOrbsPerOrbit(orbitNumber, totalOrbs)));
                position[0] = (float) (-RADIUS * Math.sin(b) * Math.sin(tilt));
                position[1] = (float) (RADIUS * Math.sin(b) * Math.cos(tilt));
                position[2] = (float) (RADIUS * Math.cos(b));
                break;
            case 2:
                double c = Math.toRadians(angle + (360 * orbIndex / getOrbsPerOrbit(orbitNumber, totalOrbs)));
                position[0] = (float) (-RADIUS * Math.sin(c) * Math.sin(-tilt));
                position[1] = (float) (RADIUS * Math.sin(c) * Math.cos(-tilt));
                position[2] = (float) (RADIUS * Math.cos(c));
                break;
        }
        return position;
    }

    private int getOrbsPerOrbit(int orbitNumber, int totalOrbs) {
        if(totalOrbs <= orbitNumber) {
            return 0;
        }
        switch(orbitNumber) {
            case 0: return 1 + (totalOrbs - 1) / 3;
            case 1:return 1 + (totalOrbs - 2) / 3;
            case 2: return totalOrbs / 3;
        }
        return 0;
    }

    private void renderCharge(IMagicCharge charge, float x, float y, float z, int blurIndex) {
        Minecraft.getMinecraft().renderEngine.bindTexture(CHARGE_TEXTURES[charge.element().ordinal()]);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();

        //configure GL settings
        GlStateManager.color(1F, 1F, 1F, 0.7F*(1.0F - (blurIndex + 0.0F)/MAX_BLURS));

        //translate to the orb position
        GlStateManager.translate(x, y + 1, z);

        //rotate so the texture always renders parallel to the screen
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(-renderManager.playerViewX, 1, 0, 0);

        float u = Constants.UNIT;
        float scale = 0.375F*(1.0F - 0.25F*(blurIndex+0.0F)/MAX_BLURS) * (0.6F + (0.5F * charge.level())/Constants.CORE_TIERS);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(-8 * scale * u, 0, 0).tex(1, 1).endVertex();
        buffer.pos(-8 * scale * u, 16 * scale * u, 0).tex(1, 0).endVertex();
        buffer.pos(8 * scale * u, 16 * scale * u, 0).tex(0, 0).endVertex();
        buffer.pos(8 * scale * u, 0, 0).tex(0, 1).endVertex();

        tessellator.draw();

        GlStateManager.popMatrix();
    }

}
