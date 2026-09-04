package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.EldritchPan;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to disable Eldritch Pan when configured
 */
@Mixin(value = EldritchPan.class, remap = false)
public abstract class MixinEldritchPan {

    /**
     * Disable attack damage when Eldritch Pan is disabled
     */
    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableAttack(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (!BlessingConfig.ELDRITCH_PAN_ENABLED.get()) {
            // Prevent the weapon from dealing damage
            cir.setReturnValue(false);
        }
    }

    /**
     * Disable right-click usage when Eldritch Pan is disabled
     */
    @Inject(method = "use", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!BlessingConfig.ELDRITCH_PAN_ENABLED.get()) {
            // Prevent the item from being used
            cir.setReturnValue(InteractionResultHolder.fail(player.getItemInHand(hand)));
        }
    }

    /**
     * Remove attribute modifiers when Eldritch Pan is disabled
     */
    @Inject(method = "getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)Lcom/google/common/collect/Multimap;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void disableAttributes(EquipmentSlot slot, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        if (!BlessingConfig.ELDRITCH_PAN_ENABLED.get()) {
            // Return empty multimap - no attributes
            cir.setReturnValue(HashMultimap.create());
        }
    }
}
