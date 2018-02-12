package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.magic.spell.MagicBeam;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.RenderUtilBase;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import com.infinityraider.infinitylib.render.tessellation.TessellatorVertexBuffer;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.joml.Matrix4f;
import org.joml.Vector4f;
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

        float thickness = beam.getThickness();

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

        float x1 = (float) (dx*cos - dz*sin);
        float y1 = (float) (0.65*player.height);
        float z1 = (float) (dx*sin + dz*cos);

        float x2 = (float) (target.xCoord - pX);
        float y2 = (float) (target.yCoord - pY);
        float z2 = (float) (target.zCoord - pZ);

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
        float thickness = 2.0F / 16;

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

        float pX = (float) (player.prevPosX + partialTicks*(player.posX - player.prevPosX));
        float pY = (float) (player.prevPosY + partialTicks*(player.posY - player.prevPosY));
        float pZ = (float) (player.prevPosZ + partialTicks*(player.posZ - player.prevPosZ));

        Vec3d eyes = player.getPositionEyes(partialTicks);

        float pitch = player.prevRotationPitch + partialTicks*(player.rotationPitch - player.prevRotationPitch);
        float yaw = player.prevRotationYaw + partialTicks*(player.rotationYaw - player.prevRotationYaw);

        //parallel with screen width
        float dx = -0.25F;
        //parallel with screen height
        float dy = 0.95F*player.height;
        //parallel with screen normal
        float dz = -0.25F;

        Matrix4f matrix = new Matrix4f().identity().rotate((float) Math.toRadians(-yaw), 0, 1, 0).rotate((float) Math.toRadians(-pitch), 1, 0, 0);
        Vector4f transformed = matrix.transform(new Vector4f(dx, dy, dz, 0));

        float x1 = pX + transformed.x();
        float y1 = pY + transformed.y();
        float z1 = pZ + transformed.z();

        float x2 = (float) (target.xCoord);
        float y2 = (float) (target.yCoord);
        float z2 = (float) (target.zCoord);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        GlStateManager.rotate(pitch, 1, 0, 0);
        GlStateManager.rotate(yaw + 180, 0, 1, 0);
        GlStateManager.translate(-eyes.xCoord, -eyes.yCoord, -eyes.zCoord);

        this.renderBeam(x1, y1, z1, x2, y2, z2, thickness, red, green, blue, alpha);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    protected void renderBeam(float x1, float y1, float z1, float x2, float y2, float z2, float thickness, float red, float green, float blue, float alpha) {
        float angle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        angle = (3 * angle) % 360;
        float dt = 1.0F - 0.5F*((float) Math.cos(Math.toRadians(angle)));

        ITessellator tessellator = TessellatorVertexBuffer.getInstance();

        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.bindTexture(BEAM_TEXTURE);
        tessellator.setColorRGBA(red, green, blue, alpha);

        tessellator.addVertexWithUV(x2, y2 + dt*thickness/2, z2, 1, 0);
        tessellator.addVertexWithUV(x1, y1 + dt*thickness/2, z1, 1, 1);
        tessellator.addVertexWithUV(x1, y1 - dt*thickness/2, z1, 0, 1);
        tessellator.addVertexWithUV(x2, y2 - dt*thickness/2, z2, 0, 0);

        tessellator.addVertexWithUV(x2, y2 + dt*thickness/2, z2, 1, 0);
        tessellator.addVertexWithUV(x2, y2 - dt*thickness/2, z2, 0, 0);
        tessellator.addVertexWithUV(x1, y1 - dt*thickness/2, z1, 0, 1);
        tessellator.addVertexWithUV(x1, y1 + dt*thickness/2, z1, 1, 1);

        tessellator.draw();
    }

    protected void renderOrb(float red, float green, float blue, float alpha) {
        float angle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        angle = (3 * angle) % 360;
        GlStateManager.rotate(angle, 0, 0, 1);
        double scale = 0.75 - 0.25*(Math.cos(Math.toRadians(angle)) + 1)/2;
        GlStateManager.scale(scale, scale, scale);

        ITessellator tessellator = TessellatorVertexBuffer.getInstance();
        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.bindTexture(ORB_TEXTURE);
        tessellator.setColorRGBA(red, green, blue, alpha);

        tessellator.addVertexWithUV(-0.5F, 0.5F, 0, 1, 0);
        tessellator.addVertexWithUV(0.5F, 0.5F, 0, 1, 1);
        tessellator.addVertexWithUV(0.5F, -0.5F, 0, 0, 1);
        tessellator.addVertexWithUV(-0.5F, -0.5F, 0, 0, 0);

        tessellator.addVertexWithUV(-0.5F, 0.5F, 0, 1, 0);
        tessellator.addVertexWithUV(-0.5F, -0.5F, 0, 0, 0);
        tessellator.addVertexWithUV(0.5F, -0.5F, 0, 0, 1);
        tessellator.addVertexWithUV(0.5F, 0.5F, 0, 1, 1);

        tessellator.draw();
    }
}