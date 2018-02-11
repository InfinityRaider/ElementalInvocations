package com.infinityraider.elementalinvocations.config;

import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class ModConfigGui extends GuiConfig {

    public ModConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), Reference.MOD_ID, false, false, "Mod config");
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> elements = new ArrayList<>();
        ModConfiguration.getInstance().getCategories().forEach(category -> elements.addAll(new ConfigElement(category).getChildElements()));
        return elements;
    }
}