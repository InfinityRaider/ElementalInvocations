package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.utility.LogHelper;
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
    public int levelingSpeed;
    public int experienceMultiplier;

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
        LogHelper.debug("Configuration Loaded");
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
        LogHelper.debug("Client configuration Loaded");
    }

    private void loadConfiguration() {
        //general
        orbRecipes = config.getBoolean("enable orb recipes", Categories.GENERAL.getName(), true,
                "set to false to disable recipes for the elemental orbs");
        levelingSpeed = config.getInt("magic level growth rate", Categories.GENERAL.getName(), 3, 2, 5,
                "This number determines the speed at which players grow in magic level, the lower, the faster players will level (scales exponential)");
        experienceMultiplier = config.getInt("magic experience multiplier", Categories.GENERAL.getName(), 1, 0, 50,
                "Whenever the player gains magic experience, the amount is multiplied by this number");
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

    public enum Categories {
        GENERAL("general"),
        SPELLS("spells"),
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
