package me.kall.doespotatotick;

import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.config.TickConfig;
import me.kall.doespotatotick.events.ClientEvents;
import me.kall.doespotatotick.events.ConfigEvents;
import me.kall.doespotatotick.events.PlayerTracker;
import me.kall.doespotatotick.ext.Tickable;
import me.kall.doespotatotick.integration.ClaimManager;
import me.kall.doespotatotick.mixin.access.LivingEntityAccessor;
import me.kall.doespotatotick.network.TickablePacket;
import me.kall.duplicationless.network.Networker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

@Mod(DoesPotatoTick.MOD_ID)
public final class DoesPotatoTick {
    public static final String MOD_ID = "doespotatotick";

    public static final SimpleChannel CHANNEL = Networker.create(MOD_ID, "1");

    public DoesPotatoTick() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TickConfig.CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TickConfig.Client.CONFIG);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(ConfigEvents::reloadConfig);
        modBus.addListener(ConfigEvents::loadConfig);

        forgeBus.addListener(ConfigEvents::warn);
        forgeBus.addListener(ConfigEvents::filter);
        forgeBus.addListener(PlayerTracker::login);
        forgeBus.addListener(PlayerTracker::dimChange);
        forgeBus.addListener(PlayerTracker::tickLevel);

        if (FMLLoader.getDist().isClient()) {
            forgeBus.addListener(ClientEvents::bowStart);
            forgeBus.addListener((LivingEntityUseItemEvent.Stop event) -> ClientEvents.bowEnd(event));
            forgeBus.addListener((LivingEntityUseItemEvent.Finish event) -> ClientEvents.bowEnd(event));
        }

        CHANNEL.registerMessage(0, TickablePacket.class, TickablePacket::toBytes, TickablePacket::new, TickablePacket::handle);
    }

    public static boolean invalidThread() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return true;
        return !server.isSameThread();
    }

    public static boolean isTickable(@NotNull Entity entity, ServerLevel level) {
        Tickable tickable = (Tickable) entity;
        Tickable.Level tickableLevel = (Tickable.Level) level;

        long chunk = ChunkPos.asLong(entity.blockPosition().getX() >> 4, entity.blockPosition().getZ() >> 4);

        if (tickable.dpt$alwaysTick()) return true;
        if (entity instanceof LivingEntity && (((LivingEntityAccessor)entity).dpt$isDead() || ((LivingEntity)entity).isDeadOrDying())) return true;
        if (entity instanceof TamableAnimal && ((TamableAnimal)entity).getOwner() != null) {
            tickable.dpt$setAlwaysTick(true);
            return true;
        }
        if (!tickableLevel.dpt$valid()) return true;
        if (ClaimManager.isClaimedChunk(level, entity.blockPosition())) return true;
        if (level.getForcedChunks().contains(chunk)) return true;
        if (tickableLevel.dpt$hasRaids()) {
            if (ConfigConstants.onlyWhenNoRaids) return true;
            if (((Tickable.EntityType)entity.getType()).dpt$raidTick()) return true;
            if (ConfigConstants.ignoreRaidersIfRaiding && entity instanceof Raider) return true;
        }

        return PlayerTracker.include(level.dimension().location(), entity.blockPosition().getY(), chunk);
    }
}
