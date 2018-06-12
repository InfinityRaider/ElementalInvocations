package com.infinityraider.elementalinvocations.utility.debug;

import com.infinityraider.infinitylib.utility.debug.DebugMode;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.magic.spell.death.BasicSoul;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModeAddSoul extends DebugMode {
    @Override
    public String debugName() {
        return "add soul";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {}

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(player);
        if(collection != null) {
            collection.addSoul(new BasicSoul("debug"));
            if(world.isRemote) {
                ElementalInvocations.instance.getLogger().debug(player.getDisplayNameString() + " now has " + collection.getSoulCount() + " collected souls");
            }
        }
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {}
}
