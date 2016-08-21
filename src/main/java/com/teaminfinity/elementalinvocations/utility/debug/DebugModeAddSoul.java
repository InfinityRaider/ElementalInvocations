package com.teaminfinity.elementalinvocations.utility.debug;

import com.infinityraider.infinitylib.utility.LogHelper;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import com.teaminfinity.elementalinvocations.magic.spell.death.BasicSoul;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollectionProvider;
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
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ISoulCollection collection = PlayerSoulCollectionProvider.getSoulCollection(player);
        if(collection != null) {
            collection.addSoul(new BasicSoul("debug"));
            if(world.isRemote) {
                LogHelper.debug(player.getDisplayNameString() + " now has " + collection.getSoulCount() + " collected souls");
            }
        }
    }
}
