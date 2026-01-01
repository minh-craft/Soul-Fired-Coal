package com.leclowndu93150.soulfiredcoal.mixin.client;

import com.leclowndu93150.soulfiredcoal.api.LastFuelTypeAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BrewingStandMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;

@Mixin(BrewingStandScreen.class)
public abstract class BrewingStandScreenMixin extends AbstractContainerScreen<BrewingStandMenu> {

    @Unique
    private static final ResourceLocation BREWING_STAND_LOCATION = new ResourceLocation("textures/gui/container/brewing_stand.png");

    @Unique
    private static final ResourceLocation SOUL_FUEL_BAR_LOCATION = new ResourceLocation("soulfiredcoal", "textures/gui/soul_fuel_bar.png");

    @Unique
    private static boolean soulFuelBarGenerated = false;

    public BrewingStandScreenMixin(BrewingStandMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Unique
    private static void generateSoulFuelBarTexture() {
        if (soulFuelBarGenerated) return;

        try {
            Minecraft mc = Minecraft.getInstance();
            Resource resource = mc.getResourceManager().getResource(BREWING_STAND_LOCATION).orElse(null);
            if (resource == null) return;

            try (InputStream stream = resource.open()) {
                NativeImage original = NativeImage.read(stream);

                NativeImage fuelBar = new NativeImage(18, 4, false);

                int soulR = 7;
                int soulG = 202;
                int soulB = 208;

                for (int x = 0; x < 18; x++) {
                    for (int y = 0; y < 4; y++) {
                        int pixel = original.getPixelRGBA(176 + x, 29 + y);

                        int a = (pixel >> 24) & 0xFF;
                        int b = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int r = pixel & 0xFF;

                        float gray = (0.299f * r + 0.587f * g + 0.114f * b) / 255f;

                        int newR = (int) (gray * soulR);
                        int newG = (int) (gray * soulG);
                        int newB = (int) (gray * soulB);

                        int newPixel = (a << 24) | (newB << 16) | (newG << 8) | newR;
                        fuelBar.setPixelRGBA(x, y, newPixel);
                    }
                }

                original.close();

                DynamicTexture dynamicTexture = new DynamicTexture(fuelBar);
                mc.getTextureManager().register(SOUL_FUEL_BAR_LOCATION, dynamicTexture);

                soulFuelBarGenerated = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Mth;clamp(III)I", shift = At.Shift.AFTER), cancellable = true)
    private void soulfiredcoal$renderBlueFuelBar(GuiGraphics guigraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        int lastFuelType = ((LastFuelTypeAccessor) this.menu).soulfiredcoal$getLastFuelType();

        if (lastFuelType == 1) {
            generateSoulFuelBarTexture();

            int k = this.leftPos;
            int l = this.topPos;

            int fuel = this.menu.getFuel();
            int fuelWidth = Mth.clamp((18 * fuel + 20 - 1) / 20, 0, 18);

            if (fuelWidth > 0 && soulFuelBarGenerated) {
                guigraphics.blit(SOUL_FUEL_BAR_LOCATION, k + 60, l + 44, 0, 0, fuelWidth, 4, 18, 4);
            }

            int brewingTicks = this.menu.getBrewingTicks();
            if (brewingTicks > 0) {
                int arrowHeight = (int)(28.0F * (1.0F - brewingTicks / 400.0F));
                if (arrowHeight > 0) {
                    guigraphics.blit(BREWING_STAND_LOCATION, k + 97, l + 16, 176, 0, 9, arrowHeight);
                }

                int[] bubbleLengths = new int[]{29, 24, 20, 16, 11, 6, 0};
                int bubbleHeight = bubbleLengths[brewingTicks / 2 % 7];
                if (bubbleHeight > 0) {
                    guigraphics.blit(BREWING_STAND_LOCATION, k + 63, l + 14 + 29 - bubbleHeight, 185, 29 - bubbleHeight, 12, bubbleHeight);
                }
            }

            ci.cancel();
        }
    }
}
