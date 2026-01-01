package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.api.LastFuelTypeAccessor;
import com.leclowndu93150.soulfiredcoal.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin implements LastFuelTypeAccessor {

    @Shadow
    @Final
    protected ContainerData dataAccess;

    @Unique
    private int soulfiredcoal$lastFuelType = 0;

    @Unique
    private ContainerData soulfiredcoal$wrappedDataAccess = null;

    @Unique
    private ContainerData soulfiredcoal$getWrappedDataAccess() {
        if (soulfiredcoal$wrappedDataAccess == null) {
            ContainerData original = this.dataAccess;
            BrewingStandBlockEntityMixin self = this;
            soulfiredcoal$wrappedDataAccess = new ContainerData() {
                @Override
                public int get(int index) {
                    if (index == 2) {
                        return self.soulfiredcoal$lastFuelType;
                    }
                    return original.get(index);
                }

                @Override
                public void set(int index, int value) {
                    if (index == 2) {
                        self.soulfiredcoal$lastFuelType = value;
                    } else {
                        original.set(index, value);
                    }
                }

                @Override
                public int getCount() {
                    return 3;
                }
            };
        }
        return soulfiredcoal$wrappedDataAccess;
    }

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void soulfiredcoal$checkSoulFiredCoalFuel(Level level, BlockPos pos, BlockState state,
            BrewingStandBlockEntity blockEntity, CallbackInfo ci) {
        BrewingStandBlockEntityAccessor accessor = (BrewingStandBlockEntityAccessor) blockEntity;
        LastFuelTypeAccessor fuelTypeAccessor = (LastFuelTypeAccessor) blockEntity;
        ItemStack fuelStack = accessor.getItems().get(4);

        if (accessor.getFuel() <= 0) {
            if (fuelStack.is(ModItems.SOUL_FIRED_COAL)) {
                accessor.setFuel(20);
                fuelStack.shrink(1);
                fuelTypeAccessor.soulfiredcoal$setLastFuelType(1);
                blockEntity.setChanged();
            }
        }

        if (!fuelStack.isEmpty()) {
            int newType = fuelStack.is(ModItems.SOUL_FIRED_COAL) ? 1 : 0;
            if (fuelTypeAccessor.soulfiredcoal$getLastFuelType() != newType) {
                fuelTypeAccessor.soulfiredcoal$setLastFuelType(newType);
                blockEntity.setChanged();
            }
        }
    }

    @Inject(method = "canPlaceItem", at = @At("HEAD"), cancellable = true)
    private void soulfiredcoal$allowSoulFiredCoalInFuelSlot(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot == 4 && stack.is(ModItems.SOUL_FIRED_COAL)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void soulfiredcoal$loadFuelType(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("SoulFiredCoalLastFuelType")) {
            this.soulfiredcoal$lastFuelType = tag.getByte("SoulFiredCoalLastFuelType");
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void soulfiredcoal$saveFuelType(CompoundTag tag, CallbackInfo ci) {
        tag.putByte("SoulFiredCoalLastFuelType", (byte) this.soulfiredcoal$lastFuelType);
    }

    @Unique
    public int soulfiredcoal$getLastFuelType() {
        return this.soulfiredcoal$lastFuelType;
    }

    @Unique
    public void soulfiredcoal$setLastFuelType(int type) {
        this.soulfiredcoal$lastFuelType = type;
    }

    @Inject(method = "createMenu", at = @At("HEAD"), cancellable = true)
    private void soulfiredcoal$createMenuWithExtendedData(int containerId, Inventory inventory, CallbackInfoReturnable<AbstractContainerMenu> cir) {
        cir.setReturnValue(new BrewingStandMenu(containerId, inventory, (BrewingStandBlockEntity) (Object) this, soulfiredcoal$getWrappedDataAccess()));
    }
}
