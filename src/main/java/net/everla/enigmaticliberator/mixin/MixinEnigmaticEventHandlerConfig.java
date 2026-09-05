package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.everla.enigmaticliberator.config.CurseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Redirects Enigmatic Legacy's starter gear settings to this mod's config. */
@Mixin(value = EnigmaticEventHandler.class, remap = false)
public abstract class MixinEnigmaticEventHandlerConfig {

    @Redirect(
            method = "grantStarterGear",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/aizistral/omniconfig/wrappers/Omniconfig$BooleanParameter;getValue()Z",
                    ordinal = 1
            ),
            remap = false
    )
    private static boolean useConfiguredUltraHardcore(Omniconfig.BooleanParameter ignored) {
        return ExtraConfig.ULTRA_HARDCORE.get();
    }

    @Redirect(
            method = "onPlayerTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/aizistral/omniconfig/wrappers/Omniconfig$BooleanParameter;getValue()Z",
                    ordinal = 0
            ),
            remap = false
    )
    private boolean useConfiguredAutoEquip(Omniconfig.BooleanParameter ignored) {
        return ExtraConfig.AUTO_EQUIP.get();
    }
}
