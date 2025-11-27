package me.kall.doespotatotick.config;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.kall.doespotatotick.ext.Tickable;
import me.kall.duplicationless.util.RegistryEntries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ConfigConstants {
    public static boolean entityOptimizable;
    public static int verticalChunks, horizontalChunks;
    public static int mobFarmThreshold;
    public static boolean ignoreEnemies, ignoreProjectiles, ignoreProjectileTargets, ignoreItems, ignoreRaidersIfRaiding, ignoreAnimals;
    public static boolean onlyLiving, onlyEnemies, onlyAnimals, onlyWhenNoRaids;
    public static boolean notification, mobFarmDetection, detectEnemies;
    public static final Set<ResourceLocation> alwaysTickEntities = new ObjectOpenHashSet<>(), alwaysTickRaiders = new ObjectOpenHashSet<>(), validDimensions = new ObjectOpenHashSet<>();
    public static final Set<EntityType<?>> mobFarmTypes = new ObjectOpenHashSet<>();
    public static final Set<String> alwaysTickEntitiesModID = new ObjectOpenHashSet<>();

    public static volatile boolean skipRenderingUntickable;
    public static boolean projectileWeaponSkip;

    public static void validate() {
        entityOptimizable = TickConfig.ENTITY_OPTIMIZATION.get();
        verticalChunks = TickConfig.TICKABLE_VERTICAL_CHUNKS.get();
        horizontalChunks = TickConfig.TICKABLE_HORIZONTAL_CHUNKS.get();
        ignoreEnemies = TickConfig.IGNORE_ENEMIES.get();
        ignoreProjectiles = TickConfig.IGNORE_PROJECTILES.get();
        ignoreProjectileTargets = TickConfig.IGNORE_PROJECTILES_TARGETS.get();
        ignoreItems = TickConfig.IGNORE_ITEMS.get();
        ignoreRaidersIfRaiding = TickConfig.IGNORE_RAIDERS_IF_RAIDING.get();
        ignoreAnimals = TickConfig.IGNORE_ANIMALS.get();
        onlyLiving = TickConfig.ONLY_LIVING.get();
        onlyEnemies = TickConfig.ONLY_ENEMIES.get();
        onlyAnimals = TickConfig.ONLY_ANIMALS.get();
        onlyWhenNoRaids = TickConfig.ONLY_WHEN_NO_RAIDS.get();
        notification = TickConfig.NOTIFICATION.get();
        mobFarmDetection = TickConfig.MOB_FARM_DETECTION.get();
        mobFarmThreshold = TickConfig.MOB_FARM_THRESHOLD.get();
        detectEnemies = TickConfig.DETECT_ENEMIES.get();

        alwaysTickEntities.clear();
        alwaysTickEntitiesModID.clear();
        alwaysTickRaiders.clear();
        validDimensions.clear();
        mobFarmTypes.clear();

        TickConfig.ALWAYS_TICK_ENTITIES.get().stream().map(ResourceLocation::parse).forEach(alwaysTickEntities::add);
        alwaysTickEntitiesModID.addAll(TickConfig.ALWAYS_TICK_ENTITIES_MOD_ID.get());
        TickConfig.ALWAYS_TICK_RAIDERS.get().stream().map(ResourceLocation::parse).forEach(alwaysTickRaiders::add);
        TickConfig.VALID_DIMENSIONS.get().stream().map(ResourceLocation::parse).forEach(validDimensions::add);
        TickConfig.MOB_FARM_TYPES.get().stream().map(ResourceLocation::parse).map(RegistryEntries::entityType).forEach(mobFarmTypes::add);

        alwaysTickSetup();

        if (FMLLoader.getDist().isClient()) validateClient();
    }

    public static void validateClient() {
        skipRenderingUntickable = TickConfig.Client.SKIP_RENDERING_UNTICKABLE.get();
        projectileWeaponSkip = TickConfig.Client.PROJECTILE_WEAPON_SKIP.get();
    }

    public static void alwaysTickSetup() {
        for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : BuiltInRegistries.ENTITY_TYPE.entrySet()) {
            ResourceLocation id = entry.getKey().location();
            Tickable.EntityType entityType = (Tickable.EntityType) entry.getValue();
            if (alwaysTickEntities.contains(id) || alwaysTickEntitiesModID.contains(id.getNamespace())) {
                entityType.dpt$setAlwaysTick(true);
            }

            if (alwaysTickRaiders.contains(id)) {
                entityType.dpt$setRaidTick(true);
            }
        }

        Optional.ofNullable(ServerLifecycleHooks.getCurrentServer()).ifPresent(server -> server.getAllLevels().forEach(level -> level.getEntities().getAll().forEach(entity -> ((Tickable)entity).dpt$setAlwaysTick(((Tickable)entity).dpt$checkAlwaysTick()))));
    }
}
