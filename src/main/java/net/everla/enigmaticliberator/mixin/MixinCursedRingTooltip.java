package net.everla.enigmaticliberator.mixin;

import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.CursedRing;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.everla.enigmaticliberator.config.CurseConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Mixin to override CursedRing tooltip with localized text and enabled/disabled display
 */
@Mixin(value = CursedRing.class, remap = false)
public abstract class MixinCursedRingTooltip {

    /**
     * Override appendHoverText to completely control tooltip rendering with localization
     */
    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true, remap = false)
    public void overrideTooltip(ItemStack stack, @Nullable Level worldIn, List<Component> list,
                                 TooltipFlag flagIn, CallbackInfo ci) {
        // Cancel original tooltip
        ci.cancel();

        // Add void line
        ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

        if (Screen.hasShiftDown()) {
            // Count enabled and disabled curses/blessings
            int disabledCurses = 0;
            int disabledBlessings = 0;

            if (!CurseConfig.FIRST_CURSE_ENABLED.get()) disabledCurses++;
            if (!CurseConfig.SECOND_CURSE_ENABLED.get()) disabledCurses++;
            if (!CurseConfig.THIRD_CURSE_ENABLED.get()) disabledCurses++;
            if (!CurseConfig.FOURTH_CURSE_ENABLED.get()) disabledCurses++;
            if (!CurseConfig.FIFTH_CURSE_ENABLED.get()) disabledCurses++;
            if (!CurseConfig.SIXTH_CURSE_ENABLED.get()) disabledCurses++;
            if (!CurseConfig.SEVENTH_CURSE_ENABLED.get()) disabledCurses++;

            if (!BlessingConfig.LOOTING_ENABLED.get()) disabledBlessings++;
            if (!BlessingConfig.FORTUNE_ENABLED.get()) disabledBlessings++;
            if (!BlessingConfig.EXPERIENCE_ENABLED.get()) disabledBlessings++;
            if (!BlessingConfig.ENCHANTING_ENABLED.get()) disabledBlessings++;
            if (!BlessingConfig.SPECIAL_DROPS_ENABLED.get()) disabledBlessings++;
            if (!BlessingConfig.ENDER_RING_ENABLED.get()) disabledBlessings++;
            if (!BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
                disabledBlessings++;
            }

            // Show curse header - always show all 7 curses
            if (disabledCurses > 0) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.header.curses.disabled",
                    ChatFormatting.GOLD, disabledCurses);
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.header.curses");
            }

            // First Curse - Pain Amplification
            if (CurseConfig.FIRST_CURSE_ENABLED.get()) {
                double multiplier = CurseConfig.FIRST_CURSE_DAMAGE_MULTIPLIER.get();
                if (multiplier == 2.0) {
                    ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse1.default");
                } else {
                    ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse1",
                        ChatFormatting.GOLD, (int)(multiplier * 100) + "%");
                }
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse1.disabled");
            }

            // Second Curse - Neutral Hostility
            if (CurseConfig.SECOND_CURSE_ENABLED.get()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse2");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse2.disabled");
            }

            // Third Curse - Armor Weakness
            if (CurseConfig.THIRD_CURSE_ENABLED.get()) {
                double reduction = CurseConfig.THIRD_CURSE_ARMOR_REDUCTION.get();
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse3",
                    ChatFormatting.GOLD, (int)(reduction * 100) + "%");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse3.disabled");
            }

            // Fourth Curse - Weakened Strikes
            if (CurseConfig.FOURTH_CURSE_ENABLED.get()) {
                double damageReduction = CurseConfig.FOURTH_CURSE_DAMAGE_REDUCTION.get();
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse4",
                    ChatFormatting.GOLD, (int)((1.0 - damageReduction) * 100) + "%");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse4.disabled");
            }

            // Fifth Curse - Eternal Flames
            if (CurseConfig.FIFTH_CURSE_ENABLED.get()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse5");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse5.disabled");
            }

            // Sixth Curse - Soul Tear
            if (CurseConfig.SIXTH_CURSE_ENABLED.get()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse6");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse6.disabled");
            }

            // Seventh Curse - Eternal Insomnia
            if (CurseConfig.SEVENTH_CURSE_ENABLED.get()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse7");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.curse7.disabled");
            }

            // Add spacing
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

            // Show blessing header - always show all 7 blessings
            if (disabledBlessings > 0) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.header.blessings.disabled",
                    ChatFormatting.GOLD, disabledBlessings);
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.header.blessings");
            }

            // Blessing 1 - Looting
            if (BlessingConfig.LOOTING_ENABLED.get()) {
                int looting = BlessingConfig.LOOTING_BONUS_LEVELS.get();
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing1",
                    ChatFormatting.GOLD, looting);
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing1.disabled");
            }

            // Blessing 2 - Fortune
            if (BlessingConfig.FORTUNE_ENABLED.get()) {
                int fortune = BlessingConfig.FORTUNE_BONUS_LEVELS.get();
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing2",
                    ChatFormatting.GOLD, fortune);
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing2.disabled");
            }

            // Blessing 3 - Experience
            if (BlessingConfig.EXPERIENCE_ENABLED.get()) {
                double expMultiplier = BlessingConfig.EXPERIENCE_MULTIPLIER.get();
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing3",
                    ChatFormatting.GOLD, (int)(expMultiplier * 100) + "%");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing3.disabled");
            }

            // Blessing 4 - Enchanting Power
            if (BlessingConfig.ENCHANTING_ENABLED.get()) {
                int power = BlessingConfig.ENCHANTING_BONUS_POWER.get();
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing4",
                    ChatFormatting.GOLD, power);
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing4.disabled");
            }

            // Blessing 5 - Special Drops
            if (BlessingConfig.SPECIAL_DROPS_ENABLED.get()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing5");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing5.disabled");
            }

            // Blessing 6 - Ender Ring
            if (BlessingConfig.ENDER_RING_ENABLED.get()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing6");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing6.disabled");
            }

             // Blessing 7 - Unique Relics
             if (BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
                 ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing7");
             } else {
                 ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.blessing7.disabled");
            }

        } else {
            // Show lore when not holding shift - only if at least one curse/blessing is enabled
            boolean anyEnabled = CurseConfig.FIRST_CURSE_ENABLED.get() ||
                                CurseConfig.SECOND_CURSE_ENABLED.get() ||
                                CurseConfig.THIRD_CURSE_ENABLED.get() ||
                                CurseConfig.FOURTH_CURSE_ENABLED.get() ||
                                CurseConfig.FIFTH_CURSE_ENABLED.get() ||
                                CurseConfig.SIXTH_CURSE_ENABLED.get() ||
                                CurseConfig.SEVENTH_CURSE_ENABLED.get() ||
                                BlessingConfig.LOOTING_ENABLED.get() ||
                                BlessingConfig.FORTUNE_ENABLED.get() ||
                                BlessingConfig.EXPERIENCE_ENABLED.get() ||
                                BlessingConfig.ENCHANTING_ENABLED.get() ||
                                BlessingConfig.SPECIAL_DROPS_ENABLED.get() ||
                                BlessingConfig.ENDER_RING_ENABLED.get() ||
                                BlessingConfig.UNIQUE_RELICS_ENABLED.get();

            if (anyEnabled) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.lore1");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.lore2");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.lore3");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.lore4");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.lore5");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.lore6");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.lore7");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.disabled.lore1");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.disabled.lore2");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
            }

            // Show binding status based on config
            if (CurseConfig.DISABLE_ETERNAL_BINDING.get()) {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.binding.removable");
            } else {
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.binding.eternal1");
                ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.binding.eternal2");
                // Check if player is in creative mode
                if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative()) {
                    ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.binding.eternal2_creative");
                }
            }
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
            ItemLoreHelper.addLocalizedString(list, "tooltip.enigmatic_liberator.cursed_ring.hold_shift");
        }
    }
}
