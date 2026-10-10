package com.gastinelweather;

import com.gastinelweather.command.GastinelWeatherCommand;
import com.gastinelweather.weather.WeatherSystem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GastinelWeatherMod.MOD_ID)
public class GastinelWeatherMod {

    public static final String MOD_ID = "gastinelweather";
    private static final Logger LOGGER = LoggerFactory.getLogger(GastinelWeatherMod.class);

    private static WeatherSystem weatherSystem;

    public GastinelWeatherMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("[GastinelWeatherMod] Initialisation du moteur météo déterministe...");

        // Initialisation de la météo
        weatherSystem = new WeatherSystem();

        // Enregistrement des blocs, items et onglets créatifs
        com.gastinelweather.registry.ModBlocks.BLOCKS.register(modEventBus);
        com.gastinelweather.registry.ModItems.ITEMS.register(modEventBus);
        com.gastinelweather.registry.ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // Enregistrement des commandes
        NeoForge.EVENT_BUS.register(GastinelWeatherCommand.class);

        LOGGER.info("[GastinelWeatherMod] Blocs, Items, Capteurs météo et Énergie enregistrés avec succès.");
    }

    public static WeatherSystem getWeatherSystem() {
        if (weatherSystem == null) {
            weatherSystem = new WeatherSystem();
        }
        return weatherSystem;
    }
}
