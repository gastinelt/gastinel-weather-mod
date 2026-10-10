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

public class WeatherMonitorScreen extends Screen {

    private final BlockPos pos;
    private int activeTab = 0; // 0 = Relevés & Relief, 1 = Bulletin 7 Jours

    private static final int GUI_WIDTH = 320;
    private static final int GUI_HEIGHT = 220;

    public WeatherMonitorScreen(BlockPos pos) {
        super(Component.literal("Moniteur Météo"));
        this.pos = pos;
    }

    @Override
    protected void init() {
        super.init();
        int leftPos = (this.width - GUI_WIDTH) / 2;
        int topPos = (this.height - GUI_HEIGHT) / 2;

        this.addRenderableWidget(Button.builder(Component.literal("Télémétrie & Microclimat"), b -> this.activeTab = 0)
                .bounds(leftPos + 10, topPos + 190, 130, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.literal("Bulletin 7 Jours"), b -> this.activeTab = 1)
                .bounds(leftPos + 145, topPos + 190, 100, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.literal("Quitter"), b -> this.onClose())
                .bounds(leftPos + 250, topPos + 190, 60, 20)
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

        // 1. Châssis Moniteur Haute Technologie (Bleu Nuit Foncé / Cyan)
        guiGraphics.fill(leftPos, topPos, leftPos + GUI_WIDTH, topPos + GUI_HEIGHT, 0xFF0D1B2A);
        guiGraphics.renderOutline(leftPos, topPos, GUI_WIDTH, GUI_HEIGHT, 0xFF1B4965);
        guiGraphics.renderOutline(leftPos + 2, topPos + 2, GUI_WIDTH - 4, GUI_HEIGHT - 4, 0xFF62B6CB);

        // 2. Zone d'affichage logiciel
        int screenX = leftPos + 8;
        int screenY = topPos + 8;
        int screenW = GUI_WIDTH - 16;
        int screenH = 175;
        guiGraphics.fill(screenX, screenY, screenX + screenW, screenY + screenH, 0xFF08121E);
        guiGraphics.renderOutline(screenX, screenY, screenW, screenH, 0xFF1F4E79);

        // 3. Barre de titre logiciel
        guiGraphics.fill(screenX, screenY, screenX + screenW, screenY + 16, 0xFF1B4965);
        guiGraphics.drawString(this.font, "GASTINEL METEO SOFTWARE v2.4 — STATION CENTRALE", screenX + 6, screenY + 4, 0xFFE0FAFF, false);
        guiGraphics.drawString(this.font, "STATION CONNECTÉE [OK]", screenX + screenW - 130, screenY + 4, 0xFF2ECC71, false);

        WeatherSystem system = GastinelWeatherMod.getWeatherSystem();
        float gameHours = mc.level.getDayTime() / 1000.0f;
        Vector3f vPos = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
        HeightSampler terrain = new MinecraftHeightSampler(mc.level);

        SynopticState sky = system.synopticAt(gameHours);
        WeatherSample sample = system.sampleAt(vPos, sky, terrain);

        if (activeTab == 0) {
            // --- ONGLET 0 : TÉLÉMÉTRIE MICROCLIMATIQUE ET TOPOGRAPHIQUE ---

            // Bloc Gauche : Données thermiques & Relief
            int col1X = screenX + 8;
            int col1Y = screenY + 24;
            guiGraphics.fill(col1X, col1Y, col1X + 140, col1Y + 70, 0xFF0F2537);
            guiGraphics.renderOutline(col1X, col1Y, 140, 70, 0xFF2A6F97);
            guiGraphics.drawString(this.font, "THERMODYNAMIQUE", col1X + 4, col1Y + 4, 0xFF62B6CB, false);
            guiGraphics.drawString(this.font, String.format("Temp. Ambiante: §e%.1f°C", sample.temperatureC), col1X + 6, col1Y + 16, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Ressenti au Vent: §b%.1f°C", sample.windChillC), col1X + 6, col1Y + 28, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Gradient Alt (Y=%d): §c%.1f°C", pos.getY(), sample.lapseC), col1X + 6, col1Y + 40, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Gain Ensoleillement: §6%+.1f°C", sample.aspectC), col1X + 6, col1Y + 52, 0xFFFFFFFF, false);

            // Bloc Droite : Aérodynamique & Énergie
            int col2X = screenX + 154;
            int col2Y = screenY + 24;
            guiGraphics.fill(col2X, col2Y, col2X + 140, col2Y + 70, 0xFF0F2537);
            guiGraphics.renderOutline(col2X, col2Y, 140, 70, 0xFF2A6F97);
            guiGraphics.drawString(this.font, "DYNAMIQUE DU VENT", col2X + 4, col2Y + 4, 0xFF62B6CB, false);
            guiGraphics.drawString(this.font, String.format("Vitesse Horizontale: §b%.1f km/h", sample.getWindSpeedKmh()), col2X + 6, col2Y + 16, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Courant Ascendant: §a%+.2f m/s", sample.verticalMps), col2X + 6, col2Y + 28, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Thermiques Pures: §6%+.2f m/s", sample.thermalMps), col2X + 6, col2Y + 40, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Turbulences / Sillage: §c%.0f%%", sample.turbulence * 100.0f), col2X + 6, col2Y + 52, 0xFFFFFFFF, false);

            // Grand Bloc Inférieur : Écoulement et Précipitations
            int botY = screenY + 100;
            guiGraphics.fill(screenX + 8, botY, screenX + screenW - 8, botY + 68, 0xFF0F2537);
            guiGraphics.renderOutline(screenX + 8, botY, screenW - 16, 68, 0xFF2A6F97);
            guiGraphics.drawString(this.font, "PRÉCIPITATIONS ET ÉTAT DU SOL", screenX + 14, botY + 4, 0xFF62B6CB, false);

            String statusPluie = sample.snowing ? "§bChute de neige en cours" : (sample.precipitationMmPerHour > 0.05f ? "§9Pluie active" : "§aAucune précipitation");
            guiGraphics.drawString(this.font, String.format("Régime: %s §7(Taux: §f%.2f mm/h§7)", statusPluie, sample.precipitationMmPerHour), screenX + 14, botY + 18, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Saturation du Sol (Mouillure): §a%.0f%% §7| Nébulosité: §f%.0f%%", sample.wetness * 100.0f, sky.cloudCover * 100.0f), screenX + 14, botY + 32, 0xFFFFFFFF, false);
            guiGraphics.drawString(this.font, String.format("Position Solaire: Azimut §e%.0f° §7| Élévation §e%.1f° §7| Flux: §6%.0f W/m²", sky.sunAzimuthDegrees, sky.sunElevationDegrees, sky.daylight * 850.0f), screenX + 14, botY + 46, 0xFFFFFFFF, false);

        } else {
            // --- ONGLET 1 : BULLETIN PRÉVISIONNEL 7 JOURS ---
            List<WeatherSystem.DailyForecast> daily = system.predictDailyForecast(gameHours, 7, vPos, terrain);

            guiGraphics.drawString(this.font, "PRÉVISIONS MÉTÉOROLOGIQUES SUR 7 JOURS (CALCUL PUR DÉTERMINISTE)", screenX + 8, screenY + 22, 0xFF62B6CB, false);

            int startY = screenY + 36;
            for (int i = 0; i < daily.size(); i++) {
                WeatherSystem.DailyForecast d = daily.get(i);
                int rowY = startY + i * 19;
                guiGraphics.fill(screenX + 8, rowY, screenX + screenW - 8, rowY + 17, (i % 2 == 0) ? 0xFF0D2539 : 0xFF091C2C);
                guiGraphics.renderOutline(screenX + 8, rowY, screenW - 16, 17, 0xFF1F4E79);

                String icon = d.hasSnow() ? "§b❄ Neige" : (d.hasRain() ? "§9🌧 Pluie (" + String.format("%.1f", d.totalRainMm()) + "mm)" : (d.maxCloudPercent() > 50 ? "§7☁ Nuageux" : "§e☀ Ensoleillé"));
                String line = String.format("§6Jour %d §7: %-16s §7| T°: §b%.1f°C §7à §e%.1f°C §7| Rafales: §f%.0f km/h",
                        d.dayNumber(), icon, d.minTempC(), d.maxTempC(), d.maxWindKmh());
                guiGraphics.drawString(this.font, line, screenX + 12, rowY + 4, 0xFFE0FAFF, false);
            }
        }
    }
}
