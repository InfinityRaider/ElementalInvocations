package com.teaminfinity.elementalinvocations.magic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * Class keeping track of all beams currently being channeled on the server
 */
@SideOnly(Side.CLIENT)
public class BeamHandler {
    private static final BeamHandler INSTANCE = new BeamHandler();

    public static BeamHandler getInstance() {
        return INSTANCE;
    }

    private final HashMap<UUID, MagicBeam> beams;

    private BeamHandler() {
        this.beams = new HashMap<>();
    }

    public void startBeam(EntityPlayer player, int[] potencies, double range) {
        this.beams.put(player.getUniqueID(), new MagicBeam(player, potencies, range));
    }

    public void stopBeam(EntityPlayer player, int channelTick) {
        if(this.beams.containsKey(player.getUniqueID())) {
            this.beams.get(player.getUniqueID()).onBeamEnd(channelTick);
            this.beams.remove(player.getUniqueID());
        }
    }

    public Optional<MagicBeam> getMagicBeam(EntityPlayer player) {
        return this.beams.containsKey(player.getUniqueID()) ? Optional.of(this.beams.get(player.getUniqueID())) : Optional.empty();
    }
}
