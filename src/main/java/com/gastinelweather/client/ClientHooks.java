package com.gastinelweather.client;

import com.gastinelweather.client.gui.Vp2ConsoleScreen;
import com.gastinelweather.client.gui.WeatherMonitorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class ClientHooks {

    public static void openVp2ConsoleScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new Vp2ConsoleScreen(pos));
    }

    public static void openMonitorScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new WeatherMonitorScreen(pos));
    }
}
