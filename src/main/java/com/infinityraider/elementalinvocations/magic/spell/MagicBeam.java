package com.infinityraider.elementalinvocations.magic.spell;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Small class to assist rendering of beam spells on the client
 */
@SideOnly(Side.CLIENT)
public class MagicBeam {
    private final EntityPlayer player;
    private final IPotencyMap potencies;

    private double range;
    private boolean frustrumIgnore;

    public MagicBeam(EntityPlayer player, IPotencyMap potencies, double range) {
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
        return ((double) this.potencies.getTotalPotency())/ ( 16.0 * Element.values().length);
    }

    public float getRed() {
        return this.potencies.getRed();
    }

    public float getGreen() {
        return this.potencies.getGreen();
    }

    public float getBlue() {
        return this.potencies.getBlue();
    }
}
