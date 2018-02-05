package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.elementalinvocations.reference.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderInstability {
    private static final RenderInstability INSTANCE = new RenderInstability();

    public static RenderInstability getInstance() {
        return INSTANCE;
    }

    private RenderInstability() {}

    public void renderInstability(double z) {
        EntityPlayer player = ElementalInvocations.proxy.getClientPlayer();
        if(player == null) {
            return;
        }
        IPlayerMagicProperties props = CapabilityPlayerMagicProperties.getMagicProperties(player);
        if(props == null) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for(int i = 0; i < Element.values().length; i++) {
            Element first = Element.values()[i];
            Element second = Element.values()[(i + 1)%Element.values().length];

            double x1 = first.calculateX(((double) props.getPlayerAdeptness(first)* Constants.NOMINAL_ORBS*Constants.CORE_TIERS)/Constants.MAX_LEVEL);
            double y1 = first.calculateY(((double) props.getPlayerAdeptness(first)* Constants.NOMINAL_ORBS*Constants.CORE_TIERS)/Constants.MAX_LEVEL);
            double dx = second.calculateX(((double) props.getPlayerAdeptness(second)* Constants.NOMINAL_ORBS*Constants.CORE_TIERS)/Constants.MAX_LEVEL) - x1;
            double dy = second.calculateY(((double) props.getPlayerAdeptness(second)* Constants.NOMINAL_ORBS*Constants.CORE_TIERS)/Constants.MAX_LEVEL) - y1;

            int r = first.getRed();
            int g = first.getGreen();
            int b = first.getBlue();
            int dr = second.getRed() - r;
            int dg = second.getGreen() - g;
            int db = second.getBlue() - b;

            int nodes = 10;
            GL11.glLineWidth(3);

            //Connecting line between two elements
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for(int j = 0; j <= nodes; j++) {
                buffer.pos(x1 +  (dx*j)/nodes, y1 + (dy*j)/nodes, z).color(r + dr*j/nodes, g + dg*j/nodes, b + db*j/nodes, 255).endVertex();
            }
            tessellator.draw();

            //line from origin to element
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for(int j = 0; j <= nodes; j++) {
                buffer.pos((x1*j)/nodes, (y1*j)/nodes, z).color(255 - (255-r)*j/nodes, 255 - (255-g)*j/nodes, 255 - (255-b)*j/nodes, 255).endVertex();
            }
            tessellator.draw();
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

}