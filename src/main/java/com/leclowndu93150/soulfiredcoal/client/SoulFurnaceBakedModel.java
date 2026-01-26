package com.leclowndu93150.soulfiredcoal.client;

import com.leclowndu93150.soulfiredcoal.registry.ModBlockStateProperties;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class SoulFurnaceBakedModel extends ForwardingBakedModel {

    private final TextureAtlasSprite soulFrontSprite;

    public SoulFurnaceBakedModel(BakedModel baseModel, TextureAtlasSprite soulFrontSprite) {
        this.wrapped = baseModel;
        this.soulFrontSprite = soulFrontSprite;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                               Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (soulFrontSprite == null || RendererAccess.INSTANCE.getRenderer() == null) {
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            return;
        }

        boolean isSoulFuel = state.getValue(AbstractFurnaceBlock.LIT)
                && state.hasProperty(ModBlockStateProperties.SOUL_LIT)
                && state.getValue(ModBlockStateProperties.SOUL_LIT);

        if (!isSoulFuel) {
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            return;
        }

        Direction facing = state.getValue(AbstractFurnaceBlock.FACING);

        context.pushTransform(quad -> {
            Direction quadFace = quad.lightFace();
            if (quadFace == facing) {
                quad.spriteBake(soulFrontSprite, MutableQuadView.BAKE_LOCK_UV);
            }
            return true;
        });

        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
    }
}
