package com.teaminfinity.elementalinvocations.render.player;

import com.infinityraider.infinitylib.render.RenderUtilBase;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.teaminfinity.elementalinvocations.magic.spell.BeamHandler;
import com.teaminfinity.elementalinvocations.magic.spell.MagicBeam;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

@SideOnly(Side.CLIENT)
public class RenderBeam extends RenderUtilBase {
    private static final RenderBeam INSTANCE = new RenderBeam();

    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "entities/beam.png");

    public static RenderBeam getInstance() {
        return INSTANCE;
    }

    private RenderBeam() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void renderPlayerCharges(RenderPlayerEvent.Post event) {
        Optional<MagicBeam> optional = BeamHandler.getInstance().getMagicBeam(event.getEntityPlayer());
        if(!optional.isPresent()) {
            return;
        }

        MagicBeam beam = optional.get();

        double thickness = 1.0/16;

        int red = beam.getRed();
        int green = beam.getGreen();
        int blue = beam.getBlue();
        int alpha = 127;

        double range = beam.getRange();
        EntityPlayer player = event.getEntityPlayer();
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

        double x1 = 0;
        double y1 = (0.875)*player.height;
        double z1 = 0;

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

        Minecraft.getMinecraft().renderEngine.bindTexture(BEAM_TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        buffer.pos(x2, y2 + thickness/2, z2).tex(1, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 + thickness/2, z1).tex(1, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 - thickness/2, z1).tex(0, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2 - thickness/2, z2).tex(0, 0).color(red, green, blue, alpha).endVertex();

        buffer.pos(x2, y2 + thickness/2, z2).tex(1, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2 - thickness/2, z2).tex(0, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 - thickness/2, z1).tex(0, 1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1 + thickness/2, z1).tex(1, 1).color(red, green, blue, alpha).endVertex();

        tessellator.draw();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}