package com.leclowndu93150.soulfiredcoal.client;

import com.leclowndu93150.soulfiredcoal.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SoulfiredcoalClient implements ClientModInitializer {

    private static final Map<Block, ResourceLocation> EMISSIVE_BLOCKS = new HashMap<>();

    static {
        EMISSIVE_BLOCKS.put(ModBlocks.SOUL_SAND_SOUL_FIRED_COAL_ORE,
                new ResourceLocation("soulfiredcoal", "block/soul_sand_soul_fired_coal_ore_e"));
        EMISSIVE_BLOCKS.put(ModBlocks.SOUL_SOIL_SOUL_FIRED_COAL_ORE,
                new ResourceLocation("soulfiredcoal", "block/soul_soil_soul_fired_coal_ore_e"));
        EMISSIVE_BLOCKS.put(ModBlocks.SOUL_FIRED_COAL_BLOCK,
                new ResourceLocation("soulfiredcoal", "block/soul_fired_coal_block_e"));
    }

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_SAND_SOUL_FIRED_COAL_ORE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_SOIL_SOUL_FIRED_COAL_ORE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_FIRED_COAL_BLOCK, RenderType.cutout());

        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(
                    new ResourceLocation("soulfiredcoal", "block/soul_sand_soul_fired_coal_ore_e"),
                    new ResourceLocation("soulfiredcoal", "block/soul_soil_soul_fired_coal_ore_e"),
                    new ResourceLocation("soulfiredcoal", "block/soul_fired_coal_block_e")
            );

            pluginContext.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, context) -> {
                ResourceLocation modelId = context.id();

                for (Map.Entry<Block, ResourceLocation> entry : EMISSIVE_BLOCKS.entrySet()) {
                    String blockId = "soulfiredcoal:block/" + getBlockId(entry.getKey());
                    if (modelId.toString().contains(blockId)) {
                        Function<Material, TextureAtlasSprite> spriteGetter = context.textureGetter();
                        if (spriteGetter != null) {
                            Material emissiveMaterial = new Material(TextureAtlas.LOCATION_BLOCKS, entry.getValue());
                            TextureAtlasSprite emissiveSprite = spriteGetter.apply(emissiveMaterial);
                            if (emissiveSprite != null) {
                                return new EmissiveBakedModel(model, emissiveSprite);
                            }
                        }
                    }
                }

                return model;
            });
        });
    }

    private static String getBlockId(Block block) {
        if (block == ModBlocks.SOUL_SAND_SOUL_FIRED_COAL_ORE) {
            return "soul_sand_soul_fired_coal_ore";
        } else if (block == ModBlocks.SOUL_SOIL_SOUL_FIRED_COAL_ORE) {
            return "soul_soil_soul_fired_coal_ore";
        } else if (block == ModBlocks.SOUL_FIRED_COAL_BLOCK) {
            return "soul_fired_coal_block";
        }
        return "";
    }
}
