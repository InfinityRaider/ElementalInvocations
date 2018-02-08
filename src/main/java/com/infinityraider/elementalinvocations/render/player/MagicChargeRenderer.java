package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IMagicCharge;
import com.infinityraider.elementalinvocations.reference.Constants;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class MagicChargeRenderer {
    private static final MagicChargeRenderer INSTANCE = new MagicChargeRenderer();

    public static MagicChargeRenderer getInstance() {
        return INSTANCE;
    }

    private static final int MAX_BLURS = 5;
    private static final double RADIUS = 1.0;

    private static final ResourceLocation[] CHARGE_TEXTURES;

    public static ResourceLocation getTexture(Element element) {
        return CHARGE_TEXTURES[element.ordinal()];
    }

    private MagicChargeRenderer() {}

    public void renderChargesThirdPerson(List<IMagicCharge> charges) {
        renderCharges(charges, RADIUS, getCurrentAngle(), 1.0F);
    }

    public void renderInvokeThirdPerson(List<IMagicCharge> charges, int frame, int total, float partialTick) {
        if(frame <= 0) {
            return;
        }
        GlStateManager.pushMatrix();
        double deltaY = (1.0*(frame + partialTick))/total;
        GlStateManager.translate(0, deltaY, 0);
        double radius = RADIUS*Math.sqrt(2)*((double) total - frame - partialTick);
        renderCharges(charges, radius, getCurrentAngle(), 1.0F);
        GlStateManager.popMatrix();
    }

    public void renderFadeThirdPerson(List<IMagicCharge> charges, int frame, int total, float partialTick) {
        if(frame <= 0) {
            return;
        }
        float scale = (total - frame - partialTick) / total;
        renderCharges(charges, RADIUS, getCurrentAngle(), scale);
    }

    public void renderFizzleThirdPerson(List<IMagicCharge> charges, int frame, int total, float partialTick) {
        if(frame <= 0) {
            return;
        }
        float scale = 1 + 0.5F*(frame + partialTick)/total;
        double radius = RADIUS * Math.sin((1 + 3*(frame + partialTick)/total)*Math.PI/4);
        renderCharges(charges, radius, getCurrentAngle(), scale);
    }

    public void renderChargesFirstPerson(List<IMagicCharge> charges, ScaledResolution resolution) {
        if (!charges.isEmpty()) {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();

            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int index = 0;
            int row = 0;
            int perRow = (resolution.getScaledWidth() / (4 * 18));

            int x0 = resolution.getScaledWidth() / 2 + 103;
            int y0 = resolution.getScaledHeight() - 11;

            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

            for (IMagicCharge charge : charges) {
                Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(charge.element()));

                GlStateManager.pushMatrix();

                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

                float scale = (0.6F + (0.5F * charge.potency()) / Constants.CORE_TIERS);
                float angle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

                int xC = x0 + index * 18;
                int yC = y0 - row * 18;

                GlStateManager.translate(xC, yC, 0);
                GlStateManager.rotate(angle, 0, 0, 1);

                double xMin = -8 * scale;
                double xMax = 8 * scale;
                double yMin = -8 * scale;
                double yMax = 8 * scale;

                buffer.pos(xMin, yMin, 0).tex(0, 0).endVertex();
                buffer.pos(xMin, yMax, 0).tex(0, 1).endVertex();
                buffer.pos(xMax, yMax, 0).tex(1, 1).endVertex();
                buffer.pos(xMax, yMin, 0).tex(1, 0).endVertex();

                tessellator.draw();

                GlStateManager.popMatrix();

                index = index + 1;
                if (index > perRow) {
                    index = 0;
                    row = row + 1;
                }
            }

            GlStateManager.popAttrib();
            GlStateManager.popMatrix();

            Minecraft.getMinecraft().renderEngine.bindTexture(Gui.ICONS);
        }
    }

    public void renderInvokeFirstPerson(List<IMagicCharge> charges, int frame, int total, float partialTick, ScaledResolution resolution) {
        if(charges == null ||charges.size() <= 0) {
            return;
        }

    }

    public void renderFadeFirstPerson(List<IMagicCharge> charges, int frame, int total, float partialTick, ScaledResolution resolution) {
        if(charges == null ||charges.size() <= 0) {
            return;
        }

    }

    public void renderFizzleFirstPerson(List<IMagicCharge> charges, int frame, int total, float partialTick, ScaledResolution resolution) {
        if(charges == null ||charges.size() <= 0) {
            return;
        }

    }

    private double getCurrentAngle() {
        return 360.0 * (System.currentTimeMillis() & 0x3FFFL) / ((double) 0x3FFFL);
    }

    private double[] calculateOrbPosition(double radius, double newAngle, int orbNumber, int totalOrbs, int blurIndex) {
        int orbitNumber = orbNumber % 3;
        int orbIndex = orbNumber / 3;

        //calculate trajectories
        double angle = (10 * (newAngle + 360 - (2.5F * blurIndex) / MAX_BLURS)) % 360;
        double tilt = Math.toRadians(45);

        double[] position = new double[3];
        switch(orbitNumber) {
            case 0:
                double a = Math.toRadians(angle - 120 + (360 * orbIndex / getOrbsPerOrbit(orbitNumber, totalOrbs)));
                position[0] = radius * Math.cos(a);
                position[1] = 0;
                position[2] = radius * Math.sin(a);
                break;
            case 1:
                double b = Math.toRadians(-angle + 120 + (360 * orbIndex / getOrbsPerOrbit(orbitNumber, totalOrbs)));
                position[0] = -radius * Math.sin(b) * Math.sin(tilt);
                position[1] = radius * Math.sin(b) * Math.cos(tilt);
                position[2] = radius * Math.cos(b);
                break;
            case 2:
                double c = Math.toRadians(angle + (360 * orbIndex / getOrbsPerOrbit(orbitNumber, totalOrbs)));
                position[0] = -radius * Math.sin(c) * Math.sin(-tilt);
                position[1] = radius * Math.sin(c) * Math.cos(-tilt);
                position[2] = radius * Math.cos(c);
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

    private void renderCharges(List<IMagicCharge> charges, double radius, double angle, float scale) {
        if(charges == null ||charges.size() <= 0) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        for(int orb = 0; orb < charges.size(); orb++) {
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);
            for(int blur = 0; blur < MAX_BLURS; blur++) {
                double[] position = calculateOrbPosition(radius, angle, orb, charges.size(), blur);
                renderCharge(charges.get(orb), position[0], position[1], position[2], blur, scale);
            }
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private void renderCharge(IMagicCharge charge, double x, double y, double z, int blurIndex, float scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(charge.element()));
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();

        //configure GL settings
        GlStateManager.color(1F, 1F, 1F, 0.7F*(1.0F - (blurIndex + 0.0F)/MAX_BLURS));

        //translate to the orb position
        GlStateManager.translate(x, y + 1, z);

        //rotate so the texture always renders parallel to the screen
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        boolean invert = Minecraft.getMinecraft().gameSettings.thirdPersonView == 2;
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate((invert ? -1 : 1) * renderManager.playerViewX, 1, 0, 0);

        float u = Constants.UNIT;
        float f = scale * 0.375F*(1.0F - 0.25F*(blurIndex+0.0F)/MAX_BLURS) * (0.6F + (0.5F * charge.potency())/Constants.CORE_TIERS);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(-8 * f * u, 0, 0).tex(1, 1).endVertex();
        buffer.pos(-8 * f * u, 16 * f * u, 0).tex(1, 0).endVertex();
        buffer.pos(8 * f * u, 16 * f * u, 0).tex(0, 0).endVertex();
        buffer.pos(8 * f * u, 0, 0).tex(0, 1).endVertex();

        tessellator.draw();

        GlStateManager.popMatrix();
    }

    static {
        ResourceLocation[] textures = new ResourceLocation[Element.values().length];
        for(Element element : Element.values()) {
            textures[element.ordinal()] = new ResourceLocation(Reference.MOD_ID.toLowerCase(),
                    "textures/entities/player/charge_" + element.name().toLowerCase() + ".png");
        }
        CHARGE_TEXTURES = textures;
    }
}