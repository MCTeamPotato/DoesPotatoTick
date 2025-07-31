package me.kall.doespotatotick.common.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kall.doespotatotick.common.api.Tickable;
import me.kall.doespotatotick.common.integration.ClaimManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PotatoConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.IntValue LIVING_HORIZONTAL_TICK_DIST;
    public static final ForgeConfigSpec.IntValue LIVING_VERTICAL_TICK_DIST;
    public static final ForgeConfigSpec.BooleanValue OPTIMIZE_ITEM_MOVEMENT;
    public static final ForgeConfigSpec.BooleanValue IGNORE_DEAD_ENTITIES;
    public static final ForgeConfigSpec.BooleanValue IGNORE_HOSTILE_ENTITIES;
    public static final ForgeConfigSpec.BooleanValue IGNORE_PROJECTILE_ENTITIES;
    public static final ForgeConfigSpec.BooleanValue IGNORE_ITEM_ENTITIES;
    public static final ForgeConfigSpec.BooleanValue TICKING_RAIDER_ENTITIES_IN_RAID;
    public static final ForgeConfigSpec.BooleanValue OPTIMIZE_ENTITIES_TICKING;
    public static final ForgeConfigSpec.BooleanValue ONLY_LIVING_OPTIMIZABLE;
    public static final ForgeConfigSpec.BooleanValue SEND_MESSAGE;
    public static final ForgeConfigSpec.BooleanValue ALLOW_TICKING_FORCE_LOADED;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITIES_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEMS_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITIES_MOD_ID_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> RAID_ENTITIES_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> RAID_ENTITIES_MOD_ID_LIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_WHITELIST;

    static {
        List<? extends String> itemList = Lists.newArrayList("minecraft:cobblestone");
        List<? extends String> entityModIdList = Lists.newArrayList("create", "witherstormmod");
        List<? extends String> entityWhiteList = Lists.newArrayList("minecraft:ender_dragon", "minecraft:ghast", "minecraft:wither", "minecraft:player",
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
                "iceandfire:fire_dragon", "iceandfire:ice_dragon", "iceandfire:lightning_dragon", "iceandfire:dragon_multipart");

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("DoesPotatoTick?").push("Living Entities Tick Settings");
        OPTIMIZE_ENTITIES_TICKING = builder.comment("If you disable this, entities will not stop ticking when they'are far from you, this mod may be useless for you too").define("OptimizeEntitiesTicking", true);
        LIVING_HORIZONTAL_TICK_DIST = builder.defineInRange("LivingEntitiesMaxHorizontalTickDistance", 64, 1, Integer.MAX_VALUE);
        LIVING_VERTICAL_TICK_DIST = builder.defineInRange("LivingEntitiesMaxVerticalTickDistance", 32, 1, Integer.MAX_VALUE);
        ENTITIES_WHITELIST = builder.comment("If you don't want an entity to be affected by the optimization, you can write its registry name down here.").defineList("EntitiesWhitelist", entityWhiteList, Predicates.alwaysTrue());
        ENTITIES_MOD_ID_WHITELIST = builder.comment("If you don't want entities of a mod to be affected by the optimization, you can write its modid down here").defineList("EntitiesModIDWhiteList", entityModIdList, Predicates.alwaysTrue());
        TICKING_RAIDER_ENTITIES_IN_RAID = builder.comment("With this turned on, all the raider will always tick if the world has raids").define("TickRaidersIfRaid", true);
        RAID_ENTITIES_WHITELIST = builder.comment("Similar to entity whitelist, but only take effect in raid.").defineList("RaidEntitiesWhiteList", ObjectArrayList.wrap(new String[]{"minecraft:witch", "minecraft:vex"}), Predicates.alwaysTrue());
        RAID_ENTITIES_MOD_ID_LIST = builder.comment("Similar to entity modID whitelist, but only take effect in raid").defineList("RaidEntitiesModIDWhiteList", new ObjectArrayList<>(), Predicates.alwaysTrue());
        ALLOW_TICKING_FORCE_LOADED = builder.comment("Allow ticking of entities in force loaded chunks").define("AllowForceLoaded", true);
        DIMENSION_WHITELIST = builder.comment("Leave this empty for applying to all the dimensions", "Entities in these dimensions will be affected by the optimization").defineList("DimensionWhitelist", new ObjectArrayList<>(), Predicates.alwaysTrue());
        IGNORE_DEAD_ENTITIES = builder.comment("If this is enabled, dead entities will always tick").define("IgnoreDeadEntities", false);
        IGNORE_HOSTILE_ENTITIES = builder.comment("If this is enabled, hostile entities will always tick").define("IgnoreHostileEntities", false);
        IGNORE_PROJECTILE_ENTITIES = builder.comment("If this is enabled, projectiles will always tick").define("IgnoreProjectiles", true);
        ONLY_LIVING_OPTIMIZABLE = builder.comment("If this is enabled, optimization will only take effect on living entities").define("OnlyLivingOptimizable", false);
        builder.pop();
        builder.push("Item Entities Tick Settings");
        IGNORE_ITEM_ENTITIES = builder.comment("If this is enabled, item entities will always tick").define("IgnoreItemEntities", true);
        OPTIMIZE_ITEM_MOVEMENT = builder.comment("Slow down item entities' ticking speed", "Note this does impact rendering continuity, so this optimization is disabled by default").define("OptimizeItemMovement", false);
        ITEMS_WHITELIST = builder.comment("If you don't want to let a specific item entity in the world to be effected by the optimization, you can write its registry name down here.", "Require 'OptimizeItemMovement' to be true").defineList("ItemWhiteList", itemList, Predicates.alwaysTrue());
        builder.pop();
        builder.push("Misc");
        SEND_MESSAGE = builder.define("SendWarningMessageWhenPlayerLogIn", true);
        builder.pop();
        COMMON_CONFIG = builder.build();
    }

    private static volatile Set<ResourceLocation> dims = null;
    private static volatile Set<Item> items = null;
    private static volatile int maxDistSquared = 0;
    private static volatile int maxHeight = 0;

    public static Set<ResourceLocation> getDimensions() {
        if (dims == null) dims = DIMENSION_WHITELIST.get().stream().map(ResourceLocation::parse).collect(Collectors.toSet());
        return dims;
    }

    public static Set<Item> getItems() {
        if (items == null) items = ITEMS_WHITELIST.get().stream().map(ResourceLocation::parse).map(ForgeRegistries.ITEMS::getValue).collect(Collectors.toSet());
        return items;
    }

    public static boolean allDimsOptimizable() {
        return DIMENSION_WHITELIST.get().isEmpty();
    }

    public static void setupConfig(@NotNull FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (EntityType<?> entityType : ForgeRegistries.ENTITY_TYPES) {
                ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
                if (id != null) {
                    if (ENTITIES_WHITELIST.get().contains(id.toString()) || ENTITIES_MOD_ID_WHITELIST.get().contains(id.getNamespace())) ((Tickable.EntityType)entityType).doesPotatoTick$setShouldAlwaysTick();
                    if (RAID_ENTITIES_WHITELIST.get().contains(id.toString()) || RAID_ENTITIES_MOD_ID_LIST.get().contains(id.getNamespace())) ((Tickable.EntityType)entityType).doesPotatoTick$setShouldAlwaysTickInRaid();
                }
            }
        });
    }

    public static void warn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!SEND_MESSAGE.get()) return;
        if (ClaimManager.FTB_CHUNKS) {
            if (ClaimManager.OPEN_PARTIES_AND_CLAIMS) {
                event.getEntity().displayClientMessage(Component.translatable("doespotatotick.warn.both.present"), false);
            } else {
                event.getEntity().displayClientMessage(Component.translatable("doespotatotick.warn.ftbchunks.present"), false);
            }
        } else {
            if (ClaimManager.OPEN_PARTIES_AND_CLAIMS) {
                event.getEntity().displayClientMessage(Component.translatable("doespotatotick.warn.openpartiesandclaims.present"), false);
            } else {
                event.getEntity().displayClientMessage(Component.translatable("doespotatotick.warn.notfound"), false);
            }
        }
    }

    private static int maxDistSquared() {
        if (maxDistSquared == 0) maxDistSquared =PotatoConfig.LIVING_HORIZONTAL_TICK_DIST.get() * PotatoConfig.LIVING_HORIZONTAL_TICK_DIST.get();
        return maxDistSquared;
    }

    private static int maxHeight() {
        if (maxHeight == 0) maxHeight = LIVING_VERTICAL_TICK_DIST.get();
        return maxHeight;
    }

    public static boolean isNearPlayer(@NotNull Level level, @NotNull BlockPos pos) {
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        int maxHeight = maxHeight();
        int maxDistSquared = maxDistSquared();
        for (Player player : level.players()) {
            if (Math.abs(player.getY() - posY) < maxHeight) {
                double x = player.getX() - posX;
                double z = player.getZ() - posZ;
                if ((x * x + z * z) < maxDistSquared) return true;
            }
        }
        return false;
    }
}
