package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.magic.spell.BeamHandler;
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
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.joml.Matrix4d;
import org.joml.Vector4d;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

@SideOnly(Side.CLIENT)
public class RenderBeam extends RenderUtilBase {
    private static final RenderBeam INSTANCE = new RenderBeam();

    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/beam.png");
    public static final ResourceLocation ORB_TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/beam_orb.png");

    public static RenderBeam getInstance() {
        return INSTANCE;
    }

    private RenderBeam() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        Optional<MagicBeam> optional = BeamHandler.getInstance().getMagicBeam(player);
        if(!optional.isPresent()) {
            return;
        }

        MagicBeam beam = optional.get();

        double thickness = beam.getThickness();

        int red = beam.getRed();
        int green = beam.getGreen();
        int blue = beam.getBlue();
        int alpha = 127;

        double range = beam.getRange();
        RayTraceResult hit = RayTraceHelper.getTargetEntityOrBlock(player, range);
        Vec3d target;
        if(hit == null) {
            target = player.getPositionEyes(event.getPartialRenderTick()).add(player.getLook(event.getPartialRenderTick()).scale(range));
        } else {
            target = hit.hitVec;
        }

        double pX = player.prevPosX + event.getPartialRenderTick()*(player.posX - player.prevPosX);
        double pY = player.prevPosY + event.getPartialRenderTick()*(player.posY - player.prevPosY);
        double pZ = player.prevPosZ + event.getPartialRenderTick()*(player.posZ - player.prevPosZ);

        double yaw = player.prevRenderYawOffset + event.getPartialRenderTick()*(player.renderYawOffset - player.prevRenderYawOffset);
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

        GlStateManager.translate(event.getX(), event.getY(), event.getZ());

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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onRenderHand(RenderHandEvent event) {
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            return;
        }
        EntityPlayer player = ElementalInvocations.proxy.getClientPlayer();
        Optional<MagicBeam> optional = BeamHandler.getInstance().getMagicBeam(player);
        if (!optional.isPresent()) {
            return;
        }

        MagicBeam beam = optional.get();

        double thickness = 2.0 / 16;

        int red = beam.getRed();
        int green = beam.getGreen();
        int blue = beam.getBlue();
        int alpha = 127;

        double range = beam.getRange();
        RayTraceResult hit = RayTraceHelper.getTargetEntityOrBlock(player, range);
        Vec3d target;
        if (hit == null) {
            target = player.getPositionEyes(event.getPartialTicks()).add(player.getLook(event.getPartialTicks()).scale(range));
        } else {
            target = hit.hitVec;
        }

        double pX = player.prevPosX + event.getPartialTicks()*(player.posX - player.prevPosX);
        double pY = player.prevPosY + event.getPartialTicks()*(player.posY - player.prevPosY);
        double pZ = player.prevPosZ + event.getPartialTicks()*(player.posZ - player.prevPosZ);

        Vec3d eyes = player.getPositionEyes(event.getPartialTicks());

        double pitch = player.prevRotationPitch + event.getPartialTicks()*(player.rotationPitch - player.prevRotationPitch);
        double cosPitch = Math.cos(Math.toRadians(pitch));
        double sinPitch = Math.sin(Math.toRadians(pitch));

        double yaw = player.prevRotationYaw + event.getPartialTicks()*(player.rotationYaw - player.prevRotationYaw);
        double cosYaw = Math.cos(Math.toRadians(yaw));
        double sinYaw = Math.sin(Math.toRadians(yaw));

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

    protected void renderBeam(double x1, double y1, double z1, double x2, double y2, double z2, double thickness, int red, int green, int blue, int alpha) {
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

    protected void renderOrb(int red, int green, int blue, int alpha) {
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