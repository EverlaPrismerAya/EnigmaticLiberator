package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.DesolationRing;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

/**
 * Mixin to disable Desolation Ring when configured
 */
@Mixin(value = DesolationRing.class, remap = false)
public abstract class MixinDesolationRing {

    /**
     * Allow unequipping when Desolation Ring is disabled (removes binding)
     */
    @Inject(method = "canUnequip", at = @At("HEAD"), cancellable = true, remap = false)
    private void allowUnequip(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.DESOLATION_RING_ENABLED.get()) {
            // Allow unequipping - removes the binding curse
            cir.setReturnValue(true);
        }
    }

    /**
     * Prevent equipping from use when Desolation Ring is disabled
     */
    @Inject(method = "canEquipFromUse", at = @At("HEAD"), cancellable = true, remap = false)
    private void preventEquipFromUse(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.DESOLATION_RING_ENABLED.get()) {
            // Prevent auto-equipping
            cir.setReturnValue(false);
        }
    }
}
