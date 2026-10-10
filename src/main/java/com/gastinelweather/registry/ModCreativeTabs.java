package com.gastinelweather.registry;

import com.gastinelweather.GastinelWeatherMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GastinelWeatherMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WEATHER_TECH_TAB = CREATIVE_MODE_TABS.register("weather_tech_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("§6Gastinel Weather & Tech"))
                    .icon(() -> new ItemStack(ModBlocks.VP2_STATION.get()))
                    .displayItems((parameters, output) -> {
                        // Instruments Météo
                        output.accept(ModItems.ELITECH_LOGGER.get());
                        output.accept(ModBlocks.VP2_STATION.get());
                        output.accept(ModBlocks.ANEMOMETER.get());
                        output.accept(ModBlocks.ULTRASONIC_ANEMOMETER.get());
                        output.accept(ModBlocks.RAIN_GAUGE.get());
                        output.accept(ModBlocks.SNOW_GAUGE.get());

                        // Radio & Transmission
                        output.accept(ModBlocks.ESP32_BOARD.get());
                        output.accept(ModBlocks.DIRECTIONAL_ANTENNA.get());
                        output.accept(ModBlocks.OMNI_ANTENNA.get());
                        output.accept(ModBlocks.VP2_CONSOLE.get());

                        // Informatique & Réseau
                        output.accept(ModBlocks.PC_CASE.get());
                        output.accept(ModBlocks.MONITOR.get());
                        output.accept(ModBlocks.NETWORK_ROUTER.get());
                        output.accept(ModItems.MOTHERBOARD.get());
                        output.accept(ModItems.CPU.get());
                        output.accept(ModItems.CPU_COOLER.get());
                        output.accept(ModItems.GPU.get());
                        output.accept(ModItems.POWER_SUPPLY.get());
                        output.accept(ModItems.POWER_CABLE.get());
                        output.accept(ModBlocks.NETWORK_CABLE.get());
                        output.accept(ModBlocks.OUTDOOR_NETWORK_CABLE.get());
                        output.accept(ModBlocks.UNDERGROUND_NETWORK_CABLE.get());

                        // Énergie & Électricité
                        output.accept(ModBlocks.WIND_TURBINE.get());
                        output.accept(ModBlocks.HYDRO_TURBINE.get());
                        output.accept(ModBlocks.COAL_GENERATOR.get());
                        output.accept(ModBlocks.EXHAUST_CHIMNEY.get());
                        output.accept(ModItems.TRANSISTOR.get());
                        output.accept(ModBlocks.ELECTRIC_CABLE.get());
                        output.accept(ModBlocks.UNDERGROUND_ELECTRIC_CABLE.get());
                    })
                    .build());
}
