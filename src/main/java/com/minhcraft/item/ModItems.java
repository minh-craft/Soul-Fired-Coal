package com.minhcraft.item;

import com.minhcraft.SoulFiredCoal;
import com.minhcraft.block.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ModItems {
    public static final Item SOUL_FIRED_COAL = registerItem("soul_fired_coal", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientsItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.COAL, SOUL_FIRED_COAL);
    }

    private static void addItemsToNaturalBlocksItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Blocks.NETHER_QUARTZ_ORE, ModBlocks.SOUL_SAND_SOUL_FIRED_COAL_ORE);
        entries.addAfter(ModBlocks.SOUL_SAND_SOUL_FIRED_COAL_ORE, ModBlocks.SOUL_SOIL_SOUL_FIRED_COAL_ORE);
    }

    private static void addItemsToBuildingBlocksItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Blocks.COAL_BLOCK, ModBlocks.SOUL_FIRED_COAL_BLOCK);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(SoulFiredCoal.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SoulFiredCoal.LOGGER.info(String.format("[%s] Registering items",SoulFiredCoal.MOD_ID));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(ModItems::addItemsToIngredientsItemGroup);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ModItems::addItemsToNaturalBlocksItemGroup);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(ModItems::addItemsToBuildingBlocksItemGroup);
    }
}
