package com.leclowndu93150.soulfiredcoal.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SoulSoilSoulFiredCoalOreBlock extends Block {

    private final UniformInt xpRange = UniformInt.of(0, 2);

    public SoulSoilSoulFiredCoalOreBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            this.tryDropExperience(world, pos, tool, this.xpRange);
        }
    }

    private void tryDropExperience(ServerLevel world, BlockPos pos, ItemStack tool, UniformInt xpRange) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0) {
            int xp = xpRange.sample(world.random);
            if (xp > 0) {
                this.popExperience(world, pos, xp);
            }
        }
    }
}
