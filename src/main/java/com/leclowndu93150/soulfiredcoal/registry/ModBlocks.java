package com.leclowndu93150.soulfiredcoal.registry;

import com.leclowndu93150.soulfiredcoal.block.SoulSandSoulFiredCoalOreBlock;
import com.leclowndu93150.soulfiredcoal.block.SoulSoilSoulFiredCoalOreBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {

    public static final Block SOUL_SAND_SOUL_FIRED_COAL_ORE = register("soul_sand_soul_fired_coal_ore",
            new SoulSandSoulFiredCoalOreBlock(
                    FabricBlockSettings.create()
                            .mapColor(MapColor.COLOR_BROWN)
                            .strength(1.0F)
                            .speedFactor(0.4F)
                            .sound(SoundType.SOUL_SAND)
                            .emissiveRendering((state, world, pos) -> true)
            ));

    public static final Block SOUL_SOIL_SOUL_FIRED_COAL_ORE = register("soul_soil_soul_fired_coal_ore",
            new SoulSoilSoulFiredCoalOreBlock(
                    FabricBlockSettings.create()
                            .mapColor(MapColor.COLOR_BROWN)
                            .strength(1.0F)
                            .sound(SoundType.SOUL_SOIL)
                            .emissiveRendering((state, world, pos) -> true)
            ));

    public static final Block SOUL_FIRED_COAL_BLOCK = register("soul_fired_coal_block",
            new Block(
                    FabricBlockSettings.create()
                            .mapColor(MapColor.COLOR_BLACK)
                            .strength(5.0F, 6.0F)
                            .requiresTool()
                            .sound(SoundType.STONE)
                            .emissiveRendering((state, world, pos) -> true)
            ));

    private static Block register(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation("soulfiredcoal", name), block);
    }

    public static void init() {
    }
}
