package com.teaminfinity.elementalinvocations.magic.spell;

import com.teaminfinity.elementalinvocations.utility.ColorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Small class to assist rendering of beam spells on the client
 */
@SideOnly(Side.CLIENT)
public class MagicBeam {
    private final EntityPlayer player;
    private final int[] potencies;

    private boolean frustrumIgnore;

    private int red;
    private int green;
    private int blue;

    public MagicBeam(EntityPlayer player, int[] potencies) {
        this.player = player;
        this.potencies = potencies;
        this.frustrumIgnore = player.ignoreFrustumCheck;
        player.ignoreFrustumCheck = true;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public void onBeamEnd(int channelTicks) {
        this.getPlayer().ignoreFrustumCheck = this.frustrumIgnore;
    }

    public int getRed() {
        if(red < 0) {
            compileColors();
        }
        return red;
    }

    public int getGreen() {
        if(green < 0) {
            compileColors();
        }
        return green;
    }

    public int getBlue() {
        if(blue < 0) {
            compileColors();
        }
        return blue;
    }

    private void compileColors() {
        int[] colors = ColorHelper.compileColors(this.potencies);
        this.red = colors[0];
        this.green = colors[1];
        this.blue = colors[2];
    }

}
