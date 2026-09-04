package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.TheInfinitum;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to disable The Infinitum when configured
 */
@Mixin(value = TheInfinitum.class, remap = false)
public abstract class MixinTheInfinitum {

    /**
     * Disable attack damage when The Infinitum is disabled
     */
    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableAttack(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.THE_INFINITUM_ENABLED.get()) {
            // Prevent the weapon from dealing its special damage
            cir.setReturnValue(false);
        }
    }
}
