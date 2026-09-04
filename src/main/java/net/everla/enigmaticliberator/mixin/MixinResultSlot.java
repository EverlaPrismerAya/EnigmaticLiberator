package net.everla.enigmaticliberator.mixin;

import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/** Prevents the result slot from giving out disabled unique relics. */
@Mixin(ResultSlot.class)
public abstract class MixinResultSlot {
    private static final Set<ResourceLocation> UNIQUE_RELICS = Set.of(
            new ResourceLocation("enigmaticlegacy", "abyssal_heart"),
            new ResourceLocation("enigmaticlegacy", "eldritch_pan"),
            new ResourceLocation("enigmaticlegacy", "eldritch_amulet"),
            new ResourceLocation("enigmaticlegacy", "the_infinitum"),
            new ResourceLocation("enigmaticlegacy", "desolation_ring"),
            new ResourceLocation("enigmaticaddons", "violence_scroll"),
            new ResourceLocation("enigmaticaddons", "chaos_elytra")
    );

    @Inject(method = "m_6201_", at = @At("HEAD"), cancellable = true, remap = false)
    private void blockDisabledRelicCrafting(int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (!BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
            ItemStack result = ((ResultSlot) (Object) this).getItem();
            if (!result.isEmpty()
                    && UNIQUE_RELICS.contains(result.getItem().builtInRegistryHolder().key().location())) {
                cir.setReturnValue(ItemStack.EMPTY);
            }
        }
    }
}
