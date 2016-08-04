package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.teaminfinity.elementalinvocations.api.spells.IPlayerSoulCollection;
import com.teaminfinity.elementalinvocations.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerSoulCollection implements IPlayerSoulCollection {
    /* player instance */
    private EntityPlayer player;

    /* number of souls */
    private int souls;

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public PlayerSoulCollection setPlayer(EntityPlayer player) {
        this.player = player;
        return this;
    }

    @Override
    public IPlayerSoulCollection addSoul() {
        this.souls = this.souls + 1;
        return this;
    }

    @Override
    public IPlayerSoulCollection removeSoul() {
        this.souls = Math.max(0, this.souls - 1);
        return this;
    }

    @Override
    public IPlayerSoulCollection releaseSouls() {
        this.souls = 0;
        return this;
    }

    @Override
    public int getSoulCount() {
        return souls;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(Names.NBT.COUNT, souls);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.souls = tag.getInteger(Names.NBT.COUNT);
    }
}
