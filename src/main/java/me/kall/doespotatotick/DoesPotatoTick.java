package me.kall.doespotatotick;

import me.kall.doespotatotick.common.api.IRaids;
import me.kall.doespotatotick.common.api.Tickable;
import me.kall.doespotatotick.common.config.PotatoConfig;
import me.kall.doespotatotick.common.integration.ClaimManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
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
    }

    public static boolean isTickable(Entity entity) {
        if (!PotatoConfig.OPTIMIZE_ENTITIES_TICKING.get()) return true;
        if (entity instanceof Projectile && PotatoConfig.IGNORE_PROJECTILE_ENTITIES.get()) return true;
        if (entity instanceof ItemEntity && PotatoConfig.IGNORE_ITEM_ENTITIES.get()) return true;
        if (PotatoConfig.ONLY_LIVING_OPTIMIZABLE.get()) {
            if (entity instanceof LivingEntity living) {
                if (PotatoConfig.IGNORE_DEAD_ENTITIES.get() && living.isDeadOrDying()) return true;
                if (PotatoConfig.IGNORE_HOSTILE_ENTITIES.get() && (living instanceof Enemy)) return true;
            } else {
                return true;
            }
        }

        Level level = entity.level();
        BlockPos entityPos = entity.blockPosition();
        EntityType<?> entityType = entity.getType();

        if (((Tickable.EntityType)entityType).doesPotatoTick$shouldAlwaysTick()) return true;

        if (!PotatoConfig.allDimsOptimizable() && !((Tickable.Level)level).doesPotatoTick$isInOptimizableDimension()) return true;

        if (ClaimManager.isClaimed(level, entityPos)) return true;
        if (entity instanceof FallingBlockEntity) return true;

        if (level instanceof ServerLevel serverLevel) {
            if (PotatoConfig.ALLOW_TICKING_FORCE_LOADED.get() && serverLevel.getForcedChunks().contains(ChunkPos.asLong(entityPos))) return true;
            if (((IRaids)serverLevel.getRaids()).doesPotatoTick$hasRaid()) {
                if (entity instanceof Raider && PotatoConfig.TICKING_RAIDER_ENTITIES_IN_RAID.get()) return true;
                if (((Tickable.EntityType)entityType).doesPotatoTick$shouldAlwaysTickInRaid()) return true;
            }
        }

        if (PotatoConfig.OPTIMIZE_ITEM_MOVEMENT.get() && entity instanceof ItemEntity itemEntity && !PotatoConfig.getItems().contains(itemEntity.getItem().getItem())) return ThreadLocalRandom.current().nextBoolean();

        return PotatoConfig.isNearPlayer(level, entityPos);
    }
}
