package com.gastinelweather.command;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.integration.MinecraftHeightSampler;
import com.gastinelweather.weather.HeightSampler;
import com.gastinelweather.weather.SynopticState;
import com.gastinelweather.weather.WeatherSample;
import com.gastinelweather.weather.WeatherSystem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.joml.Vector3f;

import java.util.List;

public class GastinelWeatherCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
            Commands.literal("gweather")
                .requires(source -> source.hasPermission(0))
                .executes(GastinelWeatherCommand::showCurrentWeather)
                .then(Commands.literal("now")
                    .executes(GastinelWeatherCommand::showCurrentWeather))
                .then(Commands.literal("current")
                    .executes(GastinelWeatherCommand::showCurrentWeather))
                .then(Commands.literal("forecast")
                    .executes(ctx -> showForecast(ctx, 12))
                    .then(Commands.argument("heures", IntegerArgumentType.integer(1, 72))
                        .executes(ctx -> showForecast(ctx, IntegerArgumentType.getInteger(ctx, "heures")))))
                .then(Commands.literal("daily")
                    .executes(ctx -> showDailyForecast(ctx, 7))
                    .then(Commands.argument("jours", IntegerArgumentType.integer(1, 14))
                        .executes(ctx -> showDailyForecast(ctx, IntegerArgumentType.getInteger(ctx, "jours")))))
        );

        // Alias /gastinelweather
        dispatcher.register(
            Commands.literal("gastinelweather")
                .requires(source -> source.hasPermission(0))
                .executes(GastinelWeatherCommand::showCurrentWeather)
                .then(Commands.literal("forecast")
                    .executes(ctx -> showForecast(ctx, 12))
                    .then(Commands.argument("heures", IntegerArgumentType.integer(1, 72))
                        .executes(ctx -> showForecast(ctx, IntegerArgumentType.getInteger(ctx, "heures")))))
                .then(Commands.literal("daily")
                    .executes(ctx -> showDailyForecast(ctx, 7))
                    .then(Commands.argument("jours", IntegerArgumentType.integer(1, 14))
                        .executes(ctx -> showDailyForecast(ctx, IntegerArgumentType.getInteger(ctx, "jours")))))
        );
    }

    private static float getCurrentGameHours(CommandSourceStack source) {
        long dayTime = source.getLevel().getDayTime();
        // 24000 ticks = 24 heures de jeu -> 1000 ticks = 1 heure de jeu
        return dayTime / 1000.0f;
    }

    private static int showCurrentWeather(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        WeatherSystem system = GastinelWeatherMod.getWeatherSystem();

        Vec3 playerPos = source.getPosition();
        Vector3f pos = new Vector3f((float) playerPos.x, (float) playerPos.y, (float) playerPos.z);
        HeightSampler terrain = new MinecraftHeightSampler(source.getLevel());

        float gameHours = getCurrentGameHours(source);
        SynopticState sky = system.synopticAt(gameHours);
        WeatherSample sample = system.sampleAt(pos, sky, terrain);

        source.sendSuccess(() -> Component.literal("§8=== §6§lMétéo Gastinel §7(Temps Réel & Microclimat) §8==="), false);
        source.sendSuccess(() -> Component.literal(
            "§7Température : §e" + String.format("%.1f°C", sample.temperatureC) +
            " §7(Ressenti au vent : §b" + String.format("%.1f°C", sample.windChillC) + "§7)"
        ), false);

        source.sendSuccess(() -> Component.literal(
            "§7Effets de relief : Gradient alt. §c" + String.format("%.1f°C", sample.lapseC) +
            "§7, Ensoleillement versant §6" + String.format("%+.1f°C", sample.aspectC)
        ), false);

        source.sendSuccess(() -> Component.literal(
            "§7Vent : §b" + String.format("%.1f km/h", sample.getWindSpeedKmh()) +
            " §7(§f" + String.format("%.1f m/s", sample.windMps) + "§7) de §e" + String.format("%.0f°", sky.windFromDegrees)
        ), false);

        source.sendSuccess(() -> Component.literal(
            "§7Courants d'air : Thermiques §a" + String.format("%+.2f m/s", sample.thermalMps) +
            "§7, Pente §e" + String.format("%+.2f m/s", sample.ridgeLiftMps) +
            "§7, Total vertical §b" + String.format("%+.2f m/s", sample.verticalMps)
        ), false);

        final String finalCond = getConditionDescription(sample);
        source.sendSuccess(() -> Component.literal("§7Conditions : " + finalCond + " §7| Humidité sol : §f" + String.format("%.0f%%", sample.wetness * 100.0f)), false);
        source.sendSuccess(() -> Component.literal("§8------------------------------------------------"), false);
        source.sendSuccess(() -> Component.literal("§e/gweather forecast [heures] §7: Prévisions horaires"), false);
        source.sendSuccess(() -> Component.literal("§e/gweather daily [jours] §7: Bulletin prévisionnel sur 7 jours"), false);

        return 1;
    }

    private static String getConditionDescription(WeatherSample sample) {
        if (sample.snowing) return "§bNeige soutenue (" + String.format("%.1f", sample.precipitationMmPerHour) + " mm/h)";
        if (sample.precipitationMmPerHour > 2.0f) return "§9Forte pluie (" + String.format("%.1f", sample.precipitationMmPerHour) + " mm/h)";
        if (sample.precipitationMmPerHour > 0.05f) return "§3Pluie fine (" + String.format("%.1f", sample.precipitationMmPerHour) + " mm/h)";
        if (sample.cloudCover > 0.7f) return "§7Ciel très couvert (" + String.format("%.0f%%", sample.cloudCover * 100.0f) + ")";
        if (sample.cloudCover > 0.3f) return "§eÉclaircies (" + String.format("%.0f%%", sample.cloudCover * 100.0f) + ")";
        return "§aBeau temps dégagé";
    }

    private static int showForecast(CommandContext<CommandSourceStack> context, int hoursAhead) {
        CommandSourceStack source = context.getSource();
        WeatherSystem system = GastinelWeatherMod.getWeatherSystem();

        Vec3 playerPos = source.getPosition();
        Vector3f pos = new Vector3f((float) playerPos.x, (float) playerPos.y, (float) playerPos.z);
        HeightSampler terrain = new MinecraftHeightSampler(source.getLevel());

        float currentHours = getCurrentGameHours(source);
        List<WeatherSystem.HourlyForecast> forecasts = system.predictHourlyForecast(currentHours, hoursAhead, pos, terrain);

        source.sendSuccess(() -> Component.literal("§8=== §6§lPrévisions Météo Déterministes (§e+" + hoursAhead + "h§6§l) §8==="), false);

        for (int i = 1; i < forecasts.size(); i += Math.max(1, hoursAhead / 8)) {
            WeatherSystem.HourlyForecast f = forecasts.get(i);
            int hourInt = (int) f.timeOfDayHours();
            String icon = "☀️";
            String color = "§e";
            if (f.willSnow()) { icon = "❄️"; color = "§b"; }
            else if (f.willRain()) { icon = "🌧️"; color = "§9"; }
            else if (f.cloudPercent() > 60.0f) { icon = "☁️"; color = "§7"; }

            String line = String.format(
                "§8[§f+%02dh§8] §e%02dh00 §7: %s %s%.1f°C §7| Vent §b%.0f km/h §7| Nuages §f%.0f%% %s",
                (int)(f.gameHours() - currentHours), hourInt, icon, color, f.temperatureC(), f.windKmh(), f.cloudPercent(),
                (f.precipitationMm() > 0.05f ? "§9(" + String.format("%.1fmm", f.precipitationMm()) + ")" : "")
            );
            source.sendSuccess(() -> Component.literal(line), false);
        }
        return 1;
    }

    private static int showDailyForecast(CommandContext<CommandSourceStack> context, int daysAhead) {
        CommandSourceStack source = context.getSource();
        WeatherSystem system = GastinelWeatherMod.getWeatherSystem();

        Vec3 playerPos = source.getPosition();
        Vector3f pos = new Vector3f((float) playerPos.x, (float) playerPos.y, (float) playerPos.z);
        HeightSampler terrain = new MinecraftHeightSampler(source.getLevel());

        float currentHours = getCurrentGameHours(source);
        List<WeatherSystem.DailyForecast> daily = system.predictDailyForecast(currentHours, daysAhead, pos, terrain);

        source.sendSuccess(() -> Component.literal("§8=== §6§lBulletin Météo des " + daysAhead + " Prochains Jours §8==="), false);

        for (WeatherSystem.DailyForecast d : daily) {
            String icon = "☀️ Ensoleillé";
            if (d.hasSnow()) icon = "❄️ Neige";
            else if (d.hasRain()) icon = "🌧️ Pluie (" + String.format("%.1f", d.totalRainMm()) + "mm)";
            else if (d.maxCloudPercent() > 60.0f) icon = "☁️ Nuageux";

            String line = String.format(
                "§6Jour %d §7: %s §7| Min: §b%.1f°C §7- Max: §e%.1f°C §7| Rafales: §f%.0f km/h",
                d.dayNumber(), icon, d.minTempC(), d.maxTempC(), d.maxWindKmh()
            );
            source.sendSuccess(() -> Component.literal(line), false);
        }
        return 1;
    }
}
