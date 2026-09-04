package net.everla.enigmaticliberator.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CurseConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // First Curse - Pain Amplification
    public static final ForgeConfigSpec.BooleanValue FIRST_CURSE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue FIRST_CURSE_DAMAGE_MULTIPLIER;

    // Second Curse - Neutral Hostility
    public static final ForgeConfigSpec.BooleanValue SECOND_CURSE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue SECOND_CURSE_ANGER_RANGE;
    public static final ForgeConfigSpec.DoubleValue SECOND_CURSE_XRAY_RANGE;
    public static final ForgeConfigSpec.DoubleValue SECOND_CURSE_ENDERMAN_RANGE;
    public static final ForgeConfigSpec.DoubleValue SECOND_CURSE_ENDERMAN_FREQUENCY;
    public static final ForgeConfigSpec.BooleanValue SECOND_CURSE_SAVE_BEES;

    // Third Curse - Armor Weakness
    public static final ForgeConfigSpec.BooleanValue THIRD_CURSE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue THIRD_CURSE_ARMOR_REDUCTION;

    // Fourth Curse - Weakened Strikes
    public static final ForgeConfigSpec.BooleanValue FOURTH_CURSE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue FOURTH_CURSE_DAMAGE_REDUCTION;

    // Fifth Curse - Eternal Flames (NEW CONFIGURATION)
    public static final ForgeConfigSpec.BooleanValue FIFTH_CURSE_ENABLED;
    public static final ForgeConfigSpec.IntValue FIFTH_CURSE_FIRE_TICK_INCREASE;

    // Sixth Curse - Soul Tear (NEW CONFIGURATION)
    public static final ForgeConfigSpec.BooleanValue SIXTH_CURSE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue SIXTH_CURSE_SOUL_DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue SIXTH_CURSE_MAX_HEALTH_LOSS;

    // Seventh Curse - Eternal Insomnia
    public static final ForgeConfigSpec.BooleanValue SEVENTH_CURSE_ENABLED;
    public static final ForgeConfigSpec.BooleanValue SEVENTH_CURSE_PREVENT_SLEEP;

    // Extra - Knockback Vulnerability
    public static final ForgeConfigSpec.BooleanValue KNOCKBACK_CURSE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue KNOCKBACK_CURSE_MULTIPLIER;

    // Ring Binding
    public static final ForgeConfigSpec.BooleanValue DISABLE_ETERNAL_BINDING;

    static {
        BUILDER.push("Curses Configuration");
        BUILDER.comment("Configure the Seven Curses of the Cursed Ring");
        BUILDER.comment("Each curse can be individually enabled/disabled and its intensity adjusted");

        // First Curse
        BUILDER.push("first_curse_pain_amplification");
        FIRST_CURSE_ENABLED = BUILDER
            .comment("Enable or disable the First Curse (Pain Amplification)")
            .comment("When enabled, player receives increased damage from all sources")
            .define("enabled", true);
        FIRST_CURSE_DAMAGE_MULTIPLIER = BUILDER
            .comment("Damage multiplier for the First Curse")
            .comment("2.0 = 200% damage (double damage), 1.0 = normal damage, 3.0 = triple damage")
            .defineInRange("damage_multiplier", 2.0, 0.1, 10.0);
        BUILDER.pop();

        // Second Curse
        BUILDER.push("second_curse_neutral_hostility");
        SECOND_CURSE_ENABLED = BUILDER
            .comment("Enable or disable the Second Curse (Neutral Hostility)")
            .comment("When enabled, neutral creatures become aggressive towards the player")
            .define("enabled", true);
        SECOND_CURSE_ANGER_RANGE = BUILDER
            .comment("Range in blocks where neutral mobs become hostile")
            .comment("Set to 0 to disable aggression range")
            .defineInRange("anger_range", 24.0, 0.0, 128.0);
        SECOND_CURSE_XRAY_RANGE = BUILDER
            .comment("Range where mobs can detect player through walls")
            .comment("Set to 0 to require line of sight")
            .defineInRange("xray_range", 4.0, 0.0, 32.0);
        SECOND_CURSE_ENDERMAN_RANGE = BUILDER
            .comment("Range where Endermen can randomly teleport to player")
            .comment("Set to 0 to disable Enderman teleportation")
            .defineInRange("enderman_teleport_range", 32.0, 0.0, 128.0);
        SECOND_CURSE_ENDERMAN_FREQUENCY = BUILDER
            .comment("Frequency multiplier for Enderman teleportation")
            .comment("1.0 = normal frequency, 0.0 = never, 2.0 = twice as frequent")
            .defineInRange("enderman_teleport_frequency", 1.0, 0.0, 10.0);
        SECOND_CURSE_SAVE_BEES = BUILDER
            .comment("If true, bees will not be affected by the Second Curse")
            .comment("This option exists for players who want to protect their bees")
            .define("save_the_bees", false);
        BUILDER.pop();

        // Third Curse
        BUILDER.push("third_curse_armor_weakness");
        THIRD_CURSE_ENABLED = BUILDER
            .comment("Enable or disable the Third Curse (Armor Weakness)")
            .comment("When enabled, armor is less effective")
            .define("enabled", true);
        THIRD_CURSE_ARMOR_REDUCTION = BUILDER
            .comment("Armor effectiveness reduction percentage")
            .comment("0.3 = 30% less effective, 0.0 = no reduction, 1.0 = completely ineffective")
            .defineInRange("armor_reduction", 0.3, 0.0, 1.0);
        BUILDER.pop();

        // Fourth Curse
        BUILDER.push("fourth_curse_weakened_strikes");
        FOURTH_CURSE_ENABLED = BUILDER
            .comment("Enable or disable the Fourth Curse (Weakened Strikes)")
            .comment("When enabled, damage dealt to monsters is reduced")
            .define("enabled", true);
        FOURTH_CURSE_DAMAGE_REDUCTION = BUILDER
            .comment("Damage reduction percentage against monsters")
            .comment("0.5 = 50% damage dealt, 0.0 = no damage, 1.0 = full damage (no reduction)")
            .defineInRange("damage_reduction", 0.5, 0.0, 1.0);
        BUILDER.pop();

        // Fifth Curse - NEW CONFIGURATION
        BUILDER.push("fifth_curse_eternal_flames");
        FIFTH_CURSE_ENABLED = BUILDER
            .comment("Enable or disable the Fifth Curse (Eternal Flames)")
            .comment("When enabled, fire never extinguishes naturally")
            .define("enabled", true);
        FIFTH_CURSE_FIRE_TICK_INCREASE = BUILDER
            .comment("Fire ticks added per game tick when player is on fire")
            .comment("2 = fire never extinguishes (default), 0 = fire behaves normally, higher = burns faster")
            .defineInRange("fire_tick_increase", 2, 0, 20);
        BUILDER.pop();

        // Sixth Curse - NEW CONFIGURATION
        BUILDER.push("sixth_curse_soul_tear");
        SIXTH_CURSE_ENABLED = BUILDER
            .comment("Enable or disable the Sixth Curse (Soul Tear)")
            .comment("When enabled, soul fragments are dropped on death")
            .define("enabled", true);
        SIXTH_CURSE_SOUL_DROP_CHANCE = BUILDER
            .comment("Chance to drop soul crystal on death")
            .comment("1.0 = 100% chance, 0.5 = 50% chance, 0.0 = never drops")
            .defineInRange("soul_drop_chance", 1.0, 0.0, 1.0);
        SIXTH_CURSE_MAX_HEALTH_LOSS = BUILDER
            .comment("Maximum health lost per death (in half-hearts)")
            .comment("2.0 = 1 full heart lost, 4.0 = 2 hearts, 0.0 = no health loss")
            .defineInRange("max_health_loss_per_death", 2.0, 0.0, 20.0);
        BUILDER.pop();

        // Seventh Curse
        BUILDER.push("seventh_curse_eternal_insomnia");
        SEVENTH_CURSE_ENABLED = BUILDER
            .comment("Enable or disable the Seventh Curse (Eternal Insomnia)")
            .comment("When enabled, player cannot skip night by sleeping")
            .define("enabled", true);
        SEVENTH_CURSE_PREVENT_SLEEP = BUILDER
            .comment("Prevent player from sleeping through the night")
            .comment("Can still set respawn point but cannot skip time")
            .define("prevent_sleep", true);
        BUILDER.pop();

        // Knockback Curse
        BUILDER.push("knockback_vulnerability");
        KNOCKBACK_CURSE_ENABLED = BUILDER
            .comment("Enable or disable Knockback Vulnerability (Extra Curse)")
            .comment("When enabled, player receives increased knockback")
            .define("enabled", true);
        KNOCKBACK_CURSE_MULTIPLIER = BUILDER
            .comment("Knockback multiplier")
            .comment("2.0 = 200% knockback (double), 1.0 = normal knockback, 0.5 = half knockback")
            .defineInRange("knockback_multiplier", 2.0, 0.1, 10.0);
        BUILDER.pop();

        // Ring Binding
        BUILDER.push("ring_binding");
        DISABLE_ETERNAL_BINDING = BUILDER
            .comment("Disable eternal binding of the Cursed Ring")
            .comment("When enabled, the ring can be removed in survival mode")
            .define("disable_eternal_binding", false);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
