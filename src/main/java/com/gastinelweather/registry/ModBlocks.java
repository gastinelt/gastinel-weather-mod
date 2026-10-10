package com.gastinelweather.registry;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GastinelWeatherMod.MOD_ID);

    // --- Pôle Météo ---
    public static final DeferredBlock<Block> VP2_STATION = BLOCKS.register("vp2_station",
            () -> new Vp2StationBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> ANEMOMETER = BLOCKS.register("anemometer",
            () -> new AnemometerBlock(BlockBehaviour.Properties.of().strength(1.2f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> ULTRASONIC_ANEMOMETER = BLOCKS.register("ultrasonic_anemometer",
            () -> new UltrasonicAnemometerBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> RAIN_GAUGE = BLOCKS.register("rain_gauge",
            () -> new RainGaugeBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.GLASS).noOcclusion()));

    public static final DeferredBlock<Block> SNOW_GAUGE = BLOCKS.register("snow_gauge",
            () -> new SnowGaugeBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.METAL).noOcclusion()));

    // --- Pôle Radio ---
    public static final DeferredBlock<Block> ESP32_BOARD = BLOCKS.register("esp32_board",
            () -> new Esp32Block(BlockBehaviour.Properties.of().strength(0.8f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> DIRECTIONAL_ANTENNA = BLOCKS.register("directional_antenna",
            () -> new DirectionalAntennaBlock(BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> OMNI_ANTENNA = BLOCKS.register("omni_antenna",
            () -> new OmniAntennaBlock(BlockBehaviour.Properties.of().strength(1.2f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> VP2_CONSOLE = BLOCKS.register("vp2_console",
            () -> new Vp2ConsoleBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.METAL).noOcclusion()));

    // --- Pôle Informatique & Réseau ---
    public static final DeferredBlock<Block> PC_CASE = BLOCKS.register("pc_case",
            () -> new PcCaseBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> MONITOR = BLOCKS.register("monitor",
            () -> new MonitorBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.GLASS).noOcclusion()));

    public static final DeferredBlock<Block> NETWORK_ROUTER = BLOCKS.register("network_router",
            () -> new NetworkRouterBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> NETWORK_CABLE = BLOCKS.register("network_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().strength(0.3f).sound(SoundType.WOOL).noOcclusion(), CableBlock.CableType.NETWORK_INDOOR));

    public static final DeferredBlock<Block> OUTDOOR_NETWORK_CABLE = BLOCKS.register("outdoor_network_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.STONE).noOcclusion(), CableBlock.CableType.NETWORK_OUTDOOR));

    public static final DeferredBlock<Block> UNDERGROUND_NETWORK_CABLE = BLOCKS.register("underground_network_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.STONE).noOcclusion(), CableBlock.CableType.NETWORK_UNDERGROUND));

    // --- Pôle Énergie ---
    public static final DeferredBlock<Block> WIND_TURBINE = BLOCKS.register("wind_turbine",
            () -> new WindTurbineBlock(BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> HYDRO_TURBINE = BLOCKS.register("hydro_turbine",
            () -> new HydroTurbineBlock(BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.METAL).noOcclusion()));

    public static final DeferredBlock<Block> COAL_GENERATOR = BLOCKS.register("coal_generator",
            () -> new CoalGeneratorBlock(BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> EXHAUST_CHIMNEY = BLOCKS.register("exhaust_chimney",
            () -> new ExhaustChimneyBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ELECTRIC_CABLE = BLOCKS.register("electric_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().strength(0.4f).sound(SoundType.WOOL).noOcclusion(), CableBlock.CableType.ELECTRIC_INDOOR));

    public static final DeferredBlock<Block> UNDERGROUND_ELECTRIC_CABLE = BLOCKS.register("underground_electric_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().strength(1.2f).sound(SoundType.STONE).noOcclusion(), CableBlock.CableType.ELECTRIC_UNDERGROUND));
}
