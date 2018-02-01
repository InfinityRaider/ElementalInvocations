package com.infinityraider.elementalinvocations.handler;

import com.infinityraider.elementalinvocations.api.spells.ISpell;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigurationHandler {
    private static final ConfigurationHandler INSTANCE = new ConfigurationHandler();

    private ConfigurationHandler() {}

    public static ConfigurationHandler getInstance() {
        return INSTANCE;
    }

    private Configuration config;

    //general
    public boolean orbRecipes;

    //balance
    public double damageMultiplier;
    public int fizzleConstant;
    public int experienceConstant;
    public double experienceMultiplier;
    public int levelLossOnAffinityChange;
    public double affinityBonus;


    //client
    @SideOnly(Side.CLIENT)
    public boolean overridePlayerRenderer;

    //debug
    public boolean debug;

    public void init(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadConfiguration();
        if(config.hasChanged()) {
            config.save();
        }
        ElementalInvocations.instance.getLogger().debug("Configuration Loaded");
    }

    @SideOnly(Side.CLIENT)
    public void initClientConfigs(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadClientConfiguration(event);
        if(config.hasChanged()) {
            config.save();
        }
        ElementalInvocations.instance.getLogger().debug("Client configuration Loaded");
    }

    private void loadConfiguration() {
        //general
        orbRecipes = config.getBoolean("enable orb recipes", Categories.GENERAL.getName(), true,
                "set to false to disable recipes for the elemental orbs");

        //balance
        damageMultiplier = config.getFloat("damage multiplier", Categories.BALANCE.getName(), 1.0F, 0.1F, 100.0F,
                "The global damage multiplier applied to all damage done by spells, tweak to balance the mod in your modpack");
        fizzleConstant = config.getInt("Fizzle growth factor", Categories.BALANCE.getName(), 1000, 1, 100000,
                "The fizzle chance growth factor, used to calculate the fizzle chance as function of instability (larger factor = lower fizzle chance), " +
                "this is used in an exponent, and is thus a very sensitive value, use the Excel file on the GitHub repo when tweaking this factor");
        experienceConstant = config.getInt("Experience growth factor", Categories.BALANCE.getName(), 1000, 1, 100000,
                "The fizzle chance growth factor, used to calculate the gained experience as function of instability (larger factor = less experience), " +
                "this is used in an exponent, and is thus a very sensitive value, use the Excel file on the GitHub repo when tweaking this factor");
        experienceMultiplier = config.getFloat("magic experience multiplier", Categories.BALANCE.getName(), 1F, 0.0F, 100.0F,
                "Whenever the player gains magic experience, the amount is multiplied by this number");
        affinityBonus = config.getFloat("affinity bonus", Categories.BALANCE.getName(), 0.2F, 0.0F, 1.0F,
                "The potency of spells is increased/decreased proportionality if the spell's element matches the player's element/opposite element");
        levelLossOnAffinityChange = config.getInt("level loss on affinity change", Categories.BALANCE.getName(), 1, 0, 10,
                "The amount of magic levels the player loses when changing its magic affinity to another element (both for the old and new affinity)");

        //debug
        debug = config.getBoolean("debug", Categories.DEBUG.getName(), false, "Set to true if you wish to enable debug mode");
    }

    @SideOnly(Side.CLIENT)
    private void loadClientConfiguration(FMLPreInitializationEvent event) {
        //override rendering
        overridePlayerRenderer = config.getBoolean("Left arm swing animation", Categories.CLIENT.getName(), true,
                "Set to false if you experience issues with player rendering, " +
                        "disabling this will have the effect of no animating the left arm of players using the maneuver gear sword." +
                        "This is a client side only config and does not have to match the server");

    }

    public int getPotionEffectId(String name, int defaultId) {
        int id = config.getInt(name, Categories.POTION.getName(), defaultId, 0, 255,
                "Id for the " + name + " potion effect, this potion effect is generated on the first run by detecting a free potion id.");
        if(config.hasChanged()) {
            config.save();
        }
        return id;
    }

    public Tuple<Float, Integer> getElementChargeLootProperties(Element element, String type, boolean generate) {
        float chance = config.getFloat(element.name().toLowerCase() + " elemental charge " + type + " loot chance", Categories.LOOT.getName(), generate ? 0.1F : 1.0F, 0.0F, 1.0F,
                "chance for this elemental charge to generate as loot in " + type + "s");
        int rolls = config.getInt(element.name().toLowerCase() + " elemental charge " + type + " loot rolls", Categories.LOOT.getName(), 1, 1, 5,
                "maximum amount of charges generating in each chest");
        if(config.hasChanged()) {
            config.save();
        }
        return new Tuple<>(chance, rolls);
    }

    public boolean isSpellDisabled(ISpell spell) {
        boolean disabled = config.getBoolean("Disable " + spell.getId(), Categories.SPELLS.getName(), false,
                "set to true to disable the " + spell.getId() + " spell");
        if(config.hasChanged()) {
            config.save();
        }
        return disabled;
    }

    public enum Categories {
        GENERAL("general"),
        SPELLS("spells"),
        BALANCE("balance"),
        POTION("potion"),
        LOOT("loot"),
        CLIENT("client"),
        DEBUG("debug");

        private final String name;

        Categories(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
