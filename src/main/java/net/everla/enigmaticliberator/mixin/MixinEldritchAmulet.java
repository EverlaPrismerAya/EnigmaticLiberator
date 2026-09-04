package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.EldritchAmulet;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

/**
 * Mixin to disable Eldritch Amulet when configured
 */
@Mixin(value = EldritchAmulet.class, remap = false)
public abstract class MixinEldritchAmulet {

    /**
     * Prevent equipping when Eldritch Amulet is disabled
     */
    @Inject(method = "canEquip", at = @At("HEAD"), cancellable = true, remap = false)
    private void preventEquip(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.ELDRITCH_AMULET_ENABLED.get()) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Remove attribute modifiers when Eldritch Amulet is disabled
     */
    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableAttributes(SlotContext slotContext, UUID uuid, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        if (!BlessingConfig.ELDRITCH_AMULET_ENABLED.get()) {
            // Return empty multimap - no attributes
            cir.setReturnValue(HashMultimap.create());
        }
    }

    /**
     * Disable tick effects when Eldritch Amulet is disabled
     */
    @Inject(method = "curioTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableTick(SlotContext slotContext, ItemStack stack, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (!BlessingConfig.ELDRITCH_AMULET_ENABLED.get()) {
            ci.cancel();
        }
    }
}
