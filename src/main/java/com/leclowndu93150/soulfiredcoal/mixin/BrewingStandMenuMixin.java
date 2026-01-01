package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.api.LastFuelTypeAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingStandMenu.class)
public abstract class BrewingStandMenuMixin extends AbstractContainerMenu implements LastFuelTypeAccessor {

    protected BrewingStandMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Unique
    private SimpleContainerData soulfiredcoal$extraData;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("RETURN"))
    private void soulfiredcoal$expandContainerData(int containerId, Inventory inventory, CallbackInfo ci) {
        this.soulfiredcoal$extraData = new SimpleContainerData(1);
        super.addDataSlot(DataSlot.forContainer(this.soulfiredcoal$extraData, 0));
    }

    @Override
    @Unique
    public int soulfiredcoal$getLastFuelType() {
        if (this.soulfiredcoal$extraData != null) {
            return this.soulfiredcoal$extraData.get(0);
        }
        return 0;
    }

    @Override
    @Unique
    public void soulfiredcoal$setLastFuelType(int type) {
    }
}
