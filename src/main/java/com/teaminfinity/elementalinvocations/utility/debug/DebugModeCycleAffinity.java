package com.teaminfinity.elementalinvocations.utility.debug;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.reference.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugModeCycleAffinity extends DebugMode {
    private final Element element;

    public DebugModeCycleAffinity(Element element) {
        this.element = element;
    }

    @Override
    public String debugName() {
        return "cycle affinity: " + element.getTextFormat() + element.name();
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IPlayerMagicProperties magicProperties = PlayerMagicProvider.getMagicProperties(player);
        if(magicProperties != null) {
            if(magicProperties.getPlayerAffinity() == this.element) {
                magicProperties.setPlayerAdeptness((magicProperties.getPlayerAdeptness() + 1) % (Constants.MAX_LEVEL + 1));
            } else {
                magicProperties.setPlayerAffinity(this.element);
                magicProperties.setPlayerAdeptness(0);
            }
            if(player.worldObj.isRemote) {
                player.addChatComponentMessage(new TextComponentString(
                        "Set player magic properties to: " + magicProperties.getPlayerAffinity().name() + " level " + magicProperties.getPlayerAdeptness()));
            }
        }
    }
}
