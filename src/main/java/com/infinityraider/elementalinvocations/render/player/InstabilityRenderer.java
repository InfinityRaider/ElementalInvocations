package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IChargeConfiguration;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.reference.Constants;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class InstabilityRenderer {
    private static final InstabilityRenderer INSTANCE = new InstabilityRenderer();

    public static InstabilityRenderer getInstance() {
        return INSTANCE;
    }

    private static final int RADIAL_VERTICES = 10;
    private static final int CIRCLE_VERTICES = 24;

    private InstabilityRenderer() {}

    public void renderInstability(IPlayerMagicProperties props, int width, int height, double z, float alpha) {
        if(props == null) {
            return;
        }

        //push matrix and attributes
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        //prepare render
        this.prepareRender(props, width, height);

        //render stability circle
        this.renderCircle(CIRCLE_VERTICES, 1.0, 0, 0, z, 1.0F, 1.0F, 1.0F, alpha);
        //render element lines
        this.renderElementLines(props, RADIAL_VERTICES, z, alpha);
        //render instability dot
        this.renderInstabilityDot(props.getChargeConfiguration(), z, alpha);

        //pop matrix and attributes
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private void prepareRender(IPlayerMagicProperties props, int width, int height) {
        //color stuff
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(3);
        //scaling
        double maxX = 0;
        double maxY = 0;
        double factor = ((double) Constants.NOMINAL_ORBS*Constants.CORE_TIERS)/Constants.MAX_LEVEL;
        for(Element element : Element.values()) {
            double valX = Math.abs(element.calculateX(props.getPlayerAdeptness(element) * factor));
            double valY = Math.abs(element.calculateY(props.getPlayerAdeptness(element) * factor));
            maxX = valX > maxX ? valX : maxX;
            maxY = valY > maxY ? valY : maxY;
        }
        double ratioX = 0.5*width/maxX;
        double ratioY = 0.5*height/maxY;
        double scale = Math.min(ratioX, ratioY);
        GlStateManager.scale(scale, scale, scale);
    }

    private void renderCircle(int vertices, double radius, double x, double y, double z, float r, float g, float b, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        for(int j = 0; j <= vertices; j++) {
            buffer.pos(x + radius*Math.cos(2*j*Math.PI/vertices), y + radius*Math.sin(2*j*Math.PI/vertices), z).color(r, g, b, alpha).endVertex();
        }
        tessellator.draw();
    }

    private void renderElementLines(IPlayerMagicProperties props, int vertices, double z, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        double factor = ((double) Constants.NOMINAL_ORBS*Constants.CORE_TIERS)/Constants.MAX_LEVEL;
        int period = 250;
        int time = (int) (System.currentTimeMillis() % period);
        for(int i = 0; i < Element.values().length; i++) {
            Element first = Element.values()[i];
            Element second = Element.values()[(i + 1)%Element.values().length];

            double x1 = first.calculateX(props.getPlayerAdeptness(first) * factor);
            double y1 = first.calculateY(props.getPlayerAdeptness(first) * factor);
            double x0 = first.calculateX(1);
            double y0 = first.calculateY(1);

            float r = first.getRed()/255.0F;
            float g = first.getGreen()/255.0F;
            float b = first.getBlue()/255.0F;
            float dr = second.getRed()/255.0F - r;
            float dg = second.getGreen()/255.0F - g;
            float db = second.getBlue()/255.0F - b;

            //Connecting line between two elements
            double dx = second.calculateX(props.getPlayerAdeptness(second) * factor) - x1;
            double dy = second.calculateY(props.getPlayerAdeptness(second) * factor) - y1;
            double amplitude = Math.sqrt(dx*dx + dy*dy)/(Constants.MAX_LEVEL*factor);
            int nodes = (int) (vertices * Math.sqrt(dx*dx + dy*dy)) + 1;
            nodes = nodes + ((nodes % 2 == 0) ? 0 : 1);
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for(int j = 0; j <= nodes; j++) {
                this.addVertex(buffer, j, nodes, x1, dx, y1, dy, z, r, dr, g, dg, b, db, alpha, amplitude, time, period);
            }
            tessellator.draw();

            //line from origin to element
            dx = x1 - x0;
            dy = y1 - y0;
            amplitude = Math.sqrt(dx*dx + dy*dy)/(Constants.MAX_LEVEL*factor);
            nodes = (int) (vertices * Math.sqrt(dx*dx + dy*dy)) + 1;
            nodes = nodes + ((nodes % 2 == 0) ? 0 : 1);
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for(int j = 0; j <= nodes; j++) {
                this.addVertex(buffer, j, nodes, x0, dx, y0, dy, z, 1.0F, r - 1.0F , 1.0F, g - 1.0F, 1.0F, b - 1.0F, alpha, amplitude, time, period);
            }
            tessellator.draw();
        }
    }

    private void renderInstabilityDot(IChargeConfiguration charges, double z, float alpha) {
        double x = charges.getInstabilityX();
        double y = charges.getInstabilityY();
        float r = charges.getPotencyMap().getRed();
        float g = charges.getPotencyMap().getGreen();
        float b = charges.getPotencyMap().getBlue();
        renderCircle(CIRCLE_VERTICES, 0.5, x, y, z, r, g, b, alpha);
    }

    private void addVertex(BufferBuilder buffer, int index, int total,
                           double x0, double dx, double y0, double dy, double z,
                           float r0, float dr, float g0, float dg, float b0, float db, float alpha,
                           double amplitude, int time, int period) {
        double deltaX = 0;
        double deltaY = 0;
        if(index%2 == 1) {
            double pdx = -dy/Math.sqrt(dx*dx + dy*dy);
            double pdy = dx/Math.sqrt(dx*dx + dy*dy);
            double phase =  phase(index, total);
            double amp = amplitude(amplitude, index, total);
            double mp = amp * Math.sin(2*time*Math.PI/period + phase);
            deltaX = mp*pdx;
            deltaY = mp*pdy;
        }
        buffer.pos(x0 +  (dx*index)/total + deltaX, y0 + (dy*index)/total + deltaY, z)
                .color(r0 + (dr*index)/total, g0 + (dg*index)/total, b0 + (db*index)/total, alpha)
                .endVertex();
    }

    private double phase(int index, int total) {
        return 2*Math.PI*Math.cos(47*index*Math.PI/(total+3.0));
    }

    private double amplitude(double amplitude, int index, int total) {
        return amplitude * Math.sin(index*Math.PI/RADIAL_VERTICES);
    }
}