package com.leclowndu93150.soulfiredcoal.mixin.client;

import com.leclowndu93150.soulfiredcoal.api.LastFuelTypeAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceScreen.class)
public abstract class AbstractFurnaceScreenMixin<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T> {

    @Unique
    private static final ResourceLocation SOUL_FIRE_TEXTURE = new ResourceLocation("soulfiredcoal", "textures/gui/soul_furnace_fire.png");

    public AbstractFurnaceScreenMixin(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
            ordinal = 1,
            shift = At.Shift.AFTER))
    private void soulfiredcoal$renderSoulFire(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        int lastFuelType = ((LastFuelTypeAccessor) this.menu).soulfiredcoal$getLastFuelType();

        if (lastFuelType == 1 && this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            if (litProgress > 0) {
                guiGraphics.blit(SOUL_FIRE_TEXTURE, this.leftPos + 56, this.topPos + 36 + 12 - litProgress, 0, 12 - litProgress, 14, litProgress + 1, 14, 14);
            }
        }
    }
}
