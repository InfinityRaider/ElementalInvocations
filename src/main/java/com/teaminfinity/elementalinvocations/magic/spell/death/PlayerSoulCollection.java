package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.teaminfinity.elementalinvocations.api.souls.ISoul;
import com.teaminfinity.elementalinvocations.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PlayerSoulCollection implements ISoulCollection {

    /* player instance */
    private final EntityPlayer player;

    /* number of souls */
    private final Deque<ISoul> souls;

    public PlayerSoulCollection(EntityPlayer player) {
        this.player = player;
        this.souls = new ArrayDeque<>();
    }

    @Override
    public EntityPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void addSoul(ISoul soul) {
        this.souls.add(soul);
    }

    @Override
    public ISoul removeSoul() {
        return this.souls.pollFirst();
    }

    @Override
    public List<ISoul> releaseSouls() {
        List<ISoul> temp = new ArrayList<>(this.souls);
        this.souls.clear();
        return temp;
    }

    @Override
    public int getSoulCount() {
        return this.souls.size();
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(Names.NBT.COUNT, this.souls.size());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        int count = tag.getInteger(Names.NBT.COUNT);
        this.souls.clear();
        for (int i = 0; i < count; i++) {
            this.souls.add(new BasicSoul("Generic Soul"));
        }
    }

}
