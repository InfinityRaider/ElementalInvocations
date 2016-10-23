package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.infinityraider.infinitylib.utility.ISerializable;
import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.api.souls.ISoul;
import com.teaminfinity.elementalinvocations.network.MessageSyncSouls;
import com.teaminfinity.elementalinvocations.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;

import java.util.*;

public class PlayerSoulCollection implements ISoulCollection, ISerializable {
    /* player instance */
    private EntityPlayer player;

    /* number of souls */
    private final Deque<ISoul> souls;

    public PlayerSoulCollection() {
        this.souls = new ArrayDeque<>();
    }

    public PlayerSoulCollection setPlayer(EntityPlayer player) {
        if(this.player == null) {
            this.player = player;
        }
        return this;
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
        ElementalInvocations.instance.getNetworkWrapper().sendToAll(new MessageSyncSouls(player, this.writeToNBT()));
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
