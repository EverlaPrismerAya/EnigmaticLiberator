package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Replaces the shared qualification threshold used by abyssal artifacts.
 */
@Mixin(value = SuperpositionHandler.class, remap = false)
public abstract class MixinSuperpositionHandler {

    @Inject(method = "isTheWorthyOne", at = @At("HEAD"), cancellable = true, remap = false)
    private static void useConfiguredSufferingThreshold(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!net.everla.enigmaticliberator.config.BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
            cir.setReturnValue(false);
            return;
        }

        cir.setReturnValue(
                SuperpositionHandler.isTheCursedOne(player)
                        && SuperpositionHandler.getSufferingFraction(player) >= ExtraConfig.SUPER_CURSED_TIME.get()
        );
    }
}
