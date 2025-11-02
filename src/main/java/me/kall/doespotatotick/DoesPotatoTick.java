package me.kall.doespotatotick;

import me.kall.doespotatotick.common.api.IRaids;
import me.kall.doespotatotick.common.api.Tickable;
import me.kall.doespotatotick.common.config.PotatoConfig;
import me.kall.doespotatotick.common.data.PlayerTracker;
import me.kall.doespotatotick.common.integration.ClaimManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@Mod(DoesPotatoTick.MOD_ID)
public final class DoesPotatoTick {
    public static final String MOD_ID = "doespotatotick";

    public DoesPotatoTick(@NotNull FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, PotatoConfig.COMMON_CONFIG);
        context.getModEventBus().addListener(PotatoConfig::setupConfig);
        MinecraftForge.EVENT_BUS.addListener(PotatoConfig::warn);
        MinecraftForge.EVENT_BUS.addListener(PlayerTracker::onLevelTick);
    }

    public static boolean isTickable(@NotNull Entity entity) {
        if (((Tickable)entity).dpt$alwaysTick()) return true;
        if (entity instanceof LivingEntity && ((LivingEntity) entity).isDeadOrDying()) return true;

        Level level = entity.level();
        BlockPos entityPos = entity.blockPosition();
        EntityType<?> entityType = entity.getType();

        if (((Tickable.EntityType)entityType).dpt$alwaysTick()) return true;

        if (!PotatoConfig.allDimsOptimizable() && !((Tickable.Dim)level).dpt$optimizableDim()) return true;

        if (ClaimManager.isClaimed(level, entityPos)) return true;

        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getForcedChunks().contains(ChunkPos.asLong(entityPos))) return true;
            if (((IRaids)serverLevel.getRaids()).dpt$hasRaid()) {
                if (entity instanceof Raider && PotatoConfig.TICKING_RAIDER_ENTITIES_WHEN_RAID.get()) return true;
                if (((Tickable.EntityType)entityType).dpt$alwaysTickInRaid()) return true;
            }
        }

        if (PotatoConfig.OPTIMIZE_ITEM_MOVEMENT.get() && entity instanceof ItemEntity itemEntity && !PotatoConfig.getItems().contains(itemEntity.getItem().getItem())) return ThreadLocalRandom.current().nextBoolean();

        return PlayerTracker.isEntityNearPlayers(level.dimension().location(), entity.getBlockY(), entity.chunkPosition().toLong());
    }
}
