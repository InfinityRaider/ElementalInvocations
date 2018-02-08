package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.magic.spell.MagicBeam;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.RenderUtilBase;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.joml.Matrix4d;
import org.joml.Vector4d;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BeamRenderer extends RenderUtilBase {
    private static final BeamRenderer INSTANCE = new BeamRenderer();

    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/beam.png");
    public static final ResourceLocation ORB_TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/beam_orb.png");

    public static BeamRenderer getInstance() {
        return INSTANCE;
    }

    private BeamRenderer() {}

    public void renderBeamThirdPerson(MagicBeam beam, float partialTick) {
        EntityPlayer player = beam.getPlayer();

        double thickness = beam.getThickness();

        float red = beam.getRed();
        float green = beam.getGreen();
        float blue = beam.getBlue();
        float alpha = 0.5F;

        double range = beam.getRange();
        RayTraceResult hit = RayTraceHelper.getTargetEntityOrBlock(player, range);
        Vec3d target;
        if(hit == null) {
            target = player.getPositionEyes(partialTick).add(player.getLook(partialTick).scale(range));
        } else {
            target = hit.hitVec;
        }

        double pX = player.prevPosX + partialTick*(player.posX - player.prevPosX);
        double pY = player.prevPosY + partialTick*(player.posY - player.prevPosY);
        double pZ = player.prevPosZ + partialTick*(player.posZ - player.prevPosZ);

        double yaw = player.prevRenderYawOffset + partialTick*(player.renderYawOffset - player.prevRenderYawOffset);
        double cos = Math.cos(Math.toRadians(yaw));
        double sin = Math.sin(Math.toRadians(yaw));

        double dx = 0;
        double dz = 0.5;

        double x1 = dx*cos - dz*sin;
        double y1 = 0.65*player.height;
        double z1 = dx*sin + dz*cos;

        double x2 = target.xCoord - pX;
        double y2 = target.yCoord - pY;
        double z2 = target.zCoord - pZ;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        //render the beam
        this.renderBeam(x1, y1, z1, x2, y2, z2, thickness, red, green, blue, alpha);

        //render the orb
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float) -yaw, 0, 1, 0);
        GlStateManager.translate(dx, y1, dz);
        this.renderOrb(red, green, blue, alpha);
        GlStateManager.popMatrix();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public void renderBeamFirstPerson(MagicBeam beam, float partialTicks) {
        EntityPlayer player = beam.getPlayer();
        double thickness = 2.0 / 16;

        float red = beam.getRed();
        float green = beam.getGreen();
        float blue = beam.getBlue();
        int alpha = 127;

        double range = beam.getRange();
        RayTraceResult hit = RayTraceHelper.getTargetEntityOrBlock(player, range);
        Vec3d target;
        if (hit == null) {
            target = player.getPositionEyes(partialTicks).add(player.getLook(partialTicks).scale(range));
        } else {
            target = hit.hitVec;
        }

        double pX = player.prevPosX + partialTicks*(player.posX - player.prevPosX);
        double pY = player.prevPosY + partialTicks*(player.posY - player.prevPosY);
        double pZ = player.prevPosZ + partialTicks*(player.posZ - player.prevPosZ);

        Vec3d eyes = player.getPositionEyes(partialTicks);

        double pitch = player.prevRotationPitch + partialTicks*(player.rotationPitch - player.prevRotationPitch);
        double yaw = player.prevRotationYaw + partialTicks*(player.rotationYaw - player.prevRotationYaw);

        //parallel with screen width
        double dx = -0.25;
        //parallel with screen height
        double dy = 0.95*player.height;
        //parallel with screen normal
        double dz = -0.25;

        Matrix4d matrix = new Matrix4d().identity().rotate(Math.toRadians(-yaw), 0, 1, 0).rotate(Math.toRadians(-pitch), 1, 0, 0);
        Vector4d transformed = matrix.transform(new Vector4d(dx, dy, dz, 0));

        double x1 = pX + transformed.x();
        double y1 = pY + transformed.y();
        double z1 = pZ + transformed.z();

        double x2 = target.xCoord;
        double y2 = target.yCoord;
        double z2 = target.zCoord;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        GlStateManager.rotate((float) pitch, 1, 0, 0);
        GlStateManager.rotate((float) yaw + 180, 0, 1, 0);
        GlStateManager.translate(-eyes.xCoord, -eyes.yCoord, -eyes.zCoord);

        this.renderBeam(x1, y1, z1, x2, y2, z2, thickness, red, green, blue, alpha);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    protected void renderBeam(double x1, double y1, double z1, double x2, double y2, double z2, double thickness, float red, float green, float blue, float alpha) {
        float angle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        angle = (3 * angle) % 360;
        double dt = 1.0 - 0.5*Math.cos(Math.toRadians(angle));

        Minecraft.getMinecraft().renderEngine.bindTexture(BEAM_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        buffer.pos(x2, y2 + dt*thickness/2, z2).tex(1, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 + dt*thickness/2, z1).tex(1, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 - dt*thickness/2, z1).tex(0, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2 - dt*thickness/2, z2).tex(0, 0).color(red, green, blue, alpha).endVertex();

        buffer.pos(x2, y2 + dt*thickness/2, z2).tex(1, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2 - dt*thickness/2, z2).tex(0, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 - dt*thickness/2, z1).tex(0, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 + dt*thickness/2, z1).tex(1, 1).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
    }

    protected void renderOrb(float red, float green, float blue, float alpha) {
        float angle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        angle = (3 * angle) % 360;
        GlStateManager.rotate(angle, 0, 0, 1);
        double scale = 0.75 - 0.25*(Math.cos(Math.toRadians(angle)) + 1)/2;
        GlStateManager.scale(scale, scale, scale);

        Minecraft.getMinecraft().renderEngine.bindTexture(ORB_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        buffer.pos(-0.5, 0.5, 0).tex(1, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(0.5, 0.5, 0).tex(1, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(0.5, -0.5, 0).tex(0, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(-0.5, -0.5, 0).tex(0, 0).color(red, green, blue, alpha).endVertex();

        buffer.pos(-0.5, 0.5, 0).tex(1, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(-0.5, -0.5, 0).tex(0, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(0.5, -0.5, 0).tex(0, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(0.5, 0.5, 0).tex(1, 1).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
    }
}