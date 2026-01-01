package com.leclowndu93150.soulfiredcoal;

import com.leclowndu93150.soulfiredcoal.registry.ModBlocks;
import com.leclowndu93150.soulfiredcoal.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;

public class Soulfiredcoal implements ModInitializer {

    public static final String MOD_ID = "soulfiredcoal";

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();

        FuelRegistry.INSTANCE.add(ModItems.SOUL_FIRED_COAL, 6400);

        FuelRegistry.INSTANCE.add(ModItems.SOUL_FIRED_COAL_BLOCK, 64000);

        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MOD_ID, "ore_soul_fired_coal"))
        );
    }
}
