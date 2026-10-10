package com.gastinelweather.item;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.integration.MinecraftHeightSampler;
import com.gastinelweather.weather.HeightSampler;
import com.gastinelweather.weather.SynopticState;
import com.gastinelweather.weather.WeatherSample;
import com.gastinelweather.weather.WeatherSystem;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;

public class ElitechItem extends Item {

    public ElitechItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            WeatherSystem system = GastinelWeatherMod.getWeatherSystem();
            float gameHours = level.getDayTime() / 1000.0f;
            Vector3f pos = new Vector3f((float) player.getX(), (float) player.getY(), (float) player.getZ());
            HeightSampler terrain = new MinecraftHeightSampler(level);

            SynopticState sky = system.synopticAt(gameHours);
            WeatherSample sample = system.sampleAt(pos, sky, terrain);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.6f, 1.8f);

            // Message Actionbar (rapide)
            player.displayClientMessage(Component.literal(
                String.format("§6[Elitech] §e%.1f°C §7(Ressenti §b%.1f°C§7) | Vent §f%.0f km/h §7| Sol §a%.0f%%",
                    sample.temperatureC, sample.windChillC, sample.getWindSpeedKmh(), sample.wetness * 100.0f)
            ), true);

            // Message Chat détaillé
            player.sendSystemMessage(Component.literal(
                String.format("§8[§6Elitech Data Logger§8] §e%.1f°C §7(Ressenti: §b%.1f°C§7) | Alt: §f%d m §7| Versant: §6%+.1f°C §7| Précip: §b%.1f mm/h",
                    sample.temperatureC, sample.windChillC, (int)player.getY(), sample.aspectC, sample.precipitationMmPerHour)
            ));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("§7Enregistreur de données thermo-hygromètre portable."));
        tooltipComponents.add(Component.literal("§eClic Droit §7: Relever la température, le vent et l'humidité locale."));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
