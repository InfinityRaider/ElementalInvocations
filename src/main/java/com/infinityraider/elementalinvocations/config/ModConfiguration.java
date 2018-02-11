package com.infinityraider.elementalinvocations.config;

import com.infinityraider.elementalinvocations.api.spells.ISpell;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.infinitylib.config.ConfigEntry;
import com.infinityraider.infinitylib.config.IModConfiguration;
import com.infinityraider.infinitylib.config.InfinityConfigurationHandler;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModConfiguration implements IModConfiguration {
    private static final ModConfiguration INSTANCE = new ModConfiguration();

    public static ModConfiguration getInstance() {
        return INSTANCE;
    }

    private final InfinityConfigurationHandler handler;

    //general
    public ConfigEntry<Boolean> orbRecipes;

    //balance
    public ConfigEntry<Float> damageMultiplier;
    public ConfigEntry<Integer> fizzleConstant;
    public ConfigEntry<Integer> expConstant;
    public ConfigEntry<Integer> expMultiplier;
    public ConfigEntry<Integer> expComboMultiplier;
    public ConfigEntry<Integer> levelLossOnAffinityChange;
    public ConfigEntry<Double> affinityBonus;

    //client
    @SideOnly(Side.CLIENT)
    public ConfigEntry<Boolean> overridePlayerRenderer;

    //debug
    public ConfigEntry<Boolean> debug;

    private ModConfiguration() {
        this.handler = ElementalInvocations.instance.getConfigurationHandler();
    }

    public Configuration getConfiguration() {
        return this.handler.getConfiguration();
    }

    public boolean enableOrbRecipes() {
        return this.orbRecipes.getValue();
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier.getValue();
    }

    public int getFizzleConstant() {
        return this.fizzleConstant.getValue();
    }

    public int getExpConstant() {
        return this.expConstant.getValue();
    }

    public int getExpMultiplier() {
        return this.expMultiplier.getValue();
    }

    public int getExpComboMultiplier() {
        return this.expComboMultiplier.getValue();
    }

    public int getLevelLossOnAffinityChange() {
        return this.levelLossOnAffinityChange.getValue();
    }

    public double getAffinityBonus() {
        return this.affinityBonus.getValue();
    }

    @SideOnly(Side.CLIENT)
    public boolean overridePlayerRenderer() {
        return this.overridePlayerRenderer.getValue();
    }

    public boolean debug() {
        return this.debug == null ? false : this.debug.getValue();
    }

    @Override
    public void initializeConfiguration(InfinityConfigurationHandler handler) {
        //general
        orbRecipes = ConfigEntry.Boolean("enable orb recipes", handler, Categories.GENERAL.getCategory(), true,
                "set to false to disable recipes for the elemental orbs");
        //balance
        damageMultiplier = ConfigEntry.Float("damage multiplier", handler, Categories.BALANCE.getCategory(), 1.0F, 0.1F, 100.0F,
                "The global damage multiplier applied to all damage done by spells, tweak to balance the mod in your modpack");
        fizzleConstant = ConfigEntry.Integer("Fizzle growth factor", handler, Categories.BALANCE.getCategory(), 500, 1, 100000,
                "The fizzle chance growth factor, used to calculate the fizzle chance as function of instability (larger factor = lower fizzle chance), " +
                        "this is used in an exponent, and is thus a very sensitive value, use the Excel file on the GitHub repo when tweaking this factor");
        expConstant = ConfigEntry.Integer("Experience growth factor", handler, Categories.BALANCE.getCategory(), 10, 1, 1000,
                "The fizzle chance growth factor, used to calculate the gained experience as function of instability (larger factor = less experience), " +
                        "this is used in an exponent, and is thus a very sensitive value, use the Excel file on the GitHub repo when tweaking this factor");
        expMultiplier = ConfigEntry.Integer("magic experience multiplier", handler, Categories.BALANCE.getCategory(), 5, 0, 100,
                "Whenever the player gains magic experience, the amount is multiplied by this number");
        expComboMultiplier = ConfigEntry.Integer("magic experience  combo multiplier", handler, Categories.BALANCE.getCategory(), 5, 1, 10,
                "The multiplier added to the experience gain when a player makes a combo for an invoked spell");
        affinityBonus = ConfigEntry.Double("affinity bonus", handler, Categories.BALANCE.getCategory(), 0.2, 0.0, 1.0,
                "The potency of spells is increased/decreased proportionality if the spell's element matches the player's element/opposite element");
        levelLossOnAffinityChange = ConfigEntry.Integer("level loss on affinity change", handler, Categories.BALANCE.getCategory(), 1, 0, 10,
                "The amount of magic levels the player loses when changing its magic affinity to another element (both for the old and new affinity)");
        //debug
        debug = ConfigEntry.Boolean("debug", handler, Categories.DEBUG.getCategory(), false, "Set to true if you wish to enable debug mode");

        //debug statement
        ElementalInvocations.instance.getLogger().debug("Configuration Loaded");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initializeConfigurationClient(InfinityConfigurationHandler handler) {
        //override rendering
        overridePlayerRenderer = ConfigEntry.Boolean("Left arm swing animation", handler, Categories.CLIENT.getCategory(), true,
                "Set to false if you experience issues with player rendering, " +
                        "disabling this will have the effect of no animating the left arm of players using the maneuver gear sword." +
                        "This is a client side only config and does not have to match the server");

        //debug statement
        ElementalInvocations.instance.getLogger().debug("Client configuration Loaded");
    }

    public int getPotionEffectId(String name, int defaultId) {
        int id = this.getConfiguration().getInt(name, Categories.POTION.name(), defaultId, 0, 255,
                "Id for the " + name + " potion effect, this potion effect is generated on the first run by detecting a free potion id.");
        if(this.getConfiguration().hasChanged()) {
            this.getConfiguration().save();
        }
        return id;
    }

    public Tuple<Float, Integer> getElementChargeLootProperties(Element element, String type, boolean generate) {
        float chance = this.getConfiguration().getFloat(element.name().toLowerCase() + " elemental charge " + type + " loot chance", Categories.LOOT.name(), generate ? 0.1F : 1.0F, 0.0F, 1.0F,
                "chance for this elemental charge to generate as loot in " + type + "s");
        int rolls = this.getConfiguration().getInt(element.name().toLowerCase() + " elemental charge " + type + " loot rolls", Categories.LOOT.name(), 1, 1, 5,
                "maximum amount of charges generating in each chest");
        if(this.getConfiguration().hasChanged()) {
            this.getConfiguration().save();
        }
        return new Tuple<>(chance, rolls);
    }

    public boolean isSpellDisabled(ISpell spell) {
        boolean disabled = this.getConfiguration().getBoolean("Disable " + spell.getId(), Categories.SPELLS.name(), false,
                "set to true to disable the " + spell.getId() + " spell");
        if(this.getConfiguration().hasChanged()) {
            this.getConfiguration().save();
        }
        return disabled;
    }

    public List<ConfigCategory> getCategories() {
        return Categories.getCategories();
    }

    public enum Categories {
        GENERAL(),
        SPELLS(),
        BALANCE(),
        POTION(),
        LOOT(),
        CLIENT(),
        DEBUG();

        private final ConfigCategory category;

        Categories() {
            this.category = new ConfigCategory(this.name());
        }

        public ConfigCategory getCategory() {
            return this.category;
        }

        public static List<ConfigCategory> getCategories() {
            return Arrays.stream(values()).map(Categories::getCategory).collect(Collectors.toList());
        }
    }
}
