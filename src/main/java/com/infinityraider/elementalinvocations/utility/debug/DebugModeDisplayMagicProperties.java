package com.infinityraider.elementalinvocations.utility.debug;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugModeDisplayMagicProperties extends DebugMode {
    @Override
    public String debugName() {
        return "display magic properties";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {}

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(player);
        if(properties == null) {
            player.addChatComponentMessage(new TextComponentString("Player magic properties not found"));
        } else {
            if(world.isRemote) {
                player.addChatComponentMessage(new TextComponentString("Magic properties: CLIENT"));
            } else {
                player.addChatComponentMessage(new TextComponentString("Magic properties: SERVER"));
            }
            player.addChatComponentMessage(new TextComponentString("------------------------"));
            for(Element element : Element.values()) {
                player.addChatComponentMessage(
                        new TextComponentString(""
                                + element.getTextFormat() + element.name()
                                + ": level = " + properties.getPlayerAdeptness(element)
                                + " ( " + properties.getExperience(element) + " / " + properties.getExperienceToLevelUp(element) + " )"
                        ));
            }
        }
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {}

}