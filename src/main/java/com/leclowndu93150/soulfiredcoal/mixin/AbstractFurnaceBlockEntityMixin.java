package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

    @Shadow
    protected NonNullList<ItemStack> items;

    @Shadow
    int litTime;

    @Shadow
    int cookingProgress;

    @Shadow
    int cookingTotalTime;

    @Unique
    private boolean soulfiredcoal$isSoulFiredCoalFuel = false;

    @Unique
    private int soulfiredcoal$originalCookTime = -1;

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void soulfiredcoal$trackFuelAndAdjustCookTime(Level level, BlockPos pos, BlockState state,
            AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        AbstractFurnaceBlockEntityMixin self = (AbstractFurnaceBlockEntityMixin) (Object) blockEntity;

        ItemStack fuelStack = self.items.get(1);

        if (self.litTime <= 0 && !fuelStack.isEmpty()) {
            boolean wasSoulFuel = self.soulfiredcoal$isSoulFiredCoalFuel;
            boolean isSoulFuel = fuelStack.is(ModItems.SOUL_FIRED_COAL) || fuelStack.is(ModItems.SOUL_FIRED_COAL_BLOCK.asItem());

            if (isSoulFuel && !wasSoulFuel) {
                self.soulfiredcoal$isSoulFiredCoalFuel = true;
                if (self.cookingTotalTime > 0) {
                    self.soulfiredcoal$originalCookTime = self.cookingTotalTime;
                    self.cookingTotalTime = self.cookingTotalTime / 2;
                    self.cookingProgress = self.cookingProgress / 2;
                }
            } else if (!isSoulFuel && wasSoulFuel) {
                self.soulfiredcoal$isSoulFiredCoalFuel = false;
                if (self.soulfiredcoal$originalCookTime > 0) {
                    self.cookingTotalTime = self.soulfiredcoal$originalCookTime;
                    self.cookingProgress = self.cookingProgress * 2;
                    self.soulfiredcoal$originalCookTime = -1;
                }
            }
        }

        if (self.soulfiredcoal$isSoulFiredCoalFuel && self.litTime > 0 && self.cookingProgress == 0 && self.cookingTotalTime > 0) {
            if (self.soulfiredcoal$originalCookTime != self.cookingTotalTime * 2) {
                self.soulfiredcoal$originalCookTime = self.cookingTotalTime;
                self.cookingTotalTime = self.cookingTotalTime / 2;
            }
        }
    }

    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void soulfiredcoal$checkFuelBurnout(Level level, BlockPos pos, BlockState state,
            AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        AbstractFurnaceBlockEntityMixin self = (AbstractFurnaceBlockEntityMixin) (Object) blockEntity;

        if (self.litTime <= 0) {
            ItemStack fuelStack = self.items.get(1);
            boolean hasSoulFuel = !fuelStack.isEmpty() &&
                (fuelStack.is(ModItems.SOUL_FIRED_COAL) || fuelStack.is(ModItems.SOUL_FIRED_COAL_BLOCK.asItem()));

            if (!hasSoulFuel && self.soulfiredcoal$isSoulFiredCoalFuel) {
                self.soulfiredcoal$isSoulFiredCoalFuel = false;
                if (self.soulfiredcoal$originalCookTime > 0 && self.cookingTotalTime > 0) {
                    self.cookingTotalTime = self.soulfiredcoal$originalCookTime;
                    self.cookingProgress = Math.min(self.cookingProgress * 2, self.cookingTotalTime);
                }
                self.soulfiredcoal$originalCookTime = -1;
            }
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void soulfiredcoal$saveFuelType(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean("SoulFiredCoalFuel", this.soulfiredcoal$isSoulFiredCoalFuel);
        tag.putInt("SoulFiredCoalOriginalCookTime", this.soulfiredcoal$originalCookTime);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void soulfiredcoal$loadFuelType(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("SoulFiredCoalFuel")) {
            this.soulfiredcoal$isSoulFiredCoalFuel = tag.getBoolean("SoulFiredCoalFuel");
        }
        if (tag.contains("SoulFiredCoalOriginalCookTime")) {
            this.soulfiredcoal$originalCookTime = tag.getInt("SoulFiredCoalOriginalCookTime");
        }
    }
}
