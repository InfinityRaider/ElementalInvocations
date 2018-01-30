package com.infinityraider.elementalinvocations.magic.spell;

import com.infinityraider.elementalinvocations.utility.ColorHelper;
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

    private double range;
    private boolean frustrumIgnore;

    private int red = -1;
    private int green = -1;
    private int blue = -1;

    public MagicBeam(EntityPlayer player, int[] potencies, double range) {
        this.player = player;
        this.potencies = potencies;
        this.range = range;
        this.frustrumIgnore = player.ignoreFrustumCheck;
        player.ignoreFrustumCheck = true;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public double getRange() {
        return this.range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void onBeamEnd(int channelTicks) {
        this.getPlayer().ignoreFrustumCheck = this.frustrumIgnore;
    }

    public double getThickness() {
        int sum = 0;
        for(int potency : this.potencies) {
            sum += potency;
        }
        return ((double) sum)/ ( 16.0 * this.potencies.length);
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
