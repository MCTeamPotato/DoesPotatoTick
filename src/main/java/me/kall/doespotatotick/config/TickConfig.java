package me.kall.doespotatotick.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class TickConfig {
    public static final ForgeConfigSpec CONFIG;
    static final ForgeConfigSpec.IntValue TICKABLE_VERTICAL_CHUNKS, TICKABLE_HORIZONTAL_CHUNKS;
    static final ForgeConfigSpec.BooleanValue IGNORE_ENEMIES, IGNORE_PROJECTILES, IGNORE_PROJECTILES_TARGETS, IGNORE_ITEMS, IGNORE_RAIDERS_IF_RAIDING, IGNORE_ANIMALS;
    static final ForgeConfigSpec.BooleanValue ENTITY_OPTIMIZATION;
    static final ForgeConfigSpec.BooleanValue ONLY_LIVING, ONLY_ENEMIES, ONLY_ANIMALS, ONLY_WHEN_NO_RAIDS;
    static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAYS_TICK_ENTITIES, ALWAYS_TICK_ENTITIES_MOD_ID, ALWAYS_TICK_RAIDERS, VALID_DIMENSIONS;
    static final ForgeConfigSpec.BooleanValue NOTIFICATION;
    static final ForgeConfigSpec.BooleanValue MOB_FARM_DETECTION, DETECT_ENEMIES;
    static final ForgeConfigSpec.IntValue MOB_FARM_THRESHOLD;
    static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOB_FARM_TYPES;

    static {
        List<? extends String> entityModIDList = Lists.newArrayList("create", "witherstormmod", "traveloptics", "irons_spellbooks" , "valkyrienskies", "vs_eureka");
        List<? extends String> entityList = Lists.newArrayList("minecraft:ender_dragon", "minecraft:ghast", "minecraft:wither", "minecraft:player",
                "alexsmobs:void_worm", "alexsmobs:void_worm_part", "alexsmobs:spectre",
                "twilightforest:naga", "twilightforest:lich", "twilightforest:yeti", "twilightforest:snow_queen", "twilightforest:minoshroom", "twilightforest:hydra", "twilightforest:knight_phantom", "twilightforest:ur_ghast",
                "atum:pharaoh",
                "mowziesmobs:barako", "mowziesmobs:ferrous_wroughtnaut", "mowziesmobs:frostmaw", "mowziesmobs:naga",
                "aoa3:skeletron", "aoa3:smash", "aoa3:baroness", "aoa3:clunkhead", "aoa3:corallus", "aoa3:cotton_candor", "aoa3:craexxeus", "aoa3:xxeus", "aoa3:creep", "aoa3:crystocore", "aoa3:dracyon", "aoa3:graw", "aoa3:gyro", "aoa3:hive_king", "aoa3:kajaros", "aoa3:miskel", "aoa3:harkos", "aoa3:raxxan", "aoa3:okazor", "aoa3:king_bambambam", "aoa3:king_shroomus", "aoa3:kror", "aoa3:mechbot", "aoa3:nethengeic_wither", "aoa3:red_guardian", "aoa3:blue_guardian", "aoa3:green_guardian", "aoa3:yellow_guardian", "aoa3:rock_rider", "aoa3:shadowlord", "aoa3:tyrosaur", "aoa3:vinecorne", "aoa3:visualent", "aoa3:voxxulon", "aoa3:bane", "aoa3:elusive",
                "gaiadimension:malachite_drone", "gaiadimension:malachite_guard",
                "blue_skies:alchemist", "blue_skies:arachnarch", "blue_skies:starlit_crusher", "blue_skies:summoner",
                "stalwart_dungeons:awful_ghast", "stalwart_dungeons:nether_keeper", "stalwart_dungeons:shelterer_without_armor",
                "dungeonsmod:extrapart", "dungeonsmod:king", "dungeonsmod:deserted", "dungeonsmod:crawler", "dungeonsmod:ironslime", "dungeonsmod:kraken", "dungeonsmod:voidmaster", "dungeonsmod:lordskeleton", "dungeonsmod:winterhunter", "dungeonsmod:sun",
                "forestcraft:beequeen", "forestcraft:iguana_king", "forestcraft:cosmic_fiend", "forestcraft:nether_scourge",
                "cataclysm:ender_golem", "cataclysm:ender_guardian", "cataclysm:ignis", "cataclysm:ignited_revenant", "cataclysm:netherite_monstrosity",
                "iceandfire:fire_dragon", "iceandfire:ice_dragon", "iceandfire:lightning_dragon", "iceandfire:dragon_multipart"
        );

        Predicate<Object> isString = object -> object instanceof String;

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("DoesPotatoTickConfig");

        builder.push("Misc Settings");
        NOTIFICATION = builder
                .comment("If enabled, Does Potato Tick will send a client message to notify the login players about their mob farm.")
                .define("Notification", true);
        MOB_FARM_DETECTION = builder
                .comment("If enabled, Does Potato Tick will auto-detect the mob farms in your worlds.")
                .define("MobFarmDetection", true);
        MOB_FARM_THRESHOLD = builder
                .comment("The least count of entities that enables the current chunk to be detected as a mob farm.")
                .defineInRange("MobFarmEntitiesCountThreshold", 25, 0, Integer.MAX_VALUE);
        MOB_FARM_TYPES = builder
                .comment("The type of entities that will be detected.", "Do note that all the enemies are always detected no matter what is defined here if you don't disable the DetectEnemies config option below.")
                .defineListAllowEmpty("MobFarmTypes", Lists.newArrayList(), isString);
        DETECT_ENEMIES = builder
                .comment("If enabled, Does Potato Tick will keep tracking if your worlds have mob farms of enemies.", "Require game restart to take effect.")
                .define("DetectEnemies", true);
        builder.pop();

        builder.push("Entity Ticking Settings");

        builder.push("General Toggle Settings");
        ENTITY_OPTIMIZATION = builder
                .comment("Master switch for the entity optimization system. Disable to turn off all optimizations.")
                .define("EnableGeneralEntityOptimization", true);
        builder.pop();

        builder.push("Distance Settings");
        TICKABLE_VERTICAL_CHUNKS = builder
                .comment("Maximum vertical chunk distance where entities will continue ticking.")
                .defineInRange("MaxTickableChunkDistanceInVerticalHeight", 2, 1, Integer.MAX_VALUE);
        TICKABLE_HORIZONTAL_CHUNKS = builder
                .comment("Maximum horizontal chunk distance where entities will continue ticking.")
                .defineInRange("MaxTickableChunkDistanceInHorizontalWidth", 4, 1, Integer.MAX_VALUE);
        builder.pop();

        builder.push("Only Settings");
        ONLY_LIVING = builder
                .comment("Only optimize living entities (excludes items, projectiles, etc.)")
                .define("OnlyLivingEntitiesOptimizable", false);
        ONLY_ENEMIES = builder
                .comment("Only optimize enemies entities.")
                .define("OnlyEnemiesOptimizable", false);
        ONLY_ANIMALS = builder
                .comment("Only optimize animals and passive mobs.")
                .define("OnlyAnimalsOptimizable", false);
        ONLY_WHEN_NO_RAIDS = builder
                .comment("Only apply optimization when no raid is active in the current level.")
                .define("OnlyOptimizableWhenTheCurrentLevelHasNoRaids", false);
        builder.pop();

        builder.push("Always Tick Settings");

        builder.push("Entities");

        builder.push("Fast Ignore");
        IGNORE_ENEMIES = builder
                .comment("If true, hostile mobs will ALWAYS tick regardless of distance.")
                .define("EnemiesAlwaysTick", false);
        IGNORE_PROJECTILES = builder
                .comment("If true, projectiles will ALWAYS tick (arrows, fireballs, etc.).")
                .define("ProjectilesAlwaysTick", true);
        IGNORE_PROJECTILES_TARGETS = builder
                .comment("If true, entities that are targeted by projectiles will always tick.")
                .define("ProjectileTargetsAlwaysTick", true);
        IGNORE_ITEMS = builder
                .comment("If true, item entities will ALWAYS tick.")
                .define("ItemEntitiesAlwaysTick", false);
        IGNORE_RAIDERS_IF_RAIDING = builder
                .comment("If true, raiders will always tick if the current level has an active raid.")
                .define("RaidersAlwaysTickWhenTheCurrentLevelHasRaids", true);
        IGNORE_ANIMALS = builder
                .comment("If true, animals will ALWAYS tick regardless of distance.")
                .define("AnimalsAlwaysTick", false);
        builder.pop();

        builder.push("Blacklist");
        ALWAYS_TICK_ENTITIES = builder
                .comment("A list of specific entities that will always tick no matter what.")
                .defineList("EntitiesThatWillAlwaysTick", entityList, isString);
        ALWAYS_TICK_ENTITIES_MOD_ID = builder
                .comment("All entities from these mod IDs will always tick.")
                .defineList("EntitiesOfTheModsThatWillAlwaysTick", entityModIDList, isString);
        ALWAYS_TICK_RAIDERS = builder
                .comment("A list of raider entities that will always tick.")
                .defineListAllowEmpty("RaidersThatWillAlwaysTick", Lists.newArrayList(), isString);
        builder.pop();

        builder.pop();

        builder.push("Dimensions");
        VALID_DIMENSIONS = builder
                .comment("A whitelist of dimensions where entity optimization is enabled. Leave empty to allow all.")
                .defineListAllowEmpty("OptimizableDimensions", Lists.newArrayList(), isString);
        builder.pop();
        builder.pop();
        builder.pop();
        builder.pop();
        CONFIG = builder.build();
    }

    public static final class Client {
        public static final ForgeConfigSpec CONFIG;
        public static final ForgeConfigSpec.BooleanValue SKIP_RENDERING_UNTICKABLE, PROJECTILE_WEAPON_SKIP;

        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.push("DoesPotatoTickClientConfig");
            SKIP_RENDERING_UNTICKABLE = builder
                    .comment("If true, entities that are not ticking will not be rendered on the client. Improves FPS.", "You can also configure whether this option is invalidated when you are using bow/crossbow items.")
                    .define("StopRenderingUntickableEntities", true);
            PROJECTILE_WEAPON_SKIP = builder
                    .comment("If true, entities will always be rendered even though they're not tickable when client players are charging projectile weapons.")
                    .define("RenderAllEntitiesWhenChargingProjectileWeapons", true);
            builder.pop();
            CONFIG = builder.build();
        }
    }
}
