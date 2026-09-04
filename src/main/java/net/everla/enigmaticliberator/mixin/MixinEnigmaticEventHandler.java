package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.items.CursedRing;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.everla.enigmaticliberator.config.CurseConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin into EnigmaticEventHandler to intercept curse effects
 * Uses simple HEAD/TAIL injection with manual override logic
 */
@Mixin(value = EnigmaticEventHandler.class, remap = false, priority = 1001)
public abstract class MixinEnigmaticEventHandler {

    /**
     * CURSE 1: Pain Amplification
     * Intercept at the END of onEntityHurt after damage is set
     */
    @Inject(
        method = "onEntityHurt",
        at = @At("TAIL"),
        cancellable = false,
        remap = false
    )
    private void adjustPainMultiplier(LivingHurtEvent event, CallbackInfo ci) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!SuperpositionHandler.hasCurio(player, com.aizistral.enigmaticlegacy.registries.EnigmaticItems.CURSED_RING)) {
            return;
        }

        // Get current damage
        float currentDamage = event.getAmount();
        float originalMultiplier = CursedRing.painMultiplier.getValue().asModifier();

        if (!CurseConfig.FIRST_CURSE_ENABLED.get()) {
            // Curse disabled - undo the multiplication
            event.setAmount(currentDamage / originalMultiplier);
        } else {
            // Apply configured multiplier if different
            float configuredMultiplier = CurseConfig.FIRST_CURSE_DAMAGE_MULTIPLIER.get().floatValue();
            if (Math.abs(configuredMultiplier - originalMultiplier) > 0.001f) {
                // Undo original and apply configured
                event.setAmount((currentDamage / originalMultiplier) * configuredMultiplier);
            }
        }
    }

    /**
     * CURSE 4: Weakened Strikes
     * Intercept at the END of onEntityHurt after damage is set
     */
    @Inject(
        method = "onEntityHurt",
        at = @At("TAIL"),
        cancellable = false,
        remap = false
    )
    private void adjustMonsterDamageDebuff(LivingHurtEvent event, CallbackInfo ci) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        if (!SuperpositionHandler.isTheCursedOne(player)) {
            return;
        }

        // Check if this is damage to a monster
        if (!(event.getEntity() instanceof net.minecraft.world.entity.monster.Monster) &&
            !(event.getEntity() instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon)) {
            return;
        }

        // Get current damage
        float currentDamage = event.getAmount();
        float originalReduction = CursedRing.monsterDamageDebuff.getValue().asModifierInverted();

        if (!CurseConfig.FOURTH_CURSE_ENABLED.get()) {
            // Curse disabled - undo the reduction
            event.setAmount(currentDamage / originalReduction);
        } else {
            // Apply configured reduction if different
            float configuredReduction = CurseConfig.FOURTH_CURSE_DAMAGE_REDUCTION.get().floatValue();
            if (Math.abs(configuredReduction - originalReduction) > 0.001f) {
                // Undo original and apply configured
                event.setAmount((currentDamage / originalReduction) * configuredReduction);
            }
        }
    }

    /**
     * CURSE 5: Eternal Flames
     * Disable or modify fire tick behavior
     */
    @Inject(
        method = "onPlayerTick",
        at = @At("TAIL"),
        cancellable = false,
        remap = false
    )
    private void adjustEternalFlames(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event, CallbackInfo ci) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!CurseConfig.FIFTH_CURSE_ENABLED.get()) {
            if (!SuperpositionHandler.isTheCursedOne(player)) return;

            // Cancel the fire extension by reducing fire ticks
            if (player.getRemainingFireTicks() > 0) {
                player.setRemainingFireTicks(player.getRemainingFireTicks() - 2);
            }
        }
    }

    /**
     * KNOCKBACK CURSE: Knockback Vulnerability
     * Adjust knockback multiplier
     */
    @Inject(
        method = "onLivingKnockback",
        at = @At("TAIL"),
        cancellable = false,
        remap = false
    )
    private void adjustKnockback(LivingKnockBackEvent event, CallbackInfo ci) {
        if (!(event.getEntity() instanceof Player)) return;

        float currentStrength = event.getStrength();
        float originalMultiplier = CursedRing.knockbackDebuff.getValue().asModifier();

        if (!CurseConfig.KNOCKBACK_CURSE_ENABLED.get()) {
            // Remove knockback increase
            event.setStrength(currentStrength / originalMultiplier);
        } else {
            // Apply configured multiplier if different
            float configuredMultiplier = CurseConfig.KNOCKBACK_CURSE_MULTIPLIER.get().floatValue();
            if (Math.abs(configuredMultiplier - originalMultiplier) > 0.001f) {
                event.setStrength((currentStrength / originalMultiplier) * configuredMultiplier);
            }
        }
    }

    /**
     * BLESSING 3: Experience Amplification
     * Adjust experience drop multiplier
     */
    @Inject(
        method = "onExperienceDrop",
        at = @At("TAIL"),
        cancellable = false,
        remap = false
    )
    private void adjustExperienceBonus(LivingExperienceDropEvent event, CallbackInfo ci) {
        Player player = event.getAttackingPlayer();
        if (player == null) return;

        if (!SuperpositionHandler.isTheCursedOne(player)) return;

        int currentExp = event.getDroppedExperience();
        int originalExp = event.getOriginalExperience();
        double originalMultiplier = CursedRing.experienceBonus.getValue().asMultiplier();
        int originalBonus = (int) (originalExp * originalMultiplier);

        if (!BlessingConfig.EXPERIENCE_ENABLED.get()) {
            // Remove experience bonus
            event.setDroppedExperience(currentExp - originalBonus);
        } else {
            // Apply configured multiplier if different
            double configuredMultiplier = BlessingConfig.EXPERIENCE_MULTIPLIER.get();
            if (Math.abs(configuredMultiplier - originalMultiplier) > 0.001) {
                int configuredBonus = (int) (originalExp * configuredMultiplier);
                event.setDroppedExperience(currentExp - originalBonus + configuredBonus);
            }
        }
    }

    /**
     * BLESSING 4: Enchanting Power
     * Adjust enchanting bonus
     */
    @Inject(
        method = "onEnchantmentLevelSet",
        at = @At("TAIL"),
        cancellable = false,
        remap = false
    )
    private void adjustEnchantingBonus(net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent event, CallbackInfo ci) {
        int currentLevel = event.getEnchantLevel();
        int originalBonus = CursedRing.enchantingBonus.getValue();

        if (!BlessingConfig.ENCHANTING_ENABLED.get()) {
            // Remove enchanting bonus
            event.setEnchantLevel(currentLevel - originalBonus);
        } else {
            // Apply configured bonus if different
            int configuredBonus = BlessingConfig.ENCHANTING_BONUS_POWER.get();
            if (configuredBonus != originalBonus) {
                event.setEnchantLevel(currentLevel - originalBonus + configuredBonus);
            }
        }
    }

    /**
     * BLESSING 5: Special Drops
     * Cancel special drops when disabled in config
     */
    @Inject(
        method = "onLivingDeath",
        at = @At(value = "INVOKE",
                 target = "Lcom/aizistral/omniconfig/wrappers/Omniconfig$BooleanParameter;getValue()Ljava/lang/Boolean;",
                 ordinal = 0),
        cancellable = true,
        remap = false
    )
    private void cancelSpecialDrops(LivingDeathEvent event, CallbackInfo ci) {
        // If special drops are disabled in our config, cancel the event handler early
        if (!BlessingConfig.SPECIAL_DROPS_ENABLED.get()) {
            ci.cancel();
        }
    }
}
