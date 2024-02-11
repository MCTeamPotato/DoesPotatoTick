package com.teampotato.doespotatotick;

import com.google.common.collect.Lists;
import com.teampotato.doespotatotick.util.chunk.ActiveChunkClaimProvider;
import com.teampotato.doespotatotick.util.chunk.DummyChunkClaimProvider;
import com.teampotato.doespotatotick.util.chunk.IChunkClaimProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;
import java.util.function.Predicate;

public class DoesPotatoTick implements ModInitializer {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.IntValue LIVING_ENTITIES_HORIZONTAL_TICK_DISTANCE, LIVING_ENTITIES_VERTICAL_TICK_DISTANCE;
    public static ForgeConfigSpec.BooleanValue OPTIMIZE_ITEM_MOVEMENT, IGNORE_DEAD_ENTITIES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITIES_WHITELIST, ITEM_LIST, ENTITIES_MOD_ID_LIST;

    static {
        List<? extends String> item_list = Lists.newArrayList(
                "minecraft:cobblestone"
        );
        List<? extends String> entity_modid_list = Lists.newArrayList(
                "create"
        );
        List<? extends String> entity_whitelist = Lists.newArrayList();

        Predicate<Object> validator = o -> o instanceof String;

        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Does Potato Tick?").push("Living Entity Settings");
        LIVING_ENTITIES_HORIZONTAL_TICK_DISTANCE = CONFIG_BUILDER.comment("Living Entity Max Horizontal Tick Distance. Number Type: Integer. Default: 64").defineInRange("living entity horizontal tick distance", 64, 1, Integer.MAX_VALUE);
        LIVING_ENTITIES_VERTICAL_TICK_DISTANCE = CONFIG_BUILDER.comment("Living Entity Max Vertical Tick Distance. Number Type: Integer. Default: 32").defineInRange("living entity vertical tick distance", 32, 1, Integer.MAX_VALUE);
        ENTITIES_WHITELIST = CONFIG_BUILDER.comment("If you don't want an entity to be affected by the rule, you can write its registry name down here.").defineList("entity whitelist", entity_whitelist, validator);
        ENTITIES_MOD_ID_LIST = CONFIG_BUILDER.comment("If you don't want entities of a mod to be affected by the rule, you can write its modid down here").defineList("entity modid list", entity_modid_list, validator);
        IGNORE_DEAD_ENTITIES = CONFIG_BUILDER.comment("With this turned on, the living entity tick check will run a lot faster, but entities won't die if outside range. Default: true").define("ignore dead entities", true);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Item Entity Settings");
        OPTIMIZE_ITEM_MOVEMENT = CONFIG_BUILDER.comment("Optimize Item Movement (items move at 1/4 speed). Value Type: true / false.").define("slow down items in the world", false);
        ITEM_LIST = CONFIG_BUILDER.comment("If you don't want to let a specific item entity in the world to be effected by the rule, you can write its registry name down here.", "Require 'slow down items in the world' to be true").defineList("item list", item_list, validator);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }

    public static IChunkClaimProvider chunkClaimProvider;
    private static final String MODID = "doespotatotick";

    @Override
    public void onInitialize() {
        ModLoadingContext.registerConfig(MODID, ModConfig.Type.COMMON, COMMON_CONFIG);
        if (FabricLoader.getInstance().isModLoaded("ftbchunks")) {
            chunkClaimProvider = new ActiveChunkClaimProvider();
        } else {
            chunkClaimProvider = new DummyChunkClaimProvider();
        }
    }
}
