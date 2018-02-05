package com.infinityraider.elementalinvocations.utility.debug;

import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.reference.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugModeConfigureAdeptness extends DebugMode {
    private final Element element;

    public DebugModeConfigureAdeptness(Element element) {
        this.element = element;
    }

    @Override
    public String debugName() {
        return "configure addeptness: " + element.getTextFormat() + element.name();
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {}

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        IPlayerMagicProperties magicProperties = CapabilityPlayerMagicProperties.getMagicProperties(player);
        if(magicProperties != null && !world.isRemote) {
            magicProperties.setPlayerAdeptness(this.element, (magicProperties.getPlayerAdeptness(this.element) % Constants.MAX_LEVEL ) + 1);
            player.addChatComponentMessage(new TextComponentString(
                    "Set player magic properties to: " + this.element.name() + " level " + magicProperties.getPlayerAdeptness(this.element)));
        }
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) { }
}
