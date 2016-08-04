package com.teaminfinity.elementalinvocations.item;

import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.reference.InventoryTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;

import java.util.Collections;
import java.util.List;

public class ItemWand extends ItemBase implements IItemWithRecipe, IDualWieldedWeapon {
    public ItemWand() {
        super("wand");
        this.setCreativeTab(InventoryTabs.ELEMNTAL_INVOCATIONS);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return Collections.emptyList();
    }

    @Override
    public List<IRecipe> getRecipes() {
        return Collections.emptyList();
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(player);
        if(properties == null) {
            return;
        }
        if(shift) {

        }

    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        //wands do not damage the entity, they are only used to invoke charges
        return false;
    }

    public IMagicCharge getCharge(ItemStack stack) {
        return null;
    }
}
