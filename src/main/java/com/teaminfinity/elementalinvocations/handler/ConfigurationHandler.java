package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.utility.LogHelper;
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
        //debug
        debug = config.getBoolean("debug", Categories.CATEGORY_DEBUG.getName(), false, "Set to true if you wish to enable debug mode");
    }

    @SideOnly(Side.CLIENT)
    private void loadClientConfiguration(FMLPreInitializationEvent event) {
        //override rendering
        overridePlayerRenderer = config.getBoolean("Left arm swing animation", Categories.CATEGORY_CLIENT.getName(), true,
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

    public enum Categories {
        POTION("potion"),
        CATEGORY_CLIENT("client"),
        CATEGORY_DEBUG("debug");

        private final String name;

        Categories(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
