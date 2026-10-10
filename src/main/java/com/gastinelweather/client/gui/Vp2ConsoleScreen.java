package com.gastinelweather.client.gui;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.integration.MinecraftHeightSampler;
import com.gastinelweather.weather.HeightSampler;
import com.gastinelweather.weather.SynopticState;
import com.gastinelweather.weather.WeatherSample;
import com.gastinelweather.weather.WeatherSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.joml.Vector3f;

import java.util.List;

public class Vp2ConsoleScreen extends Screen {

    private final BlockPos pos;
    private boolean show24hForecast = false;

    private static final int GUI_WIDTH = 290;
    private static final int GUI_HEIGHT = 210;

    public Vp2ConsoleScreen(BlockPos pos) {
        super(Component.literal("Console Vantage Pro 2"));
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();
        int leftPos = (this.width - GUI_WIDTH) / 2;
        int topPos = (this.height - GUI_HEIGHT) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("Actualiser"), b -> {})
                .bounds(leftPos + 15, topPos + 180, 75, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.literal(show24hForecast ? "En Direct" : "Prévisions 24h"), b -> {
                    this.show24hForecast = !this.show24hForecast;
                    b.setMessage(Component.literal(show24hForecast ? "En Direct" : "Prévisions 24h"));
                })
                .bounds(leftPos + 95, topPos + 180, 95, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.literal("Fermer"), b -> this.onClose())
                .bounds(leftPos + 195, topPos + 180, 80, 20)
                .build());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        int leftPos = (this.width - GUI_WIDTH) / 2;
        int topPos = (this.height - GUI_HEIGHT) / 2;

        // 1. Fond Boîtier Console (Gris Ardoise Foncé avec double bordure)
        guiGraphics.fill(leftPos, topPos, leftPos + GUI_WIDTH, topPos + GUI_HEIGHT, 0xFF1B232A);
        guiGraphics.renderOutline(leftPos, topPos, GUI_WIDTH, GUI_HEIGHT, 0xFF34495E);
        guiGraphics.renderOutline(leftPos + 2, topPos + 2, GUI_WIDTH - 4, GUI_HEIGHT - 4, 0xFF0D1217);

        // 2. Écran LCD Rétroéclairé (Vert Matrix / LCD rétroéclairé)
        int lcdX = leftPos + 10;
        int lcdY = topPos + 10;
        int lcdW = GUI_WIDTH - 20;
        int lcdH = 162;
        guiGraphics.fill(lcdX, lcdY, lcdX + lcdW, lcdY + lcdH, 0xFF0A2417);
        guiGraphics.renderOutline(lcdX, lcdY, lcdW, lcdH, 0xFF1E824C);

        // 3. Récupération des données météo en direct
        WeatherSystem system = GastinelWeatherMod.getWeatherSystem();
        float gameHours = mc.level.getDayTime() / 1000.0f;
        Vector3f vPos = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
        HeightSampler terrain = new MinecraftHeightSampler(mc.level);

        SynopticState sky = system.synopticAt(gameHours);
        WeatherSample sample = system.sampleAt(vPos, sky, terrain);

        // 4. Titre et Statut Radio
        guiGraphics.drawString(this.font, "DAVIS VANTAGE PRO 2 — CONSOLE", lcdX + 8, lcdY + 6, 0xFF2ECC71, false);
        guiGraphics.drawString(this.font, "● DIRECT (868MHz)", lcdX + lcdW - 105, lcdY + 6, 0xFF00FF7F, false);
        guiGraphics.hLine(lcdX + 4, lcdX + lcdW - 5, lcdY + 16, 0xFF1E824C);

        if (!show24hForecast) {
            // --- VUE 1 : TABLEAU DE BORD EN DIRECT ---

            // CADRAN 1 : TEMPÉRATURE & RESSENTI
            int box1X = lcdX + 8;
            int box1Y = lcdY + 22;
            guiGraphics.fill(box1X, box1Y, box1X + 124, box1Y + 60, 0xFF0F3823);
            guiGraphics.renderOutline(box1X, box1Y, 124, 60, 0xFF27AE60);
            guiGraphics.drawString(this.font, "TEMPÉRATURE EXT.", box1X + 4, box1Y + 4, 0xFF82E0AA, false);
            guiGraphics.drawString(this.font, String.format("§a§l%.1f°C", sample.temperatureC), box1X + 6, box1Y + 16, 0xFF2ECC71, false);
            guiGraphics.drawString(this.font, String.format("§7Ressenti: §e%.1f°C", sample.windChillC), box1X + 4, box1Y + 34, 0xFFF1C40F, false);
            guiGraphics.drawString(this.font, String.format("§7INT: §f21.5°C §7(48%%)", sample.temperatureC), box1X + 4, box1Y + 46, 0xFFBDC3C7, false);

            // CADRAN 2 : VENT & RAFALES
            int box2X = lcdX + 138;
            int box2Y = lcdY + 22;
            guiGraphics.fill(box2X, box2Y, box2X + 124, box2Y + 60, 0xFF0F3823);
            guiGraphics.renderOutline(box2X, box2Y, 124, 60, 0xFF27AE60);
            guiGraphics.drawString(this.font, "VENT & DIRECTION", box2X + 4, box2Y + 4, 0xFF82E0AA, false);
            guiGraphics.drawString(this.font, String.format("§b§l%.1f km/h", sample.getWindSpeedKmh()), box2X + 6, box2Y + 16, 0xFF3498DB, false);
            guiGraphics.drawString(this.font, String.format("§7Dir: §e%.0f° (%s)", sky.windFromDegrees, getCompassName(sky.windFromDegrees)), box2X + 4, box2Y + 34, 0xFFF1C40F, false);
            guiGraphics.drawString(this.font, String.format("§7Rafales: §c%.1f km/h", sample.getWindSpeedKmh() * 1.35f), box2X + 4, box2Y + 46, 0xFFE74C3C, false);

            // CADRAN 3 : BAROMÈTRE & TENDANCE
            int box3X = lcdX + 8;
            int box3Y = lcdY + 86;
            guiGraphics.fill(box3X, box3Y, box3X + 124, box3Y + 45, 0xFF0F3823);
            guiGraphics.renderOutline(box3X, box3Y, 124, 45, 0xFF27AE60);
            guiGraphics.drawString(this.font, "BAROMÈTRE", box3X + 4, box3Y + 4, 0xFF82E0AA, false);
            guiGraphics.drawString(this.font, String.format("§f1013.2 hPa %s", sky.cloudCover > 0.6f ? "§c↘ Baisse" : "§a↗ Hausse"), box3X + 4, box3Y + 16, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("§7Nuages: §f%.0f%% §7| Sol: §a%.0f%%", sky.cloudCover * 100.0f, sample.wetness * 100.0f), box3X + 4, box3Y + 28, 0xFFBDC3C7, false);

            // CADRAN 4 : PLUIE & SOLAIRE
            int box4X = lcdX + 138;
            int box4Y = lcdY + 86;
            guiGraphics.fill(box4X, box4Y, box4X + 124, box4Y + 45, 0xFF0F3823);
            guiGraphics.renderOutline(box4X, box4Y, 124, 45, 0xFF27AE60);
            guiGraphics.drawString(this.font, "PLUIE & RAYONNEMENT", box4X + 4, box4Y + 4, 0xFF82E0AA, false);
            guiGraphics.drawString(this.font, String.format("§3Intensité: §b%.1f mm/h", sample.precipitationMmPerHour), box4X + 4, box4Y + 16, 0xFF00CED1, false);
            guiGraphics.drawString(this.font, String.format("§6Solaire: §f%.0f W/m² §7(UV: §e%.1f§7)", sky.daylight * 850.0f, sky.daylight * 9.5f), box4X + 4, box4Y + 28, 0xFFF39C12, false);

            // BANDEAU INFÉRIEUR : PRÉVISION IMMÉDIATE
            int stripY = lcdY + 137;
            guiGraphics.fill(lcdX + 8, stripY, lcdX + lcdW - 8, stripY + 20, 0xFF0A1F13);
            guiGraphics.renderOutline(lcdX + 8, stripY, lcdW - 16, 20, 0xFF1E824C);
            String forecastText = (sky.cloudCover > 0.72f) ? "§9Tendance: Pluie soutenue et couverture nuageuse dense." :
                                  (sky.cloudCover > 0.35f) ? "§eTendance: Temps variable avec passages nuageux." : "§aTendance: Conditions anticycloniques stables et ciel clair.";
            guiGraphics.drawString(this.font, forecastText, lcdX + 14, stripY + 6, 0xFF82E0AA, false);

        } else {
            // --- VUE 2 : PRÉVISIONS DÉTERMINISTES HEURE PAR HEURE (24H) ---
            List<WeatherSystem.HourlyForecast> forecasts = system.predictHourlyForecast(gameHours, 24, vPos, terrain);

            guiGraphics.drawString(this.font, "PRÉVISIONS DÉTERMINISTES SUR 24 HEURES", lcdX + 8, lcdY + 22, 0xFFF1C40F, false);

            int startY = lcdY + 36;
            int count = 0;
            for (int i = 2; i <= 24; i += 3) {
                if (i < forecasts.size() && count < 7) {
                    WeatherSystem.HourlyForecast f = forecasts.get(i);
                    int rowY = startY + count * 16;
                    guiGraphics.fill(lcdX + 6, rowY, lcdX + lcdW - 6, rowY + 14, (count % 2 == 0) ? 0xFF0D2D1C : 0xFF082014);

                    String icon = f.willSnow() ? "§b[NEIGE]" : (f.willRain() ? "§9[PLUIE]" : (f.cloudPercent() > 50 ? "§7[NUAGE]" : "§e[SOLEIL]"));
                    String rowText = String.format("§f+%02dh §7(%02dh00) : %s §e%.1f°C §7| Vent §b%02.0f km/h §7| Nuages §f%02.0f%%",
                            (int)(f.gameHours() - gameHours), (int)f.timeOfDayHours(), icon, f.temperatureC(), f.windKmh(), f.cloudPercent());
                    guiGraphics.drawString(this.font, rowText, lcdX + 10, rowY + 3, 0xFF82E0AA, false);
                    count++;
                }
            }
        }
    }

    private static String getCompassName(float deg) {
        if (deg >= 337.5 || deg < 22.5) return "N";
        if (deg < 67.5) return "NE";
        if (deg < 112.5) return "E";
        if (deg < 157.5) return "SE";
        if (deg < 202.5) return "S";
        if (deg < 247.5) return "SO";
        if (deg < 292.5) return "O";
        return "NO";
    }
}
