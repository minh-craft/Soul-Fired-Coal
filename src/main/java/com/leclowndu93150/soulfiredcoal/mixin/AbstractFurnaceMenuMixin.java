package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.api.LastFuelTypeAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceMenu.class)
public abstract class AbstractFurnaceMenuMixin extends AbstractContainerMenu implements LastFuelTypeAccessor {

    protected AbstractFurnaceMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Unique
    private SimpleContainerData soulfiredcoal$extraData;

    @Inject(method = "<init>(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/inventory/RecipeBookType;ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("RETURN"))
    private void soulfiredcoal$expandContainerDataClient(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int containerId, Inventory inventory, CallbackInfo ci) {
        this.soulfiredcoal$extraData = new SimpleContainerData(1);
        this.addDataSlot(DataSlot.forContainer(this.soulfiredcoal$extraData, 0));
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
        if (this.soulfiredcoal$extraData != null) {
            this.soulfiredcoal$extraData.set(0, type);
        }
    }
}
