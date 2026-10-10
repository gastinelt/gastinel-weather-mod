package com.gastinelweather.registry;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.item.ElitechItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GastinelWeatherMod.MOD_ID);

    // --- Items autonomes ---
    public static final DeferredItem<ElitechItem> ELITECH_LOGGER = ITEMS.register("elitech_logger",
            () -> new ElitechItem(new Item.Properties().stacksTo(1)));

    // Composants Informatique
    public static final DeferredItem<Item> MOTHERBOARD = ITEMS.register("motherboard",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CPU = ITEMS.register("cpu",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CPU_COOLER = ITEMS.register("cpu_cooler",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GPU = ITEMS.register("gpu",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POWER_SUPPLY = ITEMS.register("power_supply",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> POWER_CABLE = ITEMS.register("power_cable",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TRANSISTOR = ITEMS.register("transistor",
            () -> new Item(new Item.Properties()));

    // --- Block Items (Pôle Météo) ---
    public static final DeferredItem<BlockItem> VP2_STATION_ITEM = ITEMS.registerSimpleBlockItem("vp2_station", ModBlocks.VP2_STATION);
    public static final DeferredItem<BlockItem> ANEMOMETER_ITEM = ITEMS.registerSimpleBlockItem("anemometer", ModBlocks.ANEMOMETER);
    public static final DeferredItem<BlockItem> ULTRASONIC_ANEMOMETER_ITEM = ITEMS.registerSimpleBlockItem("ultrasonic_anemometer", ModBlocks.ULTRASONIC_ANEMOMETER);
    public static final DeferredItem<BlockItem> RAIN_GAUGE_ITEM = ITEMS.registerSimpleBlockItem("rain_gauge", ModBlocks.RAIN_GAUGE);
    public static final DeferredItem<BlockItem> SNOW_GAUGE_ITEM = ITEMS.registerSimpleBlockItem("snow_gauge", ModBlocks.SNOW_GAUGE);

    // --- Block Items (Pôle Radio) ---
    public static final DeferredItem<BlockItem> ESP32_BOARD_ITEM = ITEMS.registerSimpleBlockItem("esp32_board", ModBlocks.ESP32_BOARD);
    public static final DeferredItem<BlockItem> DIRECTIONAL_ANTENNA_ITEM = ITEMS.registerSimpleBlockItem("directional_antenna", ModBlocks.DIRECTIONAL_ANTENNA);
    public static final DeferredItem<BlockItem> OMNI_ANTENNA_ITEM = ITEMS.registerSimpleBlockItem("omni_antenna", ModBlocks.OMNI_ANTENNA);
    public static final DeferredItem<BlockItem> VP2_CONSOLE_ITEM = ITEMS.registerSimpleBlockItem("vp2_console", ModBlocks.VP2_CONSOLE);

    // --- Block Items (Pôle Informatique) ---
    public static final DeferredItem<BlockItem> PC_CASE_ITEM = ITEMS.registerSimpleBlockItem("pc_case", ModBlocks.PC_CASE);
    public static final DeferredItem<BlockItem> MONITOR_ITEM = ITEMS.registerSimpleBlockItem("monitor", ModBlocks.MONITOR);
    public static final DeferredItem<BlockItem> NETWORK_ROUTER_ITEM = ITEMS.registerSimpleBlockItem("network_router", ModBlocks.NETWORK_ROUTER);
    public static final DeferredItem<BlockItem> NETWORK_CABLE_ITEM = ITEMS.registerSimpleBlockItem("network_cable", ModBlocks.NETWORK_CABLE);
    public static final DeferredItem<BlockItem> OUTDOOR_NETWORK_CABLE_ITEM = ITEMS.registerSimpleBlockItem("outdoor_network_cable", ModBlocks.OUTDOOR_NETWORK_CABLE);
    public static final DeferredItem<BlockItem> UNDERGROUND_NETWORK_CABLE_ITEM = ITEMS.registerSimpleBlockItem("underground_network_cable", ModBlocks.UNDERGROUND_NETWORK_CABLE);

    // --- Block Items (Pôle Énergie) ---
    public static final DeferredItem<BlockItem> WIND_TURBINE_ITEM = ITEMS.registerSimpleBlockItem("wind_turbine", ModBlocks.WIND_TURBINE);
    public static final DeferredItem<BlockItem> HYDRO_TURBINE_ITEM = ITEMS.registerSimpleBlockItem("hydro_turbine", ModBlocks.HYDRO_TURBINE);
    public static final DeferredItem<BlockItem> COAL_GENERATOR_ITEM = ITEMS.registerSimpleBlockItem("coal_generator", ModBlocks.COAL_GENERATOR);
    public static final DeferredItem<BlockItem> EXHAUST_CHIMNEY_ITEM = ITEMS.registerSimpleBlockItem("exhaust_chimney", ModBlocks.EXHAUST_CHIMNEY);
    public static final DeferredItem<BlockItem> ELECTRIC_CABLE_ITEM = ITEMS.registerSimpleBlockItem("electric_cable", ModBlocks.ELECTRIC_CABLE);
    public static final DeferredItem<BlockItem> UNDERGROUND_ELECTRIC_CABLE_ITEM = ITEMS.registerSimpleBlockItem("underground_electric_cable", ModBlocks.UNDERGROUND_ELECTRIC_CABLE);
}
