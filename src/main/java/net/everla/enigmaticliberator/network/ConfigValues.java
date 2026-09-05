package net.everla.enigmaticliberator.network;

import net.everla.enigmaticliberator.config.BlessingConfig;
import net.everla.enigmaticliberator.config.CurseConfig;
import net.everla.enigmaticliberator.config.ExtraConfig;

import java.util.LinkedHashMap;
import java.util.Map;

/** Shared serialization and validation table for the server-owned config values. */
public final class ConfigValues {
    private ConfigValues() {
    }

    public static Map<String, String> snapshot() {
        Map<String, String> values = new LinkedHashMap<>();
        add(values, "curse.first.enabled", CurseConfig.FIRST_CURSE_ENABLED.get());
        add(values, "curse.first.damage_multiplier", CurseConfig.FIRST_CURSE_DAMAGE_MULTIPLIER.get());
        add(values, "curse.second.enabled", CurseConfig.SECOND_CURSE_ENABLED.get());
        add(values, "curse.second.anger_range", CurseConfig.SECOND_CURSE_ANGER_RANGE.get());
        add(values, "curse.second.xray_range", CurseConfig.SECOND_CURSE_XRAY_RANGE.get());
        add(values, "curse.second.enderman_range", CurseConfig.SECOND_CURSE_ENDERMAN_RANGE.get());
        add(values, "curse.second.enderman_frequency", CurseConfig.SECOND_CURSE_ENDERMAN_FREQUENCY.get());
        add(values, "curse.second.save_bees", CurseConfig.SECOND_CURSE_SAVE_BEES.get());
        add(values, "curse.third.enabled", CurseConfig.THIRD_CURSE_ENABLED.get());
        add(values, "curse.third.armor_reduction", CurseConfig.THIRD_CURSE_ARMOR_REDUCTION.get());
        add(values, "curse.fourth.enabled", CurseConfig.FOURTH_CURSE_ENABLED.get());
        add(values, "curse.fourth.damage_reduction", CurseConfig.FOURTH_CURSE_DAMAGE_REDUCTION.get());
        add(values, "curse.fifth.enabled", CurseConfig.FIFTH_CURSE_ENABLED.get());
        add(values, "curse.fifth.fire_tick_increase", CurseConfig.FIFTH_CURSE_FIRE_TICK_INCREASE.get());
        add(values, "curse.sixth.enabled", CurseConfig.SIXTH_CURSE_ENABLED.get());
        add(values, "curse.sixth.soul_drop_chance", CurseConfig.SIXTH_CURSE_SOUL_DROP_CHANCE.get());
        add(values, "curse.sixth.max_health_loss", CurseConfig.SIXTH_CURSE_MAX_HEALTH_LOSS.get());
        add(values, "curse.seventh.enabled", CurseConfig.SEVENTH_CURSE_ENABLED.get());
        add(values, "curse.seventh.prevent_sleep", CurseConfig.SEVENTH_CURSE_PREVENT_SLEEP.get());
        add(values, "curse.knockback.enabled", CurseConfig.KNOCKBACK_CURSE_ENABLED.get());
        add(values, "curse.knockback.multiplier", CurseConfig.KNOCKBACK_CURSE_MULTIPLIER.get());
        add(values, "curse.binding.disable", CurseConfig.DISABLE_ETERNAL_BINDING.get());

        add(values, "blessing.looting.enabled", BlessingConfig.LOOTING_ENABLED.get());
        add(values, "blessing.looting.levels", BlessingConfig.LOOTING_BONUS_LEVELS.get());
        add(values, "blessing.fortune.enabled", BlessingConfig.FORTUNE_ENABLED.get());
        add(values, "blessing.fortune.levels", BlessingConfig.FORTUNE_BONUS_LEVELS.get());
        add(values, "blessing.experience.enabled", BlessingConfig.EXPERIENCE_ENABLED.get());
        add(values, "blessing.experience.multiplier", BlessingConfig.EXPERIENCE_MULTIPLIER.get());
        add(values, "blessing.enchanting.enabled", BlessingConfig.ENCHANTING_ENABLED.get());
        add(values, "blessing.enchanting.power", BlessingConfig.ENCHANTING_BONUS_POWER.get());
        add(values, "blessing.special_drops.enabled", BlessingConfig.SPECIAL_DROPS_ENABLED.get());
        add(values, "blessing.special_drops.multiplier", BlessingConfig.SPECIAL_DROPS_MULTIPLIER.get());
        add(values, "blessing.ender_ring.enabled", BlessingConfig.ENDER_RING_ENABLED.get());
        add(values, "blessing.unique_relics.enabled", BlessingConfig.UNIQUE_RELICS_ENABLED.get());

        add(values, "extra.ultra_hardcore", ExtraConfig.ULTRA_HARDCORE.get());
        add(values, "extra.auto_equip", ExtraConfig.AUTO_EQUIP.get());
        add(values, "extra.cursed_ring.extra_slot", ExtraConfig.CURSED_RING_EXTRA_SLOT.get());
        add(values, "extra.lore.enabled", ExtraConfig.ENABLE_LORE.get());
        add(values, "extra.tooltip.conceal", ExtraConfig.CONCEAL_ABILITIES.get());
        add(values, "extra.super_cursed.time", ExtraConfig.SUPER_CURSED_TIME.get());
        add(values, "extra.enigmatic_amulet.replace_gravity", ExtraConfig.AMULET_REPLACE_GRAVITY.get());
        add(values, "extra.enigmatic_amulet.reroll_on_sneak_drop", ExtraConfig.AMULET_REROLL_ON_SNEAK_DROP.get());
        return values;
    }

