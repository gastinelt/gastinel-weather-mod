package com.gastinelweather.tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Générateur de textures pixel-art 16x16 et 32x32 pour tous les blocs et items de GastinelWeatherMod.
 */
public class TextureGenerator {

    public static void main(String[] args) {
        String baseDir = "C:/Users/theog/Documents/_CODE/2-minecraft/mod-meteo/src/main/resources/assets/gastinelweather/textures";
        File blockDir = new File(baseDir, "block");
        File itemDir = new File(baseDir, "item");
        blockDir.mkdirs();
        itemDir.mkdirs();

        // 1. Textures de Blocs
        generateVp2Station(new File(blockDir, "vp2_station.png"));
        generateAnemometer(new File(blockDir, "anemometer.png"));
        generateUltrasonicAnemometer(new File(blockDir, "ultrasonic_anemometer.png"));
        generateRainGauge(new File(blockDir, "rain_gauge.png"));
        generateSnowGauge(new File(blockDir, "snow_gauge.png"));
        generateEsp32(new File(blockDir, "esp32_board.png"));
        generateAntenna(new File(blockDir, "directional_antenna.png"));
        generateOmniAntenna(new File(blockDir, "omni_antenna.png"));
        generateVp2Console(new File(blockDir, "vp2_console.png"));
        generatePcCase(new File(blockDir, "pc_case.png"));
        generateMonitor(new File(blockDir, "monitor.png"));
        generateRouter(new File(blockDir, "network_router.png"));
        generateWindTurbine(new File(blockDir, "wind_turbine.png"));
        generateHydroTurbine(new File(blockDir, "hydro_turbine.png"));
        generateCoalGenerator(new File(blockDir, "coal_generator.png"));
        generateExhaustChimney(new File(blockDir, "exhaust_chimney.png"));

        generateCable(new File(blockDir, "network_cable.png"), new Color(0x2B78E4));
        generateCable(new File(blockDir, "outdoor_network_cable.png"), new Color(0x1F232A));
        generateCable(new File(blockDir, "underground_network_cable.png"), new Color(0x3B444B));
        generateCable(new File(blockDir, "electric_cable.png"), new Color(0xD63031));
        generateCable(new File(blockDir, "underground_electric_cable.png"), new Color(0x636E72));

        // 2. Textures d'Items
        generateElitechItem(new File(itemDir, "elitech_logger.png"));
        generateMotherboardItem(new File(itemDir, "motherboard.png"));
        generateCpuItem(new File(itemDir, "cpu.png"));
        generateCpuCoolerItem(new File(itemDir, "cpu_cooler.png"));
        generateGpuItem(new File(itemDir, "gpu.png"));
        generatePsuItem(new File(itemDir, "power_supply.png"));
        generatePowerCableItem(new File(itemDir, "power_cable.png"));
        generateTransistorItem(new File(itemDir, "transistor.png"));

        System.out.println("Toutes les textures ont été générées avec succès !");
    }

