package com.leclowndu93150.soulfiredcoal.mixin.client;

import com.leclowndu93150.soulfiredcoal.api.LastFuelTypeAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;

@Mixin(AbstractFurnaceScreen.class)
public abstract class AbstractFurnaceScreenMixin<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T> {

    @Shadow
    @Final
    private ResourceLocation texture;

    @Unique
    private static final ResourceLocation SOUL_FIRE_LOCATION = new ResourceLocation("soulfiredcoal", "textures/gui/soul_furnace_fire.png");

    @Unique
    private static boolean soulFireTextureGenerated = false;

    @Unique
    private static ResourceLocation soulfiredcoal$lastTextureSource = null;

    public AbstractFurnaceScreenMixin(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Unique
    private void soulfiredcoal$generateSoulFireTexture() {
        if (soulFireTextureGenerated && this.texture.equals(soulfiredcoal$lastTextureSource)) return;

        try {
            Minecraft mc = Minecraft.getInstance();
            Resource resource = mc.getResourceManager().getResource(this.texture).orElse(null);
            if (resource == null) return;

            try (InputStream stream = resource.open()) {
                NativeImage original = NativeImage.read(stream);

                NativeImage soulTexture = new NativeImage(256, 256, true);

                int soulR = 7;
                int soulG = 202;
                int soulB = 208;

                for (int x = 0; x < 14; x++) {
                    for (int y = 0; y < 14; y++) {
                        int pixel = original.getPixelRGBA(176 + x, y);

                        int a = (pixel >> 24) & 0xFF;
                        int b = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int r = pixel & 0xFF;

                        // Skip transparent pixels or GUI background gray (198, 198, 198)
                        if (a == 0 || (r == 198 && g == 198 && b == 198)) {
                            soulTexture.setPixelRGBA(176 + x, y, 0);
                            continue;
                        }

                        // Tint white pixels directly to cyan
                        if (r == 255 && g == 255 && b == 255) {
                            int cyanPixel = (a << 24) | (soulB << 16) | (soulG << 8) | soulR;
                            soulTexture.setPixelRGBA(176 + x, y, cyanPixel);
                            continue;
                        }

                        float gray = (0.299f * r + 0.587f * g + 0.114f * b) / 255f;

                        int newR = (int) (gray * soulR);
                        int newG = (int) (gray * soulG);
                        int newB = (int) (gray * soulB);

                        int newPixel = (a << 24) | (newB << 16) | (newG << 8) | newR;
                        soulTexture.setPixelRGBA(176 + x, y, newPixel);
                    }
                }

                original.close();

                DynamicTexture dynamicTexture = new DynamicTexture(soulTexture);
                mc.getTextureManager().register(SOUL_FIRE_LOCATION, dynamicTexture);

                soulFireTextureGenerated = true;
                soulfiredcoal$lastTextureSource = this.texture;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/AbstractFurnaceMenu;isLit()Z",
            shift = At.Shift.BEFORE))
    private void soulfiredcoal$beforeFireRender(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        int lastFuelType = ((LastFuelTypeAccessor) this.menu).soulfiredcoal$getLastFuelType();
        if (lastFuelType == 1) {
            soulfiredcoal$generateSoulFireTexture();
        }
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
            ordinal = 1,
            shift = At.Shift.AFTER))
    private void soulfiredcoal$renderSoulFire(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        int lastFuelType = ((LastFuelTypeAccessor) this.menu).soulfiredcoal$getLastFuelType();

        if (lastFuelType == 1 && soulFireTextureGenerated && this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            if (litProgress > 0) {
                guiGraphics.blit(SOUL_FIRE_LOCATION, this.leftPos + 56, this.topPos + 36 + 12 - litProgress, 176, 12 - litProgress, 14, litProgress + 1);
            }
        }
    }
}
