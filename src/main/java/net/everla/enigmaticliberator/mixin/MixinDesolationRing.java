package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.DesolationRing;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

/**
 * Keeps the Desolation Ring's binding behavior under the unique relic master switch.
 */
@Mixin(value = DesolationRing.class, remap = false)
public abstract class MixinDesolationRing {

    @Inject(method = "canUnequip", at = @At("HEAD"), cancellable = true, remap = false)
    private void allowUnequipWhenDisabled(SlotContext slotContext, ItemStack stack,
                                          CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canEquipFromUse", at = @At("HEAD"), cancellable = true, remap = false)
    private void preventEquipWhenDisabled(SlotContext slotContext, ItemStack stack,
                                          CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getDropRule", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableKeepDropRule(SlotContext slotContext, net.minecraft.world.damagesource.DamageSource source,
                                     int lootingLevel, boolean recentlyHit, ItemStack stack,
                                     CallbackInfoReturnable<ICurio.DropRule> cir) {
        if (!BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
            cir.setReturnValue(ICurio.DropRule.DEFAULT);
        }
    }
}
