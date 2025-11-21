package me.kall.doespotatotick.events;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.integration.ClaimManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConfigEvents {
    private static final Set<UUID> PLAYERS = new HashSet<>();

    public static void warn(PlayerEvent.PlayerLoggedInEvent event) {
        if (ConfigConstants.notification) {
            Player player = event.getEntity();
            if (!PLAYERS.contains(player.getUUID())) {
                player.displayClientMessage(Component.translatable(warnKey()), false);
                PLAYERS.add(player.getUUID());
            }
        }
    }

    @Contract(pure = true)
    private static @NotNull String warnKey() {
        if (ClaimManager.FTB_LOADED && ClaimManager.OPAC_LOADED) return "doespotatotick.warn.both.present";
        if (ClaimManager.FTB_LOADED) return "doespotatotick.warn.ftbchunks.present";
        if (ClaimManager.OPAC_LOADED) return "doespotatotick.warn.openpartiesandclaims.present";
        return "doespotatotick.warn.notfound";
    }

    public static void reloadConfig(ModConfigEvent.@NotNull Reloading event) {
        if (event.getConfig().getModId().equals(DoesPotatoTick.MOD_ID)) {
            ConfigConstants.validate();
        }
    }

    public static void loadConfig(@NotNull FMLCommonSetupEvent event) {
        event.enqueueWork(ConfigConstants::validate);
    }
}
