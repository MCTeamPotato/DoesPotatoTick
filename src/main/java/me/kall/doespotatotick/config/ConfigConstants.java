package me.kall.doespotatotick.config;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.kall.doespotatotick.ext.Tickable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ConfigConstants {
    public static boolean entityOptimizable;
    public static int verticalChunks, horizontalChunks;
    public static boolean ignoreEnemies, ignoreProjectiles, ignoreProjectileTargets, ignoreItems, ignoreRaidersIfRaiding, ignoreAnimals;
    public static boolean onlyLiving, onlyEnemies, onlyAnimals, onlyWhenNoRaids;
    public static boolean skipRenderingUntickable;
    public static boolean notification;
    public static final Set<ResourceLocation> alwaysTickEntities = new ObjectOpenHashSet<>(), alwaysTickRaiders = new ObjectOpenHashSet<>(), validDimensions = new ObjectOpenHashSet<>();
    public static final Set<String> alwaysTickEntitiesModID = new ObjectOpenHashSet<>();

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
        skipRenderingUntickable = TickConfig.SKIP_RENDERING_UNTICKABLE.get();
        notification = TickConfig.NOTIFICATION.get();

        alwaysTickEntities.clear();
        alwaysTickEntitiesModID.clear();
        alwaysTickRaiders.clear();
        validDimensions.clear();

        TickConfig.ALWAYS_TICK_ENTITIES.get().stream().map(ResourceLocation::parse).forEach(alwaysTickEntities::add);
        alwaysTickEntitiesModID.addAll(TickConfig.ALWAYS_TICK_ENTITIES_MOD_ID.get());
        TickConfig.ALWAYS_TICK_RAIDERS.get().stream().map(ResourceLocation::parse).forEach(alwaysTickRaiders::add);
        TickConfig.VALID_DIMENSIONS.get().stream().map(ResourceLocation::parse).forEach(validDimensions::add);

        alwaysTickSetup();
    }

    public static void alwaysTickSetup() {
        for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entry : ForgeRegistries.ENTITY_TYPES.getEntries()) {
            ResourceLocation id = entry.getKey().location();
            Tickable.EntityType entityType = (Tickable.EntityType) entry.getValue();
            if (alwaysTickEntities.contains(id) || alwaysTickEntitiesModID.contains(id.getNamespace())) {
                entityType.dpt$setAlwaysTick(true);
            }

            if (alwaysTickRaiders.contains(id)) {
                entityType.dpt$setRaidTick(true);
            }
        }

        Optional.ofNullable(ServerLifecycleHooks.getCurrentServer()).ifPresent(server -> server.getAllLevels().forEach(level -> level.getAllEntities().forEach(entity -> ((Tickable)entity).dpt$setAlwaysTick(((Tickable)entity).dpt$checkAlwaysTick()))));
    }
}
