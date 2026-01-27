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

public class SoulFiredCoal implements ModInitializer {

    public static final String MOD_ID = "soulfiredcoal";

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();

        // For some reason 3200 exactly doesn't cook 32 items fully
        // Likely a timing bug with soul fired coal reducing item cook time slightly too late
        // Adding a bit of extra cook time to account for this
        FuelRegistry.INSTANCE.add(ModItems.SOUL_FIRED_COAL, 3201);

        FuelRegistry.INSTANCE.add(ModItems.SOUL_FIRED_COAL_BLOCK, 32001);

        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MOD_ID, "ore_soul_fired_coal"))
        );
    }
}
