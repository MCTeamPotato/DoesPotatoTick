package me.kall.doespotatotick.events;

import me.kall.doespotatotick.config.ConfigConstants;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.jetbrains.annotations.NotNull;

public class ClientEvents {
    public static void bowStart(LivingEntityUseItemEvent.@NotNull Start event) {
        if (!ConfigConstants.bowSkip) return;
        if (event.getEntity() instanceof LocalPlayer && ConfigConstants.skipRenderingUntickable) {
            ConfigConstants.skipRenderingUntickable = false;
        }
    }

    public static void bowEnd(@NotNull LivingEntityUseItemEvent event) {
        if (!ConfigConstants.bowSkip) return;
        if (event.getEntity() instanceof LocalPlayer) {
            ConfigConstants.validateClient();
        }
    }
}
