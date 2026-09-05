package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.SoulCrystal;
import net.everla.enigmaticliberator.config.CurseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Applies the configurable Sixth Curse health penalty to lost soul crystals. */
@Mixin(value = SoulCrystal.class, remap = false)
public abstract class MixinSoulCrystal {

    @ModifyConstant(
            method = "updatePlayerSoulMap",
            constant = @Constant(floatValue = -0.1F),
            remap = false
    )
    private static float applyConfiguredHealthLoss(float original) {
        if (!CurseConfig.SIXTH_CURSE_ENABLED.get()) {
            return 0.0F;
        }

        // The original -0.1 value represents one half-heart per 20 health points.
        return -(CurseConfig.SIXTH_CURSE_MAX_HEALTH_LOSS.get().floatValue() / 20.0F);
    }
}
