package com.infinityraider.elementalinvocations.magic.spell.death;

import com.infinityraider.elementalinvocations.api.souls.ISoul;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.network.MessageSyncSouls;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.infinitylib.utility.ISerializable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class PlayerSoulCollection implements ISoulCollection, ISerializable {
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
        if(!player.worldObj.isRemote) {
            this.souls.add(soul);
            this.syncToClient();
        }
    }

    @Override
    public ISoul removeSoul() {
        if(!player.worldObj.isRemote) {
            ISoul soul = this.souls.pollFirst();
            this.syncToClient();
            return soul;
        }
        return null;
    }

    @Override
    public List<ISoul> releaseSouls() {
        if (!player.worldObj.isRemote) {
            List<ISoul> temp = new ArrayList<>(this.souls);
            this.souls.clear();
            this.syncToClient();
            return temp;
        }
        return Collections.emptyList();
    }


    @Override
    public int getSoulCount() {
        return this.souls.size();
    }

    private void syncToClient() {
        new MessageSyncSouls(player, this.writeToNBT()).sendToAll();
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
