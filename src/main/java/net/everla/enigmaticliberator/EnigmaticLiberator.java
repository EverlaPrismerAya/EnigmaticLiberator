package net.everla.enigmaticliberator;

import com.mojang.logging.LogUtils;
import net.everla.enigmaticliberator.config.BlessingConfig;
import net.everla.enigmaticliberator.config.CurseConfig;
import net.everla.enigmaticliberator.config.ExtraConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import com.aizistral.enigmaticlegacy.items.EnigmaticAmulet;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.slf4j.Logger;

@Mod(EnigmaticLiberator.MODID)
public class EnigmaticLiberator {
    public static final String MODID = "enigmatic_liberator";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    @SuppressWarnings("removal")  // These APIs are deprecated but still functional in Forge 1.20.1
    public EnigmaticLiberator() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register configs
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CurseConfig.SPEC, "enigmatic-liberator-curses.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BlessingConfig.SPEC, "enigmatic-liberator-blessings.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExtraConfig.SPEC, "enigmatic-liberator-extra.toml");

        // Register setup
        modEventBus.addListener(this::setup);
        net.everla.enigmaticliberator.network.ConfigNetwork.register();

        LOGGER.info("==================================================");
        LOGGER.info("EnigmaticLiberator initialized!");
        LOGGER.info("Making the Cursed Ring fully configurable...");
        LOGGER.info("==================================================");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE)
    public static final class NetworkEvents {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player) {
                net.everla.enigmaticliberator.network.ConfigNetwork.sync(player);
            }
        }

        @SubscribeEvent
        public static void onConfigReload(ModConfigEvent.Reloading event) {
            if (event.getConfig().getType() == ModConfig.Type.COMMON
                    && net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer() != null) {
                net.everla.enigmaticliberator.network.ConfigNetwork.syncAll();
            }
        }

        @SubscribeEvent
        public static void onItemToss(ItemTossEvent event) {
            if (!ExtraConfig.AMULET_REROLL_ON_SNEAK_DROP.get()) {
                return;
            }

            net.minecraft.world.entity.player.Player player = event.getPlayer();
            net.minecraft.world.item.ItemStack stack = event.getEntity().getItem();
            if (!player.level().isClientSide
                    && player.isCrouching()
                    && stack.is(EnigmaticItems.ENIGMATIC_AMULET)) {
                ((EnigmaticAmulet) EnigmaticItems.ENIGMATIC_AMULET).setRandomColor(stack);
            }
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("========================================");
        LOGGER.info(" _____ _   _ ___ ____ __  __    _  _____ ___ ____ ");
        LOGGER.info("| ____| \\ | |_ _/ ___|  \\/  |  / \\|_   _|_ _/ ___|");
        LOGGER.info("|  _| |  \\| || | |  _| |\\/| | / _ \\ | |  | | |    ");
        LOGGER.info("| |___| |\\  || | |_| | |  | |/ ___ \\| |  | | |___ ");
        LOGGER.info("|_____|_| \\_|___\\____|_|  |_/_/   \\_\\_| |___\\____|");
        LOGGER.info("");
        LOGGER.info(" _     ___ ____  _____ ____      _  _____ ___  ____  ");
        LOGGER.info("| |   |_ _| __ )| ____|  _ \\    / \\|_   _/ _ \\|  _ \\ ");
        LOGGER.info("| |    | ||  _ \\|  _| | |_) |  / _ \\ | || | | | |_) |");
        LOGGER.info("| |___ | || |_) | |___|  _ <  / ___ \\| || |_| |  _ < ");
        LOGGER.info("|_____|___|____/|_____|_| \\_\\/_/   \\_\\_| \\___/|_| \\_\\");
        LOGGER.info("");
        LOGGER.info("EnigmaticLiberator setup complete!");
        LOGGER.info("All curses and blessings are now under your control!");
        LOGGER.info("========================================");
    }
}
