package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import net.everla.enigmaticliberator.config.CurseConfig;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Applies the Sixth Curse settings to Enigmatic Legacy's soul-loss drop check. */
@Mixin(value = EnigmaticEventHandler.class, remap = false)
public abstract class MixinEnigmaticEventHandlerSoulCurse {

    @Redirect(
            method = "onLivingDropsLowest",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/aizistral/enigmaticlegacy/handlers/SuperpositionHandler;canDropSoulCrystal(Lnet/minecraft/world/entity/player/Player;Z)Z"
            ),
            remap = false
    )
    private boolean applySoulDropSettings(Player player, boolean hadCursedRing) {
        if (!CurseConfig.SIXTH_CURSE_ENABLED.get()
                || !SuperpositionHandler.canDropSoulCrystal(player, hadCursedRing)) {
            return false;
        }

        return Math.random() < CurseConfig.SIXTH_CURSE_SOUL_DROP_CHANCE.get();
    }
}
