package com.leclowndu93150.soulfiredcoal.mixin;

import com.leclowndu93150.soulfiredcoal.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecartFurnace.class)
public abstract class MinecartFurnaceMixin {

    @Shadow
    private int fuel;

    @Shadow
    public double xPush;

    @Shadow
    public double zPush;

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void soulfiredcoal$acceptSoulFiredCoalFuel(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        MinecartFurnace self = (MinecartFurnace) (Object) this;

        if (itemstack.is(ModItems.SOUL_FIRED_COAL) && this.fuel + 14400 <= 32000) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            this.fuel += 14400; // 4x coal's 3600 ticks

            if (this.fuel > 0) {
                this.xPush = self.getX() - player.getX();
                this.zPush = self.getZ() - player.getZ();
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(self.level().isClientSide));
        }
    }
}
