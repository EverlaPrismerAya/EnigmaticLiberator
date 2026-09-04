package net.everla.enigmaticliberator.mixin;

import auviotre.enigmatic.addon.contents.items.ViolenceScroll;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

/**
 * Mixin to disable Violence Scroll when configured
 */
@Mixin(value = ViolenceScroll.class, remap = false)
public abstract class MixinViolenceScroll {

    /**
     * Prevent equipping when Violence Scroll is disabled
     */
    @Inject(method = "canEquip", at = @At("HEAD"), cancellable = true, remap = false)
    private void preventEquip(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.VIOLENCE_SCROLL_ENABLED.get()) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Disable tick effects when Violence Scroll is disabled
     */
    @Inject(method = "curioTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableTick(SlotContext slotContext, ItemStack stack, CallbackInfo ci) {
        if (!BlessingConfig.VIOLENCE_SCROLL_ENABLED.get()) {
            ci.cancel();
        }
    }

    /**
     * Disable event handlers for Violence Scroll
     */
    @Inject(method = "onHurt", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnHurt(net.minecraftforge.event.entity.living.LivingHurtEvent event, CallbackInfo ci) {
        if (!BlessingConfig.VIOLENCE_SCROLL_ENABLED.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "onAttackLast", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnAttackLast(net.minecraftforge.event.entity.living.LivingDamageEvent event, CallbackInfo ci) {
        if (!BlessingConfig.VIOLENCE_SCROLL_ENABLED.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event, CallbackInfo ci) {
        if (!BlessingConfig.VIOLENCE_SCROLL_ENABLED.get()) {
            ci.cancel();
        }
    }
}
