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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(EnigmaticLiberator.MODID)
public class EnigmaticLiberator {
    public static final String MODID = "enigmatic_liberator";
    private static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")  // These APIs are deprecated but still functional in Forge 1.20.1
    public EnigmaticLiberator() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register configs
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CurseConfig.SPEC, "enigmatic-liberator-curses.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BlessingConfig.SPEC, "enigmatic-liberator-blessings.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExtraConfig.SPEC, "enigmatic-liberator-extra.toml");

        // Register setup
        modEventBus.addListener(this::setup);

        LOGGER.info("==================================================");
        LOGGER.info("EnigmaticLiberator initialized!");
        LOGGER.info("Making the Cursed Ring fully configurable...");
        LOGGER.info("==================================================");
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
