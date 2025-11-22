package me.kall.doespotatotick.events;

import it.unimi.dsi.fastutil.ints.IntSet;
import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.ext.Tickable;
import me.kall.doespotatotick.integration.ClaimManager;
import me.kall.duplicationless.data.EntityTracker;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConfigEvents {
    private static final Set<UUID> PLAYERS = new HashSet<>();
    private static final ResourceLocation ENEMIES = new ResourceLocation(DoesPotatoTick.MOD_ID, "enemies");

    public static void filter(EntityTracker.@NotNull EntityFilterRegistryEvent event) {
        if (ConfigConstants.detectEnemies) event.register(ENEMIES, entity -> entity instanceof Enemy);
    }

    public static void onChunkTick(long chunk, ServerLevel level) {
        if (!ConfigConstants.mobFarmDetection || level.getServer().getTickCount() % 100 != 0) return;
        level.getServer().execute(() -> {
            if (ConfigConstants.detectEnemies) {
                IntSet enemies = EntityTracker.getEntities(level, chunk, ENEMIES);
                if (enemies.size() >= ConfigConstants.mobFarmThreshold) {
                    for (int id : enemies) {
                        Entity entity = level.getEntity(id);
                        if (entity == null) continue;
                        ((Tickable) entity).dpt$setAlwaysTick(true);
                    }
                }
            }

            if (!ConfigConstants.mobFarmTypes.isEmpty()) {
                for (EntityType<?> mobFarmType : ConfigConstants.mobFarmTypes) {
                    IntSet typedEntities = EntityTracker.getEntities(level, chunk, mobFarmType);
                    if (typedEntities.size() >= ConfigConstants.mobFarmThreshold) {
                        for (int id : typedEntities) {
                            Entity entity = level.getEntity(id);
                            if (entity == null) continue;
                            ((Tickable)entity).dpt$setAlwaysTick(true);
                        }
                    }
                }
            }
        });
    }

    public static void warn(PlayerEvent.PlayerLoggedInEvent event) {
        if (ConfigConstants.notification) {
            Player player = event.getPlayer();
            if (!PLAYERS.contains(player.getUUID())) {
                player.displayClientMessage(new TranslatableComponent(warnKey()), false);
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

    public static void reloadConfig(ModConfig.Reloading event) {
        if (event.getConfig().getModId().equals(DoesPotatoTick.MOD_ID)) {
            ConfigConstants.validate();
        }
    }

    public static void loadConfig(@NotNull FMLCommonSetupEvent event) {
        event.enqueueWork(ConfigConstants::validate);
    }
}
