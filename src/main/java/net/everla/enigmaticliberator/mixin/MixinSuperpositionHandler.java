package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin into SuperpositionHandler to control Abyssal Artifacts access
 */
@Mixin(value = SuperpositionHandler.class, remap = false)
public abstract class MixinSuperpositionHandler {

    /**
     * BLESSING 7: Abyssal Artifacts
     * Prevent access to Abyssal Artifacts when disabled in config
     */
    @Inject(method = "canUseEldritch", at = @At("HEAD"), cancellable = true, remap = false)
    private static void preventAbyssalArtifactsUsage(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.ABYSSAL_ARTIFACTS_ENABLED.get()) {
            // Deny access to Abyssal Artifacts
            cir.setReturnValue(false);
        }
    }

    /**
     * BLESSING 7: Abyssal Artifacts - Modify time requirement
     * Adjust the required curse time fraction when blessing is enabled
     */
    @Inject(method = "canUseEldritch", at = @At("RETURN"), cancellable = true, remap = false)
    private static void adjustAbyssalTimeRequirement(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (BlessingConfig.ABYSSAL_ARTIFACTS_ENABLED.get()) {
            // If user configured a different time requirement, we need to recalculate
            double configuredFraction = BlessingConfig.ABYSSAL_REQUIRED_TIME_FRACTION.get();

            // If configured to 0, allow immediate access
            if (configuredFraction <= 0.0) {
                if (SuperpositionHandler.isTheCursedOne(player)) {
                    cir.setReturnValue(true);
                }
            }
            // For other values, the original method handles it via CursedRing.superCursedTime config
            // which should be synced with our config value
        }
    }
}
