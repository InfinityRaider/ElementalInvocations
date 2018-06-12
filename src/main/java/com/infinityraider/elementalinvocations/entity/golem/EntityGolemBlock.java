package com.infinityraider.elementalinvocations.entity.golem;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.Collections;

public class EntityGolemBlock extends EntityLivingBase implements IEntityAdditionalSpawnData {

    public EntityGolemBlock(World worldIn) {
        super(worldIn);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return null;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack) {}

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

    }
}