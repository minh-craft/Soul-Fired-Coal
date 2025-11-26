package com.minhcraft.block;

import com.minhcraft.SoulFiredCoal;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;

public class ModBlocks {
    public static final Block SOUL_SAND_SOUL_FIRED_COAL_ORE = registerBlock("soul_sand_soul_fired_coal_ore",
            new DropExperienceBlock(FabricBlockSettings.copyOf(Blocks.SOUL_SAND), UniformInt.of(0, 1)));

    public static final Block SOUL_SOIL_SOUL_FIRED_COAL_ORE = registerBlock("soul_soil_soul_fired_coal_ore",
            new DropExperienceBlock(FabricBlockSettings.copyOf(Blocks.SOUL_SOIL), UniformInt.of(0, 1)));

    public static final Block SOUL_FIRED_COAL_BLOCK = registerBlock("soul_fired_coal_block",
            new Block(FabricBlockSettings.copyOf(Blocks.COAL_BLOCK)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(SoulFiredCoal.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(SoulFiredCoal.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        SoulFiredCoal.LOGGER.info(String.format("[%s] Registering blocks", SoulFiredCoal.MOD_ID));
    }
}
