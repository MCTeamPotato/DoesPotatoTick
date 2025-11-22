package me.kall.doespotatotick.events;

import me.kall.doespotatotick.config.ConfigConstants;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.jetbrains.annotations.NotNull;

public class ClientEvents {
    public static void bowStart(LivingEntityUseItemEvent.@NotNull Start event) {
        if (!ConfigConstants.projectileWeaponSkip) return;
        if (event.getEntity() instanceof LocalPlayer && ConfigConstants.skipRenderingUntickable && event.getItem().getItem() instanceof ProjectileWeaponItem) {
            ConfigConstants.skipRenderingUntickable = false;
        }
    }

    public static void bowEnd(@NotNull LivingEntityUseItemEvent event) {
        if (!ConfigConstants.projectileWeaponSkip) return;
        if (event.getEntity() instanceof LocalPlayer && event.getItem().getItem() instanceof ProjectileWeaponItem) {
            ConfigConstants.validateClient();
        }
    }
}
