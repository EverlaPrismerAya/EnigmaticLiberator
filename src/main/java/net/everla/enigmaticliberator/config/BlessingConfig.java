package net.everla.enigmaticliberator.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BlessingConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Blessing 1 - Looting
    public static final ForgeConfigSpec.BooleanValue LOOTING_ENABLED;
    public static final ForgeConfigSpec.IntValue LOOTING_BONUS_LEVELS;

    // Blessing 2 - Fortune
    public static final ForgeConfigSpec.BooleanValue FORTUNE_ENABLED;
    public static final ForgeConfigSpec.IntValue FORTUNE_BONUS_LEVELS;

    // Blessing 3 - Experience
    public static final ForgeConfigSpec.BooleanValue EXPERIENCE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue EXPERIENCE_MULTIPLIER;

    // Blessing 4 - Enchanting Power
    public static final ForgeConfigSpec.BooleanValue ENCHANTING_ENABLED;
    public static final ForgeConfigSpec.IntValue ENCHANTING_BONUS_POWER;

    // Blessing 5 - Special Drops
    public static final ForgeConfigSpec.BooleanValue SPECIAL_DROPS_ENABLED;
    public static final ForgeConfigSpec.DoubleValue SPECIAL_DROPS_MULTIPLIER;

    // Blessing 6 - Ender Ring
    public static final ForgeConfigSpec.BooleanValue ENDER_RING_ENABLED;

    // Blessing 7 - Abyssal Artifacts
    public static final ForgeConfigSpec.BooleanValue ABYSSAL_HEART_ENABLED;
    public static final ForgeConfigSpec.BooleanValue ELDRITCH_PAN_ENABLED;
    public static final ForgeConfigSpec.BooleanValue ELDRITCH_AMULET_ENABLED;
    public static final ForgeConfigSpec.BooleanValue THE_INFINITUM_ENABLED;
    public static final ForgeConfigSpec.BooleanValue DESOLATION_RING_ENABLED;
    public static final ForgeConfigSpec.BooleanValue VIOLENCE_SCROLL_ENABLED;
    public static final ForgeConfigSpec.BooleanValue CHAOS_ELYTRA_ENABLED;

    static {
        BUILDER.push("Blessings Configuration");
        BUILDER.comment("Configure the Seven Blessings of the Cursed Ring");
        BUILDER.comment("Each blessing can be individually enabled/disabled and its power adjusted");

        // Looting
        BUILDER.push("blessing_1_looting");
        LOOTING_ENABLED = BUILDER
            .comment("Enable or disable Looting bonus")
            .comment("When enabled, grants bonus Looting levels")
            .define("enabled", true);
        LOOTING_BONUS_LEVELS = BUILDER
            .comment("Bonus Looting levels granted by the ring")
            .comment("1 = Looting I, 2 = Looting II, etc.")
            .defineInRange("bonus_levels", 1, 0, 10);
        BUILDER.pop();

        // Fortune
        BUILDER.push("blessing_2_fortune");
        FORTUNE_ENABLED = BUILDER
            .comment("Enable or disable Fortune bonus")
            .comment("When enabled, grants bonus Fortune levels")
            .define("enabled", true);
        FORTUNE_BONUS_LEVELS = BUILDER
            .comment("Bonus Fortune levels granted by the ring")
            .comment("1 = Fortune I, 2 = Fortune II, etc.")
            .defineInRange("bonus_levels", 1, 0, 10);
        BUILDER.pop();

        // Experience
        BUILDER.push("blessing_3_experience");
        EXPERIENCE_ENABLED = BUILDER
            .comment("Enable or disable Experience bonus")
            .comment("When enabled, multiplies experience dropped from mobs")
            .define("enabled", true);
        EXPERIENCE_MULTIPLIER = BUILDER
            .comment("Experience multiplier")
            .comment("4.0 = 400% experience (4x normal), 1.0 = normal experience, 10.0 = 10x experience")
            .defineInRange("multiplier", 4.0, 1.0, 20.0);
        BUILDER.pop();

        // Enchanting Power
        BUILDER.push("blessing_4_enchanting");
        ENCHANTING_ENABLED = BUILDER
            .comment("Enable or disable Enchanting Power bonus")
            .comment("When enabled, increases enchanting table power")
            .define("enabled", true);
        ENCHANTING_BONUS_POWER = BUILDER
            .comment("Bonus enchanting power (equivalent to bookshelves)")
            .comment("10 = equivalent to 10 extra bookshelves, 15 = max enchanting table")
            .defineInRange("bonus_power", 10, 0, 50);
        BUILDER.pop();

        // Special Drops
        BUILDER.push("blessing_5_special_drops");
        SPECIAL_DROPS_ENABLED = BUILDER
            .comment("Enable or disable Special Mob Drops")
            .comment("When enabled, allows special items to drop from certain mobs")
            .define("enabled", true);
        SPECIAL_DROPS_MULTIPLIER = BUILDER
            .comment("Drop chance multiplier for special items")
            .comment("1.0 = normal chance, 2.0 = double chance, 0.5 = half chance")
            .defineInRange("drop_chance_multiplier", 1.0, 0.0, 10.0);
        BUILDER.pop();

        // Ender Ring
        BUILDER.push("blessing_6_ender_ring");
        ENDER_RING_ENABLED = BUILDER
            .comment("Enable or disable Ender Ring functionality")
            .comment("When enabled, grants access to Ender Chest from anywhere")
            .define("enabled", true);
        BUILDER.pop();

        // Abyssal Artifacts
        BUILDER.push("blessing_7_abyssal_artifacts");
        BUILDER.comment("Configure individual Abyssal Artifacts from Enigmatic Legacy and EnigmaticAddons");
        BUILDER.comment("When disabled, the corresponding item loses its special effects");

        ABYSSAL_HEART_ENABLED = BUILDER
            .comment("Enable or disable Abyssal Heart")
            .comment("When disabled, Abyssal Heart will not provide any benefits")
            .define("abyssal_heart_enabled", true);

        ELDRITCH_PAN_ENABLED = BUILDER
            .comment("Enable or disable Eldritch Pan")
            .comment("When disabled, Eldritch Pan cannot be used for attacking or blocking")
            .define("eldritch_pan_enabled", true);

        ELDRITCH_AMULET_ENABLED = BUILDER
            .comment("Enable or disable Eldritch Amulet")
            .comment("When disabled, Eldritch Amulet cannot be equipped and loses all effects")
            .define("eldritch_amulet_enabled", true);

        THE_INFINITUM_ENABLED = BUILDER
            .comment("Enable or disable The Infinitum")
            .comment("When disabled, The Infinitum cannot be used for attacking")
            .define("the_infinitum_enabled", true);

        DESOLATION_RING_ENABLED = BUILDER
            .comment("Enable or disable Desolation Ring")
            .comment("When disabled, Desolation Ring loses its binding and all effects")
            .define("desolation_ring_enabled", true);

        VIOLENCE_SCROLL_ENABLED = BUILDER
            .comment("Enable or disable Violence Scroll")
            .comment("When disabled, Violence Scroll loses all its functions")
            .define("violence_scroll_enabled", true);

        CHAOS_ELYTRA_ENABLED = BUILDER
            .comment("Enable or disable Chaos Elytra special effects")
            .comment("When disabled, Chaos Elytra functions as a normal elytra only")
            .define("chaos_elytra_enabled", true);

        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
