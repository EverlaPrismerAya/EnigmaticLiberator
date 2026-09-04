package net.everla.enigmaticliberator.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.everla.enigmaticliberator.config.CurseConfig;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private ConfigCategory currentCategory = ConfigCategory.CURSES;
    private int scrollOffset = 0;
    private static final int ENTRY_HEIGHT = 45; // Height per config entry
    private boolean canEdit = true;
    private String permissionMessage = "";
    private Map<ConfigEntry, EditBox> editBoxes = new HashMap<>();

    private enum ConfigCategory {
        CURSES("Curses"),
        BLESSINGS("Blessings"),
        EXTRA("Extra");

        private final String displayName;

        ConfigCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public ConfigScreen(Screen parent) {
        super(Component.literal("Enigmatic Liberator Configuration"));
        this.parent = parent;
        checkPermissions();
    }

    /**
     * Check if the player has permission to edit server configs
     */
    private void checkPermissions() {
        Minecraft mc = Minecraft.getInstance();

        // If in single-player, always allow editing server configs
        if (mc.level == null || mc.getSingleplayerServer() != null) {
            canEdit = true;
            permissionMessage = "§aEditing local configuration";
            return;
        }

        // If connected to a multiplayer server, check operator status
        if (mc.player != null) {
            // Check if player has operator permissions (level 2 or higher)
            boolean hasPermission = mc.player.hasPermissions(2);

            if (!hasPermission) {
                canEdit = false;
                permissionMessage = "§eEditing client-side configuration only (no server permissions)";
            } else {
                canEdit = true;
                permissionMessage = "§aEditing server configuration (administrator)";
            }
        } else {
            canEdit = false;
            permissionMessage = "§cUnable to verify permissions";
        }
    }

    /**
     * Check if we're in a multiplayer environment
     */
    private boolean isMultiplayer() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null && mc.getSingleplayerServer() == null;
    }

    @Override
    protected void init() {
        super.init();
        editBoxes.clear();

        // Category buttons at the top
        int buttonWidth = 100;
        int buttonHeight = 20;
        int startX = (this.width - (buttonWidth * 3 + 10)) / 2;
        int startY = 20;

        this.addRenderableWidget(Button.builder(
            Component.literal("Curses"),
            btn -> switchCategory(ConfigCategory.CURSES))
            .bounds(startX, startY, buttonWidth, buttonHeight)
            .build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Blessings"),
            btn -> switchCategory(ConfigCategory.BLESSINGS))
            .bounds(startX + buttonWidth + 5, startY, buttonWidth, buttonHeight)
            .build());

        this.addRenderableWidget(Button.builder(
            Component.literal("Extra"),
            btn -> switchCategory(ConfigCategory.EXTRA))
            .bounds(startX + (buttonWidth + 5) * 2, startY, buttonWidth, buttonHeight)
            .build());

        // Done button
        this.addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> this.minecraft.setScreen(parent))
            .bounds(this.width / 2 - 50, this.height - 30, 100, 20)
            .build());

        // Add config entry buttons based on category
        rebuildConfigEntries();
    }

    private void switchCategory(ConfigCategory category) {
        this.currentCategory = category;
        this.scrollOffset = 0;
        this.rebuildAndInit();
    }

    private void scrollUp() {
        if (scrollOffset > 0) {
            scrollOffset--;
            this.rebuildAndInit();
        }
    }

    private void scrollDown() {
        int maxScroll = getMaxScrollOffset();
        if (scrollOffset < maxScroll) {
            scrollOffset++;
            this.rebuildAndInit();
        }
    }

    private int getMaxScrollOffset() {
        List<ConfigEntry> entries = getCurrentCategoryEntries();
        int entriesPerPage = getEntriesPerPage();
        return Math.max(0, entries.size() - entriesPerPage);
    }

    /**
     * Calculate how many entries can fit on screen based on window height
     */
    private int getEntriesPerPage() {
        int startY = 85; // Where entries start
        int endY = this.height - 60; // Leave space for Done button
        int availableHeight = endY - startY;
        return Math.max(1, availableHeight / ENTRY_HEIGHT);
    }

    private void rebuildAndInit() {
        this.clearWidgets();
        this.init();
    }

    private void rebuildConfigEntries() {
        List<ConfigEntry> entries = getCurrentCategoryEntries();

        int startY = 85;
        int visibleStart = scrollOffset;
        int entriesPerPage = getEntriesPerPage();
        int visibleEnd = Math.min(entries.size(), scrollOffset + entriesPerPage);

        // Calculate dynamic positions based on screen width
        int labelX = 20;
        int availableWidth = this.width - 40; // Leave margins on both sides
        int controlsWidth = 130; // Width needed for input + reset button
        int labelWidth = Math.min(200, availableWidth - controlsWidth - 20); // Dynamic label width

        for (int i = visibleStart; i < visibleEnd; i++) {
            ConfigEntry entry = entries.get(i);
            int yPos = startY + (i - scrollOffset) * ENTRY_HEIGHT;

            // Disable editing if no permission in multiplayer
            boolean canModify = canEdit || !isMultiplayer();

            int controlY = yPos + 25; // Position controls below description

            // Toggle button for boolean values
            if (entry.type == ConfigEntry.Type.BOOLEAN) {
                ForgeConfigSpec.BooleanValue boolValue = (ForgeConfigSpec.BooleanValue) entry.value;

                // Calculate button position from right side
                int buttonX = this.width - 100;

                // Toggle button
                Button toggleButton = Button.builder(
                    Component.literal((boolValue.get() ? "§aON" : "§cOFF")),
                    btn -> {
                        if (canModify) {
                            boolValue.set(!boolValue.get());
                            entry.spec.save();
                            rebuildAndInit();
                        }
                    })
                    .bounds(buttonX, controlY, 50, 20)
                    .build();
                toggleButton.active = canModify;
                this.addRenderableWidget(toggleButton);

                // Reset button
                Button resetButton = Button.builder(
                    Component.literal("⟲"),
                    btn -> {
                        if (canModify) {
                            boolValue.set((Boolean) entry.defaultValue);
                            entry.spec.save();
                            rebuildAndInit();
                        }
                    })
                    .bounds(buttonX + 55, controlY, 20, 20)
                    .build();
                resetButton.active = canModify;
                this.addRenderableWidget(resetButton);
            }
            // Text field for numeric values
            else if (entry.type == ConfigEntry.Type.INTEGER || entry.type == ConfigEntry.Type.DOUBLE) {
                String currentValue = entry.type == ConfigEntry.Type.INTEGER
                    ? String.valueOf(((ForgeConfigSpec.IntValue) entry.value).get())
                    : String.valueOf(((ForgeConfigSpec.DoubleValue) entry.value).get());

                // Calculate positions from right side
                int resetButtonX = this.width - 45;
                int editBoxX = resetButtonX - 85;
                int rangeTextX = editBoxX - 10; // Range text ends just before the edit box

                // Text input box
                EditBox editBox = new EditBox(this.font, editBoxX, controlY, 80, 18, Component.literal(""));
                editBox.setValue(currentValue);
                editBox.setMaxLength(10);
                editBox.setEditable(canModify);

                // Validate and save on focus loss
                editBox.setResponder(text -> {
                    if (!canModify) return;

                    try {
                        if (entry.type == ConfigEntry.Type.INTEGER) {
                            int value = Integer.parseInt(text);
                            ForgeConfigSpec.IntValue intValue = (ForgeConfigSpec.IntValue) entry.value;
                            intValue.set(value);
                            entry.spec.save();
                        } else {
                            double value = Double.parseDouble(text);
                            ForgeConfigSpec.DoubleValue doubleValue = (ForgeConfigSpec.DoubleValue) entry.value;
                            doubleValue.set(value);
                            entry.spec.save();
                        }
                    } catch (NumberFormatException e) {
                        // Invalid number, ignore
                    }
                });

                this.addRenderableWidget(editBox);
                editBoxes.put(entry, editBox);

                // Reset button
                Button resetButton = Button.builder(
                    Component.literal("⟲"),
                    btn -> {
                        if (canModify) {
                            if (entry.type == ConfigEntry.Type.INTEGER) {
                                ForgeConfigSpec.IntValue intValue = (ForgeConfigSpec.IntValue) entry.value;
                                intValue.set((Integer) entry.defaultValue);
                            } else {
                                ForgeConfigSpec.DoubleValue doubleValue = (ForgeConfigSpec.DoubleValue) entry.value;
                                doubleValue.set((Double) entry.defaultValue);
                            }
                            entry.spec.save();
                            rebuildAndInit();
                        }
                    })
                    .bounds(resetButtonX, controlY, 20, 20)
                    .build();
                resetButton.active = canModify;
                this.addRenderableWidget(resetButton);
            }
        }
    }

    private List<ConfigEntry> getCurrentCategoryEntries() {
        List<ConfigEntry> entries = new ArrayList<>();

        switch (currentCategory) {
            case CURSES:
                entries.add(new ConfigEntry("First Curse (Pain)", CurseConfig.FIRST_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "The First Curse amplifies all damage received", null));
                entries.add(new ConfigEntry("  Damage Multiplier", CurseConfig.FIRST_CURSE_DAMAGE_MULTIPLIER, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 2.0, "Multiplier for incoming damage", "Range: 0.1 - 10.0"));

                entries.add(new ConfigEntry("Second Curse (Hostility)", CurseConfig.SECOND_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "The Second Curse causes neutral mobs to become hostile", null));
                entries.add(new ConfigEntry("  Anger Range", CurseConfig.SECOND_CURSE_ANGER_RANGE, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 24.0, "Range in which neutral creatures are angered", "Range: 4.0 - 128.0"));
                entries.add(new ConfigEntry("  XRay Range", CurseConfig.SECOND_CURSE_XRAY_RANGE, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 4.0, "Range in which neutral creatures can see through walls", "Range: 0.0 - 64.0"));
                entries.add(new ConfigEntry("  Enderman Range", CurseConfig.SECOND_CURSE_ENDERMAN_RANGE, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 32.0, "Range for Enderman random teleportation", "Range: 8.0 - 128.0"));
                entries.add(new ConfigEntry("  Enderman Frequency", CurseConfig.SECOND_CURSE_ENDERMAN_FREQUENCY, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 1.0, "Frequency of Enderman teleportation attempts", "Range: 0.01 - 10.0"));
                entries.add(new ConfigEntry("  Save The Bees", CurseConfig.SECOND_CURSE_SAVE_BEES, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, false, "If true, bees will not be affected by the Second Curse", null));

                entries.add(new ConfigEntry("Third Curse (Armor)", CurseConfig.THIRD_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "The Third Curse reduces armor effectiveness", null));
                entries.add(new ConfigEntry("  Armor Reduction", CurseConfig.THIRD_CURSE_ARMOR_REDUCTION, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 0.3, "Percentage of armor effectiveness reduced", "Range: 0.0 - 1.0"));

                entries.add(new ConfigEntry("Fourth Curse (Damage)", CurseConfig.FOURTH_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "The Fourth Curse reduces damage dealt to enemies", null));
                entries.add(new ConfigEntry("  Damage Reduction", CurseConfig.FOURTH_CURSE_DAMAGE_REDUCTION, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 0.5, "Percentage of damage reduced", "Range: 0.0 - 1.0"));

                entries.add(new ConfigEntry("Fifth Curse (Fire)", CurseConfig.FIFTH_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "The Fifth Curse makes you more vulnerable to fire", null));
                entries.add(new ConfigEntry("  Fire Tick Increase", CurseConfig.FIFTH_CURSE_FIRE_TICK_INCREASE, CurseConfig.SPEC, ConfigEntry.Type.INTEGER, 2, "Additional fire ticks when set on fire", "Range: 0 - 100"));

                entries.add(new ConfigEntry("Sixth Curse (Soul)", CurseConfig.SIXTH_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "The Sixth Curse causes soul loss and max health reduction", null));
                entries.add(new ConfigEntry("  Soul Drop Chance", CurseConfig.SIXTH_CURSE_SOUL_DROP_CHANCE, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 1.0, "Chance to lose a soul crystal on death", "Range: 0.0 - 1.0"));
                entries.add(new ConfigEntry("  Max Health Loss", CurseConfig.SIXTH_CURSE_MAX_HEALTH_LOSS, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 2.0, "Max health lost per soul crystal", "Range: 0.0 - 20.0"));

                entries.add(new ConfigEntry("Seventh Curse (Insomnia)", CurseConfig.SEVENTH_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "The Seventh Curse prevents sleep and attracts phantoms", null));
                entries.add(new ConfigEntry("  Prevent Sleep", CurseConfig.SEVENTH_CURSE_PREVENT_SLEEP, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Prevents player from sleeping in beds", null));

                entries.add(new ConfigEntry("Knockback Curse", CurseConfig.KNOCKBACK_CURSE_ENABLED, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Extra curse: Increases knockback received", null));
                entries.add(new ConfigEntry("  Knockback Multiplier", CurseConfig.KNOCKBACK_CURSE_MULTIPLIER, CurseConfig.SPEC, ConfigEntry.Type.DOUBLE, 2.0, "Multiplier for knockback received", "Range: 0.1 - 10.0"));

                entries.add(new ConfigEntry("Disable Eternal Binding", CurseConfig.DISABLE_ETERNAL_BINDING, CurseConfig.SPEC, ConfigEntry.Type.BOOLEAN, false, "Allow ring removal in survival mode", null));
                break;

            case BLESSINGS:
                entries.add(new ConfigEntry("Looting Blessing", BlessingConfig.LOOTING_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Grants Looting enchantment bonus", null));
                entries.add(new ConfigEntry("  Looting Levels", BlessingConfig.LOOTING_BONUS_LEVELS, BlessingConfig.SPEC, ConfigEntry.Type.INTEGER, 2, "Additional Looting enchantment levels", "Range: 0 - 10"));

                entries.add(new ConfigEntry("Fortune Blessing", BlessingConfig.FORTUNE_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Grants Fortune enchantment bonus", null));
                entries.add(new ConfigEntry("  Fortune Levels", BlessingConfig.FORTUNE_BONUS_LEVELS, BlessingConfig.SPEC, ConfigEntry.Type.INTEGER, 2, "Additional Fortune enchantment levels", "Range: 0 - 10"));

                entries.add(new ConfigEntry("Experience Blessing", BlessingConfig.EXPERIENCE_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Multiplies experience gained", null));
                entries.add(new ConfigEntry("  Experience Multiplier", BlessingConfig.EXPERIENCE_MULTIPLIER, BlessingConfig.SPEC, ConfigEntry.Type.DOUBLE, 1.0, "Multiplier for experience drops", "Range: 0.0 - 10.0"));

                entries.add(new ConfigEntry("Enchanting Blessing", BlessingConfig.ENCHANTING_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Increases enchantment table power", null));
                entries.add(new ConfigEntry("  Enchanting Power", BlessingConfig.ENCHANTING_BONUS_POWER, BlessingConfig.SPEC, ConfigEntry.Type.INTEGER, 5, "Additional enchantment power bonus", "Range: 0 - 15"));

                entries.add(new ConfigEntry("Special Drops", BlessingConfig.SPECIAL_DROPS_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables special drops from vanilla mobs", null));
                entries.add(new ConfigEntry("Ender Ring", BlessingConfig.ENDER_RING_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Grants access to Ender Ring abilities", null));
                entries.add(new ConfigEntry("Abyssal Heart", BlessingConfig.ABYSSAL_HEART_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables Abyssal Heart effects", null));
                entries.add(new ConfigEntry("Eldritch Pan", BlessingConfig.ELDRITCH_PAN_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables Eldritch Pan usage", null));
                entries.add(new ConfigEntry("Eldritch Amulet", BlessingConfig.ELDRITCH_AMULET_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables Eldritch Amulet effects", null));
                entries.add(new ConfigEntry("The Infinitum", BlessingConfig.THE_INFINITUM_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables The Infinitum attacks", null));
                entries.add(new ConfigEntry("Desolation Ring", BlessingConfig.DESOLATION_RING_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables Desolation Ring binding", null));
                entries.add(new ConfigEntry("Violence Scroll", BlessingConfig.VIOLENCE_SCROLL_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables Violence Scroll effects", null));
                entries.add(new ConfigEntry("Chaos Elytra", BlessingConfig.CHAOS_ELYTRA_ENABLED, BlessingConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enables Chaos Elytra special effects", null));
                break;

            case EXTRA:
                entries.add(new ConfigEntry("Enable Special Drops", ExtraConfig.ENABLE_SPECIAL_DROPS, ExtraConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Enable ALL special drops from vanilla mobs", null));
                entries.add(new ConfigEntry("Ultra Hardcore Mode", ExtraConfig.ULTRA_HARDCORE, ExtraConfig.SPEC, ConfigEntry.Type.BOOLEAN, false, "Ring equips immediately when entering new world", null));
                entries.add(new ConfigEntry("Auto Equip", ExtraConfig.AUTO_EQUIP, ExtraConfig.SPEC, ConfigEntry.Type.BOOLEAN, false, "Ring equips immediately when entering inventory", null));
                entries.add(new ConfigEntry("Enable Lore", ExtraConfig.ENABLE_LORE, ExtraConfig.SPEC, ConfigEntry.Type.BOOLEAN, true, "Display lore text on the ring tooltip", null));
                entries.add(new ConfigEntry("Conceal Abilities", ExtraConfig.CONCEAL_ABILITIES, ExtraConfig.SPEC, ConfigEntry.Type.BOOLEAN, false, "Hide tooltip until ring is equipped", null));
                entries.add(new ConfigEntry("Super Cursed Time", ExtraConfig.SUPER_CURSED_TIME, ExtraConfig.SPEC, ConfigEntry.Type.DOUBLE, 0.995, "Fraction of playtime required for Abyssal Artifacts", "Range: 0.0 - 1.0"));
                break;
        }

        return entries;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        // Scroll up (delta > 0) or down (delta < 0)
        if (delta > 0) {
            scrollUp();
        } else if (delta < 0) {
            scrollDown();
        }
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);

        // Title
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 0xFFFFFF);

        // Category indicator
        String categoryText = "Category: " + currentCategory.getDisplayName();
        graphics.drawCenteredString(this.font, categoryText, this.width / 2, 50, 0xFFD700);

        // Permission message
        if (!permissionMessage.isEmpty()) {
            graphics.drawCenteredString(this.font, permissionMessage, this.width / 2, 65, 0xFFFFFF);
        }

        // Render labels for config entries
        List<ConfigEntry> entries = getCurrentCategoryEntries();
        int startY = 85;
        int visibleStart = scrollOffset;
        int entriesPerPage = getEntriesPerPage();
        int visibleEnd = Math.min(entries.size(), scrollOffset + entriesPerPage);

        for (int i = visibleStart; i < visibleEnd; i++) {
            ConfigEntry entry = entries.get(i);
            int yPos = startY + (i - scrollOffset) * ENTRY_HEIGHT;

            // Draw config name
            graphics.drawString(this.font, entry.name, 20, yPos + 5, 0xFFFFFF);

            // Draw description if available (left aligned below name)
            if (entry.description != null && !entry.description.isEmpty()) {
                // Wrap description text if too long
                int maxDescWidth = this.width - 60;
                String desc = entry.description;
                if (this.font.width(desc) > maxDescWidth) {
                    // Simple truncation with ellipsis
                    while (this.font.width(desc + "...") > maxDescWidth && desc.length() > 0) {
                        desc = desc.substring(0, desc.length() - 1);
                    }
                    desc = desc + "...";
                }
                graphics.drawString(this.font, "§7" + desc, 20, yPos + 15, 0xAAAAAA);
            }

            // Draw range if available (to the left of input box for numeric values)
            if (entry.range != null && !entry.range.isEmpty() &&
                (entry.type == ConfigEntry.Type.INTEGER || entry.type == ConfigEntry.Type.DOUBLE)) {

                int resetButtonX = this.width - 45;
                int editBoxX = resetButtonX - 85;

                // Draw range text right-aligned to end just before the edit box
                String rangeText = "§8" + entry.range;
                int rangeWidth = this.font.width(rangeText);
                int rangeX = editBoxX - rangeWidth - 5;

                graphics.drawString(this.font, rangeText, rangeX, yPos + 28, 0x888888);
            }
        }

        // Draw scrollbar on the right side
        drawScrollbar(graphics);

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private void drawScrollbar(GuiGraphics graphics) {
        int maxScroll = getMaxScrollOffset();
        if (maxScroll <= 0) {
            return; // No scrollbar needed
        }

        // Scrollbar dimensions
        int scrollbarX = this.width - 15;
        int scrollbarTop = 85;
        int scrollbarBottom = this.height - 60;
        int scrollbarHeight = scrollbarBottom - scrollbarTop;
        int scrollbarWidth = 6;

        // Draw scrollbar background (darker gray)
        graphics.fill(scrollbarX, scrollbarTop, scrollbarX + scrollbarWidth, scrollbarBottom, 0xFF3C3C3C);

        // Calculate scrollbar thumb size and position
        float scrollPercentage = (float) scrollOffset / maxScroll;
        int entriesPerPage = getEntriesPerPage();
        int thumbHeight = Math.max(20, scrollbarHeight / (maxScroll + entriesPerPage));
        int thumbY = scrollbarTop + (int) (scrollPercentage * (scrollbarHeight - thumbHeight));

        // Draw scrollbar thumb (lighter gray)
        graphics.fill(scrollbarX, thumbY, scrollbarX + scrollbarWidth, thumbY + thumbHeight, 0xFF8B8B8B);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check if clicking on scrollbar
        int scrollbarX = this.width - 15;
        int scrollbarTop = 85;
        int scrollbarBottom = this.height - 60;
        int scrollbarWidth = 6;

        if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth
            && mouseY >= scrollbarTop && mouseY <= scrollbarBottom) {

            int maxScroll = getMaxScrollOffset();
            if (maxScroll > 0) {
                // Calculate which position was clicked
                int scrollbarHeight = scrollbarBottom - scrollbarTop;
                float clickPercentage = (float) (mouseY - scrollbarTop) / scrollbarHeight;
                int newOffset = Math.round(clickPercentage * maxScroll);
                scrollOffset = Math.max(0, Math.min(maxScroll, newOffset));
                rebuildAndInit();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        // Allow dragging the scrollbar
        int scrollbarX = this.width - 15;
        int scrollbarTop = 85;
        int scrollbarBottom = this.height - 60;
        int scrollbarWidth = 6;

        if (mouseX >= scrollbarX - 10 && mouseX <= scrollbarX + scrollbarWidth + 10
            && mouseY >= scrollbarTop && mouseY <= scrollbarBottom) {

            int maxScroll = getMaxScrollOffset();
            if (maxScroll > 0) {
                int scrollbarHeight = scrollbarBottom - scrollbarTop;
                float clickPercentage = (float) (mouseY - scrollbarTop) / scrollbarHeight;
                int newOffset = Math.round(clickPercentage * maxScroll);
                scrollOffset = Math.max(0, Math.min(maxScroll, newOffset));
                rebuildAndInit();
                return true;
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class ConfigEntry {
        enum Type {
            BOOLEAN, INTEGER, DOUBLE
        }

        String name;
        Object value;
        ForgeConfigSpec spec;
        Type type;
        Object defaultValue;
        String description; // Configuration description
        String range; // Valid range for numeric values

        ConfigEntry(String name, Object value, ForgeConfigSpec spec, Type type, Object defaultValue) {
            this(name, value, spec, type, defaultValue, null, null);
        }

        ConfigEntry(String name, Object value, ForgeConfigSpec spec, Type type, Object defaultValue, String description, String range) {
            this.name = name;
            this.value = value;
            this.spec = spec;
            this.type = type;
            this.defaultValue = defaultValue;
            this.description = description;
            this.range = range;
        }
    }
}
