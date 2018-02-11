package com.infinityraider.elementalinvocations.handler;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.config.ModConfiguration;
import com.infinityraider.elementalinvocations.registry.ItemRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootHandler {
    private static final LootHandler INSTANCE = new LootHandler();

    public static LootHandler getInstance() {
        return INSTANCE;
    }

    //making this static causes an NPE for some reason
    private final ResourceLocation[] LOOT_TABLES = new ResourceLocation[] {
            LootTableList.CHESTS_END_CITY_TREASURE,
            LootTableList.CHESTS_SIMPLE_DUNGEON,
            LootTableList.CHESTS_ABANDONED_MINESHAFT,
            LootTableList.CHESTS_NETHER_BRIDGE,
            LootTableList.CHESTS_STRONGHOLD_LIBRARY,
            LootTableList.CHESTS_STRONGHOLD_CROSSING,
            LootTableList.CHESTS_STRONGHOLD_CORRIDOR,
            LootTableList.CHESTS_DESERT_PYRAMID,
            LootTableList.CHESTS_JUNGLE_TEMPLE,
            LootTableList.CHESTS_IGLOO_CHEST};

    private final String[] LOOT_TABLE_NAMES = new String[] {
            "End City Treasure chest",
            "Simple Dungeon chest",
            "Mineshaft chest",
            "Nether Bridge chest",
            "Stronghold library chest",
            "Stronghold crossing chest",
            "Stronghold corridor chest",
            "Desert Pyramid chest",
            "Jungle temple chest",
            "Igloo chest"};

    private final Map<ResourceLocation, List<LootPool>> poolMap;

    private LootHandler() {
        this.poolMap = new HashMap<>();
        for(int i = 0; i < LOOT_TABLES.length; i++) {
            List<LootPool> list = new ArrayList<>();
            for(Element element : Element.values()) {
                Tuple<Float, Integer> data = ModConfiguration.getInstance().getElementChargeLootProperties(element, LOOT_TABLE_NAMES[i], shouldGenerate(element, LOOT_TABLES[i]));
                if(data.getFirst() <= 0) {
                    continue;
                }
                LootFunction metaFunction = new SetMetadata(new LootCondition[0], new RandomValueRange(element.ordinal(), element.ordinal()));
                LootEntry entry = new LootEntryItem(ItemRegistry.getInstance().itemElementalCore, 1, 1, new LootFunction[] {metaFunction}, new LootCondition[] {}, "charge." + element.name().toLowerCase());

                LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[] {new RandomChance(data.getFirst())}, new RandomValueRange(0, data.getSecond()), new RandomValueRange(0, 0), "pool.charge."+ element.name().toLowerCase());
                list.add(pool);
            }
            poolMap.put(LOOT_TABLES[i], ImmutableList.copyOf(list));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onLootTableLoad(LootTableLoadEvent event) {
        if(poolMap.containsKey(event.getName())) {
            poolMap.get(event.getName()).forEach(pool -> event.getTable().addPool(pool));
        }
    }

    private boolean shouldGenerate(Element element, ResourceLocation loc) {
        if(loc == LootTableList.CHESTS_SIMPLE_DUNGEON || loc == LootTableList.CHESTS_STRONGHOLD_LIBRARY || loc == LootTableList.CHESTS_STRONGHOLD_CORRIDOR || loc == LootTableList.CHESTS_STRONGHOLD_CROSSING) {
            return true;
        }
        switch (element) {
            case FIRE:
                return loc == LootTableList.CHESTS_NETHER_BRIDGE || loc == LootTableList.CHESTS_DESERT_PYRAMID;
            case WATER:
                return loc == LootTableList.CHESTS_IGLOO_CHEST || loc == LootTableList.CHESTS_JUNGLE_TEMPLE;
            case AIR:
                return loc == LootTableList.CHESTS_END_CITY_TREASURE || loc == LootTableList.CHESTS_DESERT_PYRAMID;
            case EARTH:
                return loc == LootTableList.CHESTS_ABANDONED_MINESHAFT || loc == LootTableList.CHESTS_DESERT_PYRAMID;
            case DEATH:
                return loc == LootTableList.CHESTS_END_CITY_TREASURE || loc == LootTableList.CHESTS_NETHER_BRIDGE;
            case LIFE:
                return loc == LootTableList.CHESTS_JUNGLE_TEMPLE || loc == LootTableList.CHESTS_IGLOO_CHEST;
        }
        return false;
    }
}
