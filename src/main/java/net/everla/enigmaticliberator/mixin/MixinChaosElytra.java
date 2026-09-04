package net.everla.enigmaticliberator.mixin;

import auviotre.enigmatic.addon.contents.items.ChaosElytra;
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

import java.util.UUID;

/**
 * Mixin to disable Chaos Elytra special effects when configured
 * Keeps elytra flight functionality but removes all special effects
 */
@Mixin(value = ChaosElytra.class, remap = false)
public abstract class MixinChaosElytra {

    /**
     * Remove attribute modifiers when Chaos Elytra is disabled (keeps flight but removes bonuses)
     */
    @Inject(method = "getAttributeModifiers(Ltop/theillusivec4/curios/api/SlotContext;Ljava/util/UUID;Lnet/minecraft/world/item/ItemStack;)Lcom/google/common/collect/Multimap;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void disableAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            // Return empty multimap - no attribute bonuses
            cir.setReturnValue(HashMultimap.create());
        }
    }

    /**
     * Disable tick effects when Chaos Elytra is disabled
     */
    @Inject(method = "curioTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableTick(SlotContext slotContext, ItemStack stack, CallbackInfo ci) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            ci.cancel();
        }
    }

    /**
     * Disable event handlers for Chaos Elytra special effects
     */
    @Inject(method = "onFall(Lnet/minecraftforge/event/entity/player/PlayerFlyableFallEvent;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnFallPlayer(net.minecraftforge.event.entity.player.PlayerFlyableFallEvent event, CallbackInfo ci) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "onFall(Lnet/minecraftforge/event/entity/living/LivingFallEvent;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnFallLiving(net.minecraftforge.event.entity.living.LivingFallEvent event, CallbackInfo ci) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event, CallbackInfo ci) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "onHurt", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnHurt(net.minecraftforge.event.entity.living.LivingDamageEvent event, CallbackInfo ci) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            ci.cancel();
        }
    }

    @Inject(method = "onPlayerTickClient", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableOnPlayerTickClient(net.minecraftforge.event.TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        if (!BlessingConfig.CHAOS_ELYTRA_ENABLED.get()) {
            ci.cancel();
        }
    }
}
