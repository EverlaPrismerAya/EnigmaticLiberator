package net.everla.enigmaticliberator.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExtraConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Special Mechanics
    public static final ForgeConfigSpec.BooleanValue ULTRA_HARDCORE;
    public static final ForgeConfigSpec.BooleanValue AUTO_EQUIP;
    public static final ForgeConfigSpec.BooleanValue CURSED_RING_EXTRA_SLOT;

    // Tooltip Settings
    public static final ForgeConfigSpec.BooleanValue ENABLE_LORE;
    public static final ForgeConfigSpec.BooleanValue CONCEAL_ABILITIES;

    // Super Cursed Time (for Abyssal Artifacts)
    public static final ForgeConfigSpec.DoubleValue SUPER_CURSED_TIME;

    // Enigmatic Amulet settings
    public static final ForgeConfigSpec.BooleanValue AMULET_REPLACE_GRAVITY;
    public static final ForgeConfigSpec.BooleanValue AMULET_REROLL_ON_SNEAK_DROP;

    static {
        BUILDER.comment("Extra Cursed Ring Configuration").push("extra");

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
        CURSED_RING_EXTRA_SLOT = BUILDER
            .comment("If true, wearing Ring of the Seven Curses grants one additional ring slot.")
            .define("cursed_ring_extra_slot", true);
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

        BUILDER.comment("Enigmatic Amulet behavior").push("enigmatic_amulet");
        AMULET_REPLACE_GRAVITY = BUILDER
            .comment("Replace the amulet's -25% gravity modifier with a +25% reach modifier.")
            .define("replace_gravity_with_reach", false);
        AMULET_REROLL_ON_SNEAK_DROP = BUILDER
            .comment("Reroll an Enigmatic Amulet when a sneaking player drops it.")
            .comment("Other drop paths, including death drops, are not affected.")
            .define("reroll_on_sneak_drop", false);
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
