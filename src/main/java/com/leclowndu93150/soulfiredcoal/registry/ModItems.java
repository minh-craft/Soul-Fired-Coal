package com.leclowndu93150.soulfiredcoal.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static final Item SOUL_FIRED_COAL = register("soul_fired_coal",
            new Item(new Item.Properties()));

    public static final Item SOUL_SAND_SOUL_FIRED_COAL_ORE = register("soul_sand_soul_fired_coal_ore",
            new BlockItem(ModBlocks.SOUL_SAND_SOUL_FIRED_COAL_ORE, new Item.Properties()));

    public static final Item SOUL_SOIL_SOUL_FIRED_COAL_ORE = register("soul_soil_soul_fired_coal_ore",
            new BlockItem(ModBlocks.SOUL_SOIL_SOUL_FIRED_COAL_ORE, new Item.Properties()));

    public static final Item SOUL_FIRED_COAL_BLOCK = register("soul_fired_coal_block",
            new BlockItem(ModBlocks.SOUL_FIRED_COAL_BLOCK, new Item.Properties()));

    public static final CreativeModeTab SOUL_FIRED_COAL_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation("soulfiredcoal", "soul_fired_coal_tab"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(SOUL_FIRED_COAL))
                    .title(Component.translatable("itemGroup.soulfiredcoal.soul_fired_coal_tab"))
                    .displayItems((context, entries) -> {
                        entries.accept(SOUL_FIRED_COAL);
                        entries.accept(SOUL_SAND_SOUL_FIRED_COAL_ORE);
                        entries.accept(SOUL_SOIL_SOUL_FIRED_COAL_ORE);
                        entries.accept(SOUL_FIRED_COAL_BLOCK);
                    })
                    .build()
    );

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("soulfiredcoal", name), item);
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(SOUL_FIRED_COAL);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.accept(SOUL_SAND_SOUL_FIRED_COAL_ORE);
            entries.accept(SOUL_SOIL_SOUL_FIRED_COAL_ORE);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.accept(SOUL_FIRED_COAL_BLOCK);
        });
    }
}
