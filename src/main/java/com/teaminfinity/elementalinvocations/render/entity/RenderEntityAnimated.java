package com.teaminfinity.elementalinvocations.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderEntityAnimated<E extends Entity> extends Render<E> {
    private float lastPartialTick = -1;
    private int counter = 0;
    private int frame;

    protected RenderEntityAnimated(RenderManager renderManager) {
        super(renderManager);
    }

    protected int getFrame() {
        return frame;
    }

    protected void calculateFrame(float partialTicks, int frameTime, int frames) {
        if(lastPartialTick < 0) {
            frame = 0;
            counter = 0;
        } else {
            if(partialTicks <= lastPartialTick) {
                counter = (counter + 1) % frameTime;
                frame = counter == 0 ? (frame + 1) % frames : frame;
            }
        }
        lastPartialTick = partialTicks;
    }
}
