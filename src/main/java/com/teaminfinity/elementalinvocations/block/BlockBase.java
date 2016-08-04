package com.teaminfinity.elementalinvocations.block;

import com.teaminfinity.elementalinvocations.block.state.InfinityProperty;
import com.teaminfinity.elementalinvocations.utility.IToggleable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;

import java.util.List;

public abstract class BlockBase extends Block implements IToggleable {
    private final String internalName;

    public BlockBase(String name, Material blockMaterial) {
        super(blockMaterial);
        this.internalName = name;
        this.setDefaultState();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getInternalName() {
        return this.internalName;
    }

    public abstract List<String> getOreTags();

    @Override
    protected final BlockStateContainer createBlockState() {
        InfinityProperty[] propertyArray = this.getPropertyArray();
        IProperty[] properties = new IProperty[propertyArray.length];
        for(int i = 0; i < properties.length; i++) {
            properties[i] = propertyArray[i].getProperty();
        }
        return new BlockStateContainer(this, properties);
    }

    private void setDefaultState() {
        IBlockState state = this.blockState.getBaseState();
        for(InfinityProperty property : this.getPropertyArray()) {
            state.withProperty(property.getProperty(), property.getDefault());
        }
    }

    /**
     * @return a property array containing all properties for this block's state
     */
    protected abstract InfinityProperty[] getPropertyArray();

    /**
     * Retrieves the block's ItemBlock class, as a generic class bounded by the
     * ItemBlock class.
     *
     * @return the block's class, may be null if no specific ItemBlock class is
     * desired.
     */
    public abstract Class<? extends ItemBlock> getItemBlockClass();

    @Override
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
