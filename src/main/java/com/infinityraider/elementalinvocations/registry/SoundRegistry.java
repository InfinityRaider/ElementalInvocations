package com.infinityraider.elementalinvocations.registry;

import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundRegistry {
    private static final SoundRegistry INSTANCE = new SoundRegistry();

    public static SoundRegistry getInstance() {
        return INSTANCE;
    }

    public final SoundEvent SOUND_BALL_LIGHTNING;
    public final SoundEvent SOUND_CHAIN_LIGHTNING;
    public final SoundEvent SOUND_EARTHQUAKE;
    public final SoundEvent SOUND_FADE;
    public final SoundEvent SOUND_FIRE_BEAM;
    public final SoundEvent SOUND_FIZZLE;
    public final SoundEvent SOUND_FROST_ARMOR;
    public final SoundEvent SOUND_HEX;
    public final SoundEvent SOUND_IMPALE;
    public final SoundEvent SOUND_INVOKE;
    public final SoundEvent SOUND_LIVING_ARMOR;
    public final SoundEvent SOUND_MAGNETIZE;
    public final SoundEvent SOUND_METEOR;
    public final SoundEvent SOUND_NECROMANCY;
    public final SoundEvent SOUND_REPLICATE;
    public final SoundEvent SOUND_SOLID_AIR;
    public final SoundEvent SOUND_SUNSTRIKE;
    public final SoundEvent SOUND_THROW_ROCK;
    public final SoundEvent SOUND_TORNADO;
    public final SoundEvent SOUND_VACUUM;
    public final SoundEvent SOUND_WAVEFORM;

    private SoundRegistry() {
        this.SOUND_BALL_LIGHTNING = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "ball_lightning"));
        this.SOUND_CHAIN_LIGHTNING = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "chain_lightning"));
        this.SOUND_EARTHQUAKE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "earthquake"));
        this.SOUND_FADE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "fade"));
        this.SOUND_FIRE_BEAM = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "fire_beam"));
        this.SOUND_FIZZLE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "fizzle"));
        this.SOUND_FROST_ARMOR = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "frost_armor"));
        this.SOUND_HEX = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "hex"));
        this.SOUND_IMPALE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "impale"));
        this.SOUND_INVOKE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "invoke"));
        this.SOUND_LIVING_ARMOR = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "living_armor"));
        this.SOUND_MAGNETIZE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "magnetize"));
        this.SOUND_METEOR = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "meteor"));
        this.SOUND_NECROMANCY = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "necromancy"));
        this.SOUND_REPLICATE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "replicate"));
        this.SOUND_SOLID_AIR = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "solid_air"));
        this.SOUND_SUNSTRIKE = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "sunstrike"));
        this.SOUND_THROW_ROCK = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "throw_rock"));
        this.SOUND_TORNADO = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "tornado"));
        this.SOUND_VACUUM = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "vacuum"));
        this.SOUND_WAVEFORM = new SoundEvent(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "waveform"));
    }
}