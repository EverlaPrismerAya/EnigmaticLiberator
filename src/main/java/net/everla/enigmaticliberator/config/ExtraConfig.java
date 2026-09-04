package net.everla.enigmaticliberator.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExtraConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Neutral Hostility Settings
    public static final ForgeConfigSpec.DoubleValue NEUTRAL_ANGER_RANGE;
    public static final ForgeConfigSpec.DoubleValue NEUTRAL_XRAY_RANGE;

    // Enderman Teleport Settings
    public static final ForgeConfigSpec.DoubleValue ENDERMEN_RANDOMPORT_RANGE;
    public static final ForgeConfigSpec.DoubleValue ENDERMEN_RANDOMPORT_FREQUENCY;

    // Special Mechanics
    public static final ForgeConfigSpec.BooleanValue SAVE_THE_BEES;
    public static final ForgeConfigSpec.BooleanValue ULTRA_HARDCORE;
    public static final ForgeConfigSpec.BooleanValue AUTO_EQUIP;

    // Tooltip Settings
    public static final ForgeConfigSpec.BooleanValue ENABLE_LORE;
    public static final ForgeConfigSpec.BooleanValue CONCEAL_ABILITIES;

    // Super Cursed Time (for Abyssal Artifacts)
    public static final ForgeConfigSpec.DoubleValue SUPER_CURSED_TIME;

    static {
        BUILDER.comment("Extra Cursed Ring Configuration").push("extra");

        // Neutral Hostility
        BUILDER.comment("Settings for the Second Curse - Neutral Hostility").push("neutral_hostility");
        NEUTRAL_ANGER_RANGE = BUILDER
            .comment("Range in which neutral creatures are angered against bearers of the ring.")
            .defineInRange("neutral_anger_range", 24.0, 4.0, 128.0);
        NEUTRAL_XRAY_RANGE = BUILDER
            .comment("Range in which neutral creatures can see and target bearers of the ring even if they can't directly see them.")
            .defineInRange("neutral_xray_range", 4.0, 0.0, 64.0);
        SAVE_THE_BEES = BUILDER
            .comment("If true, bees will never be affected by the Second Curse of Ring of the Seven Curses.")
            .comment("This category exists solely because of Jusey1z who really wanted to protect his bees.")
            .define("save_the_bees", false);
        BUILDER.pop();

        // Enderman Teleportation
        BUILDER.comment("Settings for Enderman random teleportation to ring bearers").push("enderman_teleport");
        ENDERMEN_RANDOMPORT_RANGE = BUILDER
            .comment("Range in which Endermen can try to randomly teleport to bearers of the ring.")
            .defineInRange("endermen_randomport_range", 32.0, 8.0, 128.0);
        ENDERMEN_RANDOMPORT_FREQUENCY = BUILDER
            .comment("Allows to adjust how frequently Endermen will try to randomly teleport to player bearing the ring,")
            .comment("even if they can't see the player and are not angered yet.")
            .comment("Lower value = less probability of this happening.")
            .defineInRange("endermen_randomport_frequency", 1.0, 0.01, 10.0);
        BUILDER.pop();

        // Ring Behavior
        BUILDER.comment("Ring behavior settings").push("ring_behavior");
        ULTRA_HARDCORE = BUILDER
            .comment("If true, Ring of the Seven Curses will be equipped into player's ring slot right away")
            .comment("when entering a new world, instead of just being added to their inventory.")
            .define("ultra_hardcore", false);
        AUTO_EQUIP = BUILDER
            .comment("If true, Ring of the Seven Curses will be equipped into player's ring slot right away")
            .comment("when it enters their inventory. This is different from ultra hardcore option as the way")
            .comment("through which ring ends up in player's inventory does not matter.")
            .define("auto_equip", false);
        BUILDER.pop();

        // Tooltip Settings
        BUILDER.comment("Tooltip display settings").push("tooltip");
        ENABLE_LORE = BUILDER
            .comment("Set to false to disable displaying lore on Ring of the Seven Curses.")
            .comment("Useful if you are a modpack developer wanting to have your own.")
            .comment("Note: EnigmaticLiberator already overrides the tooltip, this only affects the lore text visibility.")
            .define("enable_lore", true);
        CONCEAL_ABILITIES = BUILDER
            .comment("If true, tooltip of Ring of the Seven Curses cannot be read before it is equipped.")
            .comment("Fun way to teach players that not every mystery is worth investigating.")
            .comment("Note: This affects the original tooltip, EnigmaticLiberator's tooltip is not affected.")
            .define("conceal_abilities", false);
        BUILDER.pop();

        // Super Cursed Time
        BUILDER.comment("Abyssal Artifacts requirement").push("super_cursed");
        SUPER_CURSED_TIME = BUILDER
            .comment("A fraction of time the player should bear the Seven Curses to use Abyssal Artifacts.")
            .comment("0.995 means player needs to bear the curse for 99.5% of their playtime.")
            .comment("Set to 0.0 to allow immediate use, 1.0 to require 100% uptime (impossible).")
            .defineInRange("super_cursed_time", 0.995, 0.0, 1.0);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
