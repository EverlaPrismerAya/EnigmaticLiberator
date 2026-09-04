package net.everla.enigmaticliberator.handler;

import net.everla.enigmaticliberator.config.BlessingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

/** Prevents crafting the configured unique relics when their master switch is disabled. */
@Mod.EventBusSubscriber(modid = "enigmatic_liberator", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class UniqueRelicCraftingHandler {
    private static final Set<ResourceLocation> UNIQUE_RELICS = Set.of(
            new ResourceLocation("enigmaticlegacy", "abyssal_heart"),
            new ResourceLocation("enigmaticlegacy", "eldritch_pan"),
            new ResourceLocation("enigmaticlegacy", "eldritch_amulet"),
            new ResourceLocation("enigmaticlegacy", "the_infinitum"),
            new ResourceLocation("enigmaticlegacy", "desolation_ring"),
            new ResourceLocation("enigmaticaddons", "violence_scroll"),
            new ResourceLocation("enigmaticaddons", "chaos_elytra")
    );

    private UniqueRelicCraftingHandler() {
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!BlessingConfig.UNIQUE_RELICS_ENABLED.get()) {
            ItemStack result = event.getCrafting();
            if (UNIQUE_RELICS.contains(result.getItem().builtInRegistryHolder().key().location())) {
                result.setCount(0);
            }
        }
    }
}
