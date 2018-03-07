package com.infinityraider.elementalinvocations.block;

import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.render.block.RenderBlockImpaleSpike;
import com.infinityraider.infinitylib.block.BlockCustomRenderedBase;
import com.infinityraider.infinitylib.block.blockstate.InfinityProperty;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockImpaleSpike extends BlockCustomRenderedBase {

    public BlockImpaleSpike() {
        super("ei.block.impale_spike", Material.ROCK);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderBlockImpaleSpike getRenderer() {
        return new RenderBlockImpaleSpike(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + getInternalName());
    }

    @Override
    protected InfinityProperty[] getPropertyArray() {
        return new InfinityProperty[] {};
    }
}