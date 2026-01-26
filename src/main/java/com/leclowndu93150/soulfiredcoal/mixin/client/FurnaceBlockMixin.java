package com.leclowndu93150.soulfiredcoal.mixin.client;

import com.leclowndu93150.soulfiredcoal.registry.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin {

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void soulfiredcoal$useSoulFlameParticles(BlockState blockstate, Level level, BlockPos blockpos, RandomSource randomsource, CallbackInfo ci) {
        if (blockstate.getValue(AbstractFurnaceBlock.LIT) &&
                blockstate.hasProperty(ModBlockStateProperties.SOUL_LIT) &&
                blockstate.getValue(ModBlockStateProperties.SOUL_LIT)) {
            double d0 = blockpos.getX() + 0.5;
            double d1 = blockpos.getY();
            double d2 = blockpos.getZ() + 0.5;

            Direction direction = blockstate.getValue(AbstractFurnaceBlock.FACING);
            Direction.Axis axis = direction.getAxis();
            double d4 = randomsource.nextDouble() * 0.6 - 0.3;
            double d5 = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : d4;
            double d6 = randomsource.nextDouble() * 6.0 / 16.0;
            double d7 = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : d4;

            level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);

            ci.cancel();
        }
    }
}
