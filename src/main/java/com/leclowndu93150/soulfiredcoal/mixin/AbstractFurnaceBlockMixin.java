package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.registry.ModBlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlock.class)
public abstract class AbstractFurnaceBlockMixin extends Block {

    protected AbstractFurnaceBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void soulfiredcoal$addSoulLitProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(ModBlockStateProperties.SOUL_LIT);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void soulfiredcoal$setDefaultSoulLitState(BlockBehaviour.Properties properties, CallbackInfo ci) {
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AbstractFurnaceBlock.FACING, Direction.NORTH)
                .setValue(AbstractFurnaceBlock.LIT, false)
                .setValue(ModBlockStateProperties.SOUL_LIT, false));
    }
}
