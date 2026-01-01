package com.leclowndu93150.soulfiredcoal.client;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class EmissiveBakedModel extends ForwardingBakedModel {

    private final TextureAtlasSprite emissiveSprite;
    private final RenderMaterial emissiveMaterial;

    public EmissiveBakedModel(BakedModel baseModel, TextureAtlasSprite emissiveSprite) {
        this.wrapped = baseModel;
        this.emissiveSprite = emissiveSprite;

        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        if (renderer != null) {
            MaterialFinder finder = renderer.materialFinder();
            this.emissiveMaterial = finder
                    .blendMode(BlendMode.CUTOUT)
                    .emissive(true)
                    .find();
        } else {
            this.emissiveMaterial = null;
        }
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
            Supplier<RandomSource> randomSupplier, RenderContext context) {
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);

        if (emissiveMaterial != null && emissiveSprite != null) {
            QuadEmitter emitter = context.getEmitter();

            for (Direction direction : Direction.values()) {
                emitEmissiveFace(emitter, direction);
            }
        }
    }

    private void emitEmissiveFace(QuadEmitter emitter, Direction face) {
        emitter.square(face, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
        emitter.spriteBake(emissiveSprite, MutableQuadView.BAKE_LOCK_UV);
        emitter.color(-1, -1, -1, -1);
        emitter.material(emissiveMaterial);
        emitter.emit();
    }
}