    private static void saveImage(BufferedImage img, File file) {
        try {
            ImageIO.write(img, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateVp2Station(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0xDCE4EC));
        g.fillRect(2, 2, 12, 12);
        g.setColor(new Color(0x2C3E50));
        g.fillRect(3, 4, 10, 2);
        g.fillRect(3, 7, 10, 2);
        g.fillRect(3, 10, 10, 2);
        // Panneau solaire bleu
        g.setColor(new Color(0x1A5276));
        g.fillRect(5, 2, 6, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateAnemometer(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x5D6D7E));
        g.fillRect(7, 2, 2, 12);
        g.setColor(new Color(0x34495E));
        g.fillRect(3, 3, 10, 2);
        // Coupelles
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(2, 2, 2, 3);
        g.fillRect(12, 2, 2, 3);
        g.fillRect(7, 1, 3, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateUltrasonicAnemometer(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2C3E50));
        g.fillRect(4, 10, 8, 5);
        g.setColor(new Color(0x95A5A6));
        g.fillRect(4, 2, 2, 8);
        g.fillRect(10, 2, 2, 8);
        g.setColor(new Color(0x1ABC9C));
        g.fillRect(5, 1, 6, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateRainGauge(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x7F8C8D));
        g.fillRect(4, 2, 8, 12);
        g.setColor(new Color(0x3498DB));
        g.fillRect(6, 6, 4, 7);
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(3, 1, 10, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateSnowGauge(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x95A5A6));
        g.fillRect(7, 0, 2, 16);
        g.fillRect(4, 2, 4, 2);
        // Laser rouge
        g.setColor(new Color(0xE74C3C));
        g.fillRect(4, 4, 2, 1);
        g.setColor(new Color(0xECF0F1));
        g.fillRect(2, 14, 12, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateEsp32(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x1E272C));
        g.fillRect(2, 2, 12, 12);
        // Puce blindée argentée
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(4, 4, 8, 6);
        // Pins dorés
        g.setColor(new Color(0xF39C12));
        g.fillRect(2, 2, 1, 12);
        g.fillRect(13, 2, 1, 12);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateAntenna(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x7F8C8D));
        g.fillRect(7, 2, 2, 12);
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(2, 3, 12, 2);
        g.fillRect(3, 6, 10, 2);
        g.fillRect(4, 9, 8, 2);
        g.fillRect(5, 12, 6, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateOmniAntenna(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2C3E50));
        g.fillRect(6, 12, 4, 4);
        g.setColor(new Color(0xE67E22));
        g.fillRect(7, 10, 2, 2);
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(7, 0, 2, 10);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateVp2Console(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x34495E));
        g.fillRect(1, 1, 14, 14);
        // Écran LCD rétroéclairé vert/bleu
        g.setColor(new Color(0x27AE60));
        g.fillRect(3, 3, 10, 7);
        // Segments LCD
        g.setColor(new Color(0x145A32));
        g.fillRect(4, 4, 3, 2);
        g.fillRect(8, 4, 4, 2);
        g.fillRect(4, 7, 8, 2);
        // Boutons
        g.setColor(new Color(0x95A5A6));
        g.fillRect(3, 12, 2, 1);
        g.fillRect(6, 12, 2, 1);
        g.fillRect(9, 12, 2, 1);
        g.fillRect(12, 12, 1, 1);
        g.dispose();
        saveImage(img, file);
    }

    private static void generatePcCase(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x1B1F23));
        g.fillRect(1, 1, 14, 14);
        // Vitre latérale
        g.setColor(new Color(0x24292E));
        g.fillRect(3, 3, 10, 10);
        // RGB & GPU interne
        g.setColor(new Color(0x00D2D3));
        g.fillRect(4, 6, 8, 1);
        g.setColor(new Color(0xFF9F43));
        g.fillRect(4, 9, 8, 1);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateMonitor(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2D3436));
        g.fillRect(1, 1, 14, 11);
        // Écran météo
        g.setColor(new Color(0x0984E3));
        g.fillRect(2, 2, 12, 9);
        // Graphique température
        g.setColor(new Color(0xFDCB6E));
        g.drawLine(3, 8, 6, 5);
        g.drawLine(6, 5, 9, 6);
        g.drawLine(9, 6, 13, 4);
        // Pied
        g.setColor(new Color(0x636E72));
        g.fillRect(7, 12, 2, 2);
        g.fillRect(5, 14, 6, 1);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateRouter(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2C3E50));
        g.fillRect(2, 6, 12, 8);
        // Antennes
        g.setColor(new Color(0x1A252F));
        g.fillRect(3, 1, 1, 5);
        g.fillRect(12, 1, 1, 5);
        // LEDs
        g.setColor(new Color(0x2ECC71));
        g.fillRect(4, 11, 1, 1);
        g.fillRect(6, 11, 1, 1);
        g.fillRect(8, 11, 1, 1);
        g.fillRect(10, 11, 1, 1);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateWindTurbine(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(7, 0, 2, 16);
        // Nacelle
        g.setColor(new Color(0x7F8C8D));
        g.fillRect(5, 4, 6, 4);
        // Pales
        g.setColor(new Color(0xECF0F1));
        g.drawLine(8, 6, 8, 0);
        g.drawLine(8, 6, 3, 10);
        g.drawLine(8, 6, 13, 10);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateHydroTurbine(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x16A085));
        g.fillRect(2, 2, 12, 12);
        g.setColor(new Color(0xD35400));
        g.fillOval(4, 4, 8, 8);
        g.setColor(new Color(0x2C3E50));
        g.fillRect(7, 3, 2, 10);
        g.fillRect(3, 7, 10, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateCoalGenerator(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2C3E50));
        g.fillRect(1, 1, 14, 14);
        // Foyer braises
        g.setColor(new Color(0xD35400));
        g.fillRect(4, 8, 8, 5);
        g.setColor(new Color(0xF39C12));
        g.fillRect(5, 9, 6, 3);
        // Manomètre
        g.setColor(new Color(0xBDC3C7));
        g.fillOval(6, 3, 4, 4);
        g.setColor(new Color(0xC0392B));
        g.drawLine(8, 5, 9, 4);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateExhaustChimney(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x7F8C8D));
        g.fillRect(2, 0, 12, 16);
        g.setColor(new Color(0x2C3E50));
        g.fillRect(1, 0, 14, 2);
        g.fillRect(1, 14, 14, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateCable(File file, Color color) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(5, 5, 6, 6);
        g.setColor(color.darker());
        g.drawRect(5, 5, 5, 5);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateElitechItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0xE67E22));
        g.fillRect(3, 2, 10, 12);
        // Écran
        g.setColor(new Color(0x2ECC71));
        g.fillRect(5, 4, 6, 4);
        g.setColor(new Color(0x2C3E50));
        g.fillRect(5, 10, 2, 2);
        g.fillRect(9, 10, 2, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateMotherboardItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x1B4F72));
        g.fillRect(1, 1, 14, 14);
        g.setColor(new Color(0xF1C40F));
        g.fillRect(4, 4, 5, 5);
        g.setColor(new Color(0x27AE60));
        g.fillRect(10, 3, 3, 10);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateCpuItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2C3E50));
        g.fillRect(3, 3, 10, 10);
        g.setColor(new Color(0xF1C40F));
        g.fillRect(5, 5, 6, 6);
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(6, 6, 4, 4);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateCpuCoolerItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(3, 3, 10, 10);
        g.setColor(new Color(0x34495E));
        g.fillOval(5, 5, 6, 6);
        g.setColor(new Color(0x3498DB));
        g.fillRect(7, 4, 2, 8);
        g.fillRect(4, 7, 8, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateGpuItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2C3E50));
        g.fillRect(1, 4, 14, 8);
        g.setColor(new Color(0x1B1F23));
        g.fillOval(3, 5, 4, 4);
        g.fillOval(9, 5, 4, 4);
        g.setColor(new Color(0xF1C40F));
        g.fillRect(3, 12, 10, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generatePsuItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x2C3E50));
        g.fillRect(2, 2, 12, 12);
        g.setColor(new Color(0x1A252F));
        g.fillOval(4, 4, 8, 8);
        g.setColor(new Color(0xE74C3C));
        g.fillRect(12, 5, 2, 4);
        g.dispose();
        saveImage(img, file);
    }

    private static void generatePowerCableItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x1B1F23));
        g.fillRect(2, 6, 12, 4);
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(1, 7, 2, 2);
        g.fillRect(13, 7, 2, 2);
        g.dispose();
        saveImage(img, file);
    }

    private static void generateTransistorItem(File file) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x1B1F23));
        g.fillRect(4, 3, 8, 6);
        g.setColor(new Color(0xBDC3C7));
        g.fillRect(5, 9, 1, 5);
        g.fillRect(7, 9, 1, 5);
        g.fillRect(9, 9, 1, 5);
        g.dispose();
        saveImage(img, file);
    }
}
