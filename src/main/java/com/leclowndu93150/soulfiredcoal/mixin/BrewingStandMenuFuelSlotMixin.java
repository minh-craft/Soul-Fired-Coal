package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.BrewingStandMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandMenu.FuelSlot.class)
public class BrewingStandMenuFuelSlotMixin {

    @Inject(method = "mayPlaceItem", at = @At("HEAD"), cancellable = true)
    private static void soulfiredcoal$allowSoulFiredCoal(ItemStack itemstack, CallbackInfoReturnable<Boolean> cir) {
        if (itemstack.is(ModItems.SOUL_FIRED_COAL)) {
            cir.setReturnValue(true);
        }
    }
}
