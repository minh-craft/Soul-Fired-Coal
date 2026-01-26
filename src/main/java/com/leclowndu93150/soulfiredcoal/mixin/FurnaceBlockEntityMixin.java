package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.api.LastFuelTypeAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntityMixin {

    @Inject(method = "createMenu", at = @At("HEAD"), cancellable = true)
    private void soulfiredcoal$createMenuWithExtendedData(int containerId, Inventory inventory, CallbackInfoReturnable<AbstractContainerMenu> cir) {
        FurnaceBlockEntity self = (FurnaceBlockEntity) (Object) this;
        LastFuelTypeAccessor accessor = (LastFuelTypeAccessor) self;
        cir.setReturnValue(new FurnaceMenu(containerId, inventory, self, accessor.soulfiredcoal$getWrappedDataAccess()));
    }
}
