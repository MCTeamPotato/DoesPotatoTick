package me.kall.doespotatotick.common.data;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.kall.doespotatotick.common.config.PotatoConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;

public class PlayerTracker {
    public static final Object2ObjectMap<ResourceLocation, Long2ObjectMap<MinMaxData>> ACTIVE_CHUNKS = new Object2ObjectOpenHashMap<>();

    public static void onLevelTick(TickEvent.@NotNull LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Level level = event.level;

        if (PotatoConfig.ONLY_WORKS_ON_SERVER_THREAD.get()) {
            if (event.side == LogicalSide.CLIENT) return;
            if (!(level instanceof ServerLevel serverLevel)) return;
            if (!serverLevel.getServer().isSameThread()) return;
        }

        ResourceLocation dimId = level.dimension().location();

        Long2ObjectMap<MinMaxData> chunkMap = ACTIVE_CHUNKS.computeIfAbsent(dimId, k -> new Long2ObjectOpenHashMap<>());
        chunkMap.clear();

        int horizontalRadius = PotatoConfig.getHorizontal();
        int verticalBlocks = PotatoConfig.getVertical() * 16;

        for (Player player : level.players()) {
            ChunkPos center = player.chunkPosition();
            int y = player.blockPosition().getY();
            int minY = y - verticalBlocks;
            int maxY = y + verticalBlocks;

            for (int dx = -horizontalRadius; dx <= horizontalRadius; dx++) {
                for (int dz = -horizontalRadius; dz <= horizontalRadius; dz++) {
                    long chunkKey = ChunkPos.asLong(center.x + dx, center.z + dz);

                    MinMaxData existing = chunkMap.get(chunkKey);
                    if (existing == null) {
                        chunkMap.put(chunkKey, new MinMaxData(minY, maxY));
                    } else {
                        int newMin = Math.min(existing.min, minY);
                        int newMax = Math.max(existing.max, maxY);
                        chunkMap.put(chunkKey, new MinMaxData(newMin, newMax));
                    }
                }
            }
        }
    }

    public static boolean isEntityNearPlayers(@NotNull ResourceLocation dimId, int entityY, long chunkKey) {
        final Long2ObjectMap<MinMaxData> chunkMap = ACTIVE_CHUNKS.get(dimId);
        if (chunkMap == null) return false;

        final MinMaxData minMax = chunkMap.get(chunkKey);
        if (minMax == null) return false;

        return entityY >= minMax.min && entityY <= minMax.max;
    }

    public static final class MinMaxData {
        public final int min;
        public final int max;

        private MinMaxData(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}