    public static String keyFor(Object value) {
        for (Map.Entry<String, String> entry : snapshot().entrySet()) {
            if (value == valueForKey(entry.getKey())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static Object valueForKey(String key) {
        return switch (key) {
            case "curse.first.enabled" -> CurseConfig.FIRST_CURSE_ENABLED;
            case "curse.first.damage_multiplier" -> CurseConfig.FIRST_CURSE_DAMAGE_MULTIPLIER;
            case "curse.second.enabled" -> CurseConfig.SECOND_CURSE_ENABLED;
            case "curse.second.anger_range" -> CurseConfig.SECOND_CURSE_ANGER_RANGE;
            case "curse.second.xray_range" -> CurseConfig.SECOND_CURSE_XRAY_RANGE;
            case "curse.second.enderman_range" -> CurseConfig.SECOND_CURSE_ENDERMAN_RANGE;
            case "curse.second.enderman_frequency" -> CurseConfig.SECOND_CURSE_ENDERMAN_FREQUENCY;
            case "curse.second.save_bees" -> CurseConfig.SECOND_CURSE_SAVE_BEES;
            case "curse.third.enabled" -> CurseConfig.THIRD_CURSE_ENABLED;
            case "curse.third.armor_reduction" -> CurseConfig.THIRD_CURSE_ARMOR_REDUCTION;
            case "curse.fourth.enabled" -> CurseConfig.FOURTH_CURSE_ENABLED;
            case "curse.fourth.damage_reduction" -> CurseConfig.FOURTH_CURSE_DAMAGE_REDUCTION;
            case "curse.fifth.enabled" -> CurseConfig.FIFTH_CURSE_ENABLED;
            case "curse.fifth.fire_tick_increase" -> CurseConfig.FIFTH_CURSE_FIRE_TICK_INCREASE;
            case "curse.sixth.enabled" -> CurseConfig.SIXTH_CURSE_ENABLED;
            case "curse.sixth.soul_drop_chance" -> CurseConfig.SIXTH_CURSE_SOUL_DROP_CHANCE;
            case "curse.sixth.max_health_loss" -> CurseConfig.SIXTH_CURSE_MAX_HEALTH_LOSS;
            case "curse.seventh.enabled" -> CurseConfig.SEVENTH_CURSE_ENABLED;
            case "curse.seventh.prevent_sleep" -> CurseConfig.SEVENTH_CURSE_PREVENT_SLEEP;
            case "curse.knockback.enabled" -> CurseConfig.KNOCKBACK_CURSE_ENABLED;
            case "curse.knockback.multiplier" -> CurseConfig.KNOCKBACK_CURSE_MULTIPLIER;
            case "curse.binding.disable" -> CurseConfig.DISABLE_ETERNAL_BINDING;
            case "blessing.looting.enabled" -> BlessingConfig.LOOTING_ENABLED;
            case "blessing.looting.levels" -> BlessingConfig.LOOTING_BONUS_LEVELS;
            case "blessing.fortune.enabled" -> BlessingConfig.FORTUNE_ENABLED;
            case "blessing.fortune.levels" -> BlessingConfig.FORTUNE_BONUS_LEVELS;
            case "blessing.experience.enabled" -> BlessingConfig.EXPERIENCE_ENABLED;
            case "blessing.experience.multiplier" -> BlessingConfig.EXPERIENCE_MULTIPLIER;
            case "blessing.enchanting.enabled" -> BlessingConfig.ENCHANTING_ENABLED;
            case "blessing.enchanting.power" -> BlessingConfig.ENCHANTING_BONUS_POWER;
            case "blessing.special_drops.enabled" -> BlessingConfig.SPECIAL_DROPS_ENABLED;
            case "blessing.special_drops.multiplier" -> BlessingConfig.SPECIAL_DROPS_MULTIPLIER;
            case "blessing.ender_ring.enabled" -> BlessingConfig.ENDER_RING_ENABLED;
            case "blessing.unique_relics.enabled" -> BlessingConfig.UNIQUE_RELICS_ENABLED;
            case "extra.ultra_hardcore" -> ExtraConfig.ULTRA_HARDCORE;
            case "extra.auto_equip" -> ExtraConfig.AUTO_EQUIP;
            case "extra.cursed_ring.extra_slot" -> ExtraConfig.CURSED_RING_EXTRA_SLOT;
            case "extra.lore.enabled" -> ExtraConfig.ENABLE_LORE;
            case "extra.tooltip.conceal" -> ExtraConfig.CONCEAL_ABILITIES;
            case "extra.super_cursed.time" -> ExtraConfig.SUPER_CURSED_TIME;
            case "extra.enigmatic_amulet.replace_gravity" -> ExtraConfig.AMULET_REPLACE_GRAVITY;
            case "extra.enigmatic_amulet.reroll_on_sneak_drop" -> ExtraConfig.AMULET_REROLL_ON_SNEAK_DROP;
            default -> null;
        };
    }

    public static boolean apply(String key, String value) {
        try {
            switch (key) {
                case "curse.first.enabled" -> CurseConfig.FIRST_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.first.damage_multiplier" -> CurseConfig.FIRST_CURSE_DAMAGE_MULTIPLIER.set(Double.parseDouble(value));
                case "curse.second.enabled" -> CurseConfig.SECOND_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.second.anger_range" -> CurseConfig.SECOND_CURSE_ANGER_RANGE.set(Double.parseDouble(value));
                case "curse.second.xray_range" -> CurseConfig.SECOND_CURSE_XRAY_RANGE.set(Double.parseDouble(value));
                case "curse.second.enderman_range" -> CurseConfig.SECOND_CURSE_ENDERMAN_RANGE.set(Double.parseDouble(value));
                case "curse.second.enderman_frequency" -> CurseConfig.SECOND_CURSE_ENDERMAN_FREQUENCY.set(Double.parseDouble(value));
                case "curse.second.save_bees" -> CurseConfig.SECOND_CURSE_SAVE_BEES.set(Boolean.parseBoolean(value));
                case "curse.third.enabled" -> CurseConfig.THIRD_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.third.armor_reduction" -> CurseConfig.THIRD_CURSE_ARMOR_REDUCTION.set(Double.parseDouble(value));
                case "curse.fourth.enabled" -> CurseConfig.FOURTH_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.fourth.damage_reduction" -> CurseConfig.FOURTH_CURSE_DAMAGE_REDUCTION.set(Double.parseDouble(value));
                case "curse.fifth.enabled" -> CurseConfig.FIFTH_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.fifth.fire_tick_increase" -> CurseConfig.FIFTH_CURSE_FIRE_TICK_INCREASE.set(Integer.parseInt(value));
                case "curse.sixth.enabled" -> CurseConfig.SIXTH_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.sixth.soul_drop_chance" -> CurseConfig.SIXTH_CURSE_SOUL_DROP_CHANCE.set(Double.parseDouble(value));
                case "curse.sixth.max_health_loss" -> CurseConfig.SIXTH_CURSE_MAX_HEALTH_LOSS.set(Double.parseDouble(value));
                case "curse.seventh.enabled" -> CurseConfig.SEVENTH_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.seventh.prevent_sleep" -> CurseConfig.SEVENTH_CURSE_PREVENT_SLEEP.set(Boolean.parseBoolean(value));
                case "curse.knockback.enabled" -> CurseConfig.KNOCKBACK_CURSE_ENABLED.set(Boolean.parseBoolean(value));
                case "curse.knockback.multiplier" -> CurseConfig.KNOCKBACK_CURSE_MULTIPLIER.set(Double.parseDouble(value));
                case "curse.binding.disable" -> CurseConfig.DISABLE_ETERNAL_BINDING.set(Boolean.parseBoolean(value));
                case "blessing.looting.enabled" -> BlessingConfig.LOOTING_ENABLED.set(Boolean.parseBoolean(value));
                case "blessing.looting.levels" -> BlessingConfig.LOOTING_BONUS_LEVELS.set(Integer.parseInt(value));
                case "blessing.fortune.enabled" -> BlessingConfig.FORTUNE_ENABLED.set(Boolean.parseBoolean(value));
                case "blessing.fortune.levels" -> BlessingConfig.FORTUNE_BONUS_LEVELS.set(Integer.parseInt(value));
                case "blessing.experience.enabled" -> BlessingConfig.EXPERIENCE_ENABLED.set(Boolean.parseBoolean(value));
                case "blessing.experience.multiplier" -> BlessingConfig.EXPERIENCE_MULTIPLIER.set(Double.parseDouble(value));
                case "blessing.enchanting.enabled" -> BlessingConfig.ENCHANTING_ENABLED.set(Boolean.parseBoolean(value));
                case "blessing.enchanting.power" -> BlessingConfig.ENCHANTING_BONUS_POWER.set(Integer.parseInt(value));
                case "blessing.special_drops.enabled" -> BlessingConfig.SPECIAL_DROPS_ENABLED.set(Boolean.parseBoolean(value));
                case "blessing.special_drops.multiplier" -> BlessingConfig.SPECIAL_DROPS_MULTIPLIER.set(Double.parseDouble(value));
                case "blessing.ender_ring.enabled" -> BlessingConfig.ENDER_RING_ENABLED.set(Boolean.parseBoolean(value));
                case "blessing.unique_relics.enabled" -> BlessingConfig.UNIQUE_RELICS_ENABLED.set(Boolean.parseBoolean(value));
                case "extra.ultra_hardcore" -> ExtraConfig.ULTRA_HARDCORE.set(Boolean.parseBoolean(value));
                case "extra.auto_equip" -> ExtraConfig.AUTO_EQUIP.set(Boolean.parseBoolean(value));
                case "extra.cursed_ring.extra_slot" -> ExtraConfig.CURSED_RING_EXTRA_SLOT.set(Boolean.parseBoolean(value));
                case "extra.lore.enabled" -> ExtraConfig.ENABLE_LORE.set(Boolean.parseBoolean(value));
                case "extra.tooltip.conceal" -> ExtraConfig.CONCEAL_ABILITIES.set(Boolean.parseBoolean(value));
                case "extra.super_cursed.time" -> ExtraConfig.SUPER_CURSED_TIME.set(Double.parseDouble(value));
                case "extra.enigmatic_amulet.replace_gravity" -> ExtraConfig.AMULET_REPLACE_GRAVITY.set(Boolean.parseBoolean(value));
                case "extra.enigmatic_amulet.reroll_on_sneak_drop" -> ExtraConfig.AMULET_REROLL_ON_SNEAK_DROP.set(Boolean.parseBoolean(value));
                default -> { return false; }
            }
            return true;
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    private static void add(Map<String, String> values, String key, Object value) {
        values.put(key, String.valueOf(value));
    }
}
