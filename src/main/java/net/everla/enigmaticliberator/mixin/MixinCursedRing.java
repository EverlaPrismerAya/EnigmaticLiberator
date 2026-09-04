package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.items.CursedRing;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.everla.enigmaticliberator.config.CurseConfig;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

/**
 * Mixin into CursedRing to override curse and blessing behaviors
 * Uses Mixin injection to modify original implementation instead of event handlers
 */
@Mixin(value = CursedRing.class, remap = false)
public abstract class MixinCursedRing {

    /**
     * BLESSING 1: Looting Level
     * Inject at RETURN to modify the final looting level
     */
    @Inject(method = "getLootingLevel", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target,
                                   int baseLooting, ItemStack curio, CallbackInfoReturnable<Integer> cir) {
        if (!BlessingConfig.LOOTING_ENABLED.get()) {
            // Remove looting bonus completely
            int bonusLevels = CursedRing.lootingBonus.getValue();
            cir.setReturnValue(cir.getReturnValue() - bonusLevels);
        } else {
            // Apply configured bonus if different
            int configuredBonus = BlessingConfig.LOOTING_BONUS_LEVELS.get();
            int originalBonus = CursedRing.lootingBonus.getValue();
            if (configuredBonus != originalBonus) {
                cir.setReturnValue(cir.getReturnValue() - originalBonus + configuredBonus);
            }
        }
    }

    /**
     * BLESSING 2: Fortune Level
     * Inject at RETURN to modify the final fortune level
     */
    @Inject(method = "getFortuneLevel", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack curio,
                                   CallbackInfoReturnable<Integer> cir) {
        if (!BlessingConfig.FORTUNE_ENABLED.get()) {
            // Remove fortune bonus completely
            int bonusLevels = CursedRing.fortuneBonus.getValue();
            cir.setReturnValue(cir.getReturnValue() - bonusLevels);
        } else {
            // Apply configured bonus if different
            int configuredBonus = BlessingConfig.FORTUNE_BONUS_LEVELS.get();
            int originalBonus = CursedRing.fortuneBonus.getValue();
            if (configuredBonus != originalBonus) {
                cir.setReturnValue(cir.getReturnValue() - originalBonus + configuredBonus);
            }
        }
    }

    /**
     * CURSE 3: Armor Weakness
     * Inject at RETURN to modify attribute modifiers
     */
    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true, remap = false)
    private void modifyAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack,
                                         CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create(cir.getReturnValue());

        if (!CurseConfig.THIRD_CURSE_ENABLED.get()) {
            // Remove armor debuff completely
            attributes.removeAll(Attributes.ARMOR);
            attributes.removeAll(Attributes.ARMOR_TOUGHNESS);
        } else {
            // Check if we need to modify the armor debuff value
            double configuredDebuff = CurseConfig.THIRD_CURSE_ARMOR_REDUCTION.get();
            double originalDebuff = CursedRing.armorDebuff.getValue().asModifier();

            if (Math.abs(configuredDebuff - originalDebuff) > 0.001) {
                // Remove original modifiers
                attributes.removeAll(Attributes.ARMOR);
                attributes.removeAll(Attributes.ARMOR_TOUGHNESS);

                // Add new modifiers with configured values
                attributes.put(Attributes.ARMOR, new AttributeModifier(
                    UUID.fromString("457d0ac3-69e4-482f-b636-22e0802da6bd"),
                    "enigmaticlegacy:armor_modifier",
                    -configuredDebuff,
                    AttributeModifier.Operation.MULTIPLY_TOTAL));

                attributes.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                    UUID.fromString("95e70d83-3d50-4241-a835-996e1ef039bb"),
                    "enigmaticlegacy:armor_toughness_modifier",
                    -configuredDebuff,
                    AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
        }

        cir.setReturnValue(attributes);
    }

    /**
     * CURSE 2: Neutral Hostility
     * Cancel curioTick entirely if disabled to prevent neutral mob aggression
     */
    @Inject(method = "curioTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void disableNeutralHostility(SlotContext context, ItemStack stack, CallbackInfo ci) {
        if (!CurseConfig.SECOND_CURSE_ENABLED.get()) {
            // Cancel the entire tick to prevent neutral mob aggression
            ci.cancel();
        }
        // If enabled, we can still modify ranges through config values
        // The original method will use CursedRing.neutralAngerRange etc.
    }

    /**
     * RING BINDING: Allow unequipping in survival
     * Override canUnequip to allow removal when eternal binding is disabled
     */
    @Inject(method = "canUnequip", at = @At("HEAD"), cancellable = true, remap = false)
    private void allowUnequipIfConfigured(SlotContext context, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (CurseConfig.DISABLE_ETERNAL_BINDING.get()) {
            // Allow unequipping when eternal binding is disabled
            cir.setReturnValue(true);
        }
    }

    /**
     * CURSE 2: Modify neutral anger range if curse is enabled
     */
    @ModifyVariable(
        method = "curioTick",
        at = @At(value = "STORE", ordinal = 0),
        ordinal = 0,
        name = "genericMobs",
        remap = false
    )
    private double modifyNeutralAngerRange(double original) {
        if (CurseConfig.SECOND_CURSE_ENABLED.get()) {
            return CurseConfig.SECOND_CURSE_ANGER_RANGE.get();
        }
        return original;
    }
}
