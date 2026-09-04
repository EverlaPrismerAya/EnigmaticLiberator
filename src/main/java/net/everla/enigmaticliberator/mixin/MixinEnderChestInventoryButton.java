package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.gui.EnderChestInventoryButton;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Hides the inventory button supplied by the Cursed Ring when Ender Ring is disabled. */
@Mixin(value = EnderChestInventoryButton.class, remap = false)
public abstract class MixinEnderChestInventoryButton {

    @Inject(method = "beforeRender", at = @At("HEAD"), cancellable = true, remap = false)
    private void hideDisabledEnderRingButton(GuiGraphics graphics, int mouseX, int mouseY, float partialTick,
                                             CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.ENDER_RING_ENABLED.get()) {
            net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
            if (minecraft.player != null
                    && SuperpositionHandler.hasCurio(minecraft.player, EnigmaticItems.CURSED_RING)
                    && !SuperpositionHandler.hasCurio(minecraft.player, EnigmaticItems.ENDER_RING)) {
                cir.setReturnValue(false);
            }
        }
    }
}
