package me.kall.doespotatotick;

import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.config.TickConfig;
import me.kall.doespotatotick.events.ClientEvents;
import me.kall.doespotatotick.events.ConfigEvents;
import me.kall.doespotatotick.events.PlayerTracker;
import me.kall.doespotatotick.ext.Tickable;
import me.kall.doespotatotick.integration.ClaimManager;
import me.kall.doespotatotick.integration.sodium.SodiumIntegration;
import me.kall.doespotatotick.mixin.access.LivingEntityAccessor;
import me.kall.doespotatotick.network.TickablePacket;
import me.kall.duplicationless.util.Mods;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

@Mod(DoesPotatoTick.MOD_ID)
public final class DoesPotatoTick {
    public static final String MOD_ID = "doespotatotick";

    public DoesPotatoTick(IEventBus modBus, Dist dist, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, TickConfig.CONFIG);
        container.registerConfig(ModConfig.Type.CLIENT, TickConfig.Client.CONFIG);

        IEventBus forgeBus = NeoForge.EVENT_BUS;

        modBus.addListener(ConfigEvents::reloadConfig);
        modBus.addListener(ConfigEvents::loadConfig);
        modBus.addListener(TickablePacket::register);

        forgeBus.addListener(ConfigEvents::warn);
        forgeBus.addListener(ConfigEvents::filter);
        forgeBus.addListener(PlayerTracker::login);
        forgeBus.addListener(PlayerTracker::dimChange);
        forgeBus.addListener(PlayerTracker::tickLevel);
        forgeBus.addListener(DoesPotatoTick::onEntityTick);

        if (dist.isClient()) {
            forgeBus.addListener(ClientEvents::bowStart);
            forgeBus.addListener((LivingEntityUseItemEvent.Stop event) -> ClientEvents.bowEnd(event));
            forgeBus.addListener((LivingEntityUseItemEvent.Finish event) -> ClientEvents.bowEnd(event));

            if (Mods.isLoaded("embeddium")) SodiumIntegration.register();
        }
    }

    public static boolean invalidThread() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return true;
        return !server.isSameThread();
    }

    public static boolean isTickable(@NotNull Entity entity, ServerLevel level) {
        Tickable tickable = (Tickable) entity;
        Tickable.Level tickableLevel = (Tickable.Level) level;

        long chunk = entity.chunkPosition().toLong();

        if (tickable.dpt$alwaysTick()) return true;
        if (entity instanceof LivingEntity living && (((LivingEntityAccessor)living).dpt$isDead() || living.isDeadOrDying())) return true;
        if (!tickableLevel.dpt$valid()) return true;
        if (ClaimManager.isClaimedChunk(level, entity.blockPosition())) return true;
        if (level.getForcedChunks().contains(chunk)) return true;
        if (tickableLevel.dpt$hasRaids()) {
            if (ConfigConstants.onlyWhenNoRaids) return true;
            if (((Tickable.EntityType)entity.getType()).dpt$raidTick()) return true;
            if (ConfigConstants.ignoreRaidersIfRaiding && entity instanceof Raider) return true;
        }

        return PlayerTracker.include(level.dimension().location(), entity.getBlockY(), chunk);
    }

    public static void onEntityTick(EntityTickEvent.@NotNull Pre event) {
        if (event.isCanceled()) return;
        if (invalidThread()) return;
        Entity entity = event.getEntity();
        if (entity instanceof Tickable tickable && entity.level() instanceof ServerLevel level) {
            tickable.dpt$setTickable(isTickable(entity, level));
            if (tickable.dpt$tickable()) return;
            event.setCanceled(true);
        }
    }
}
