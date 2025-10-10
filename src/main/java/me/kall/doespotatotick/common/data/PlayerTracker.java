package me.kall.doespotatotick.common.data;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.kall.doespotatotick.common.config.PotatoConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerTracker {
    public static final Object2ObjectMap<ResourceLocation, Long2ObjectMap<YRange>> ACTIVE_CHUNKS = new Object2ObjectOpenHashMap<>();

    public static void onLevelTick(TickEvent.@NotNull LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Level level = event.level;
        if (!PotatoConfig.threadSupported(level)) return;

        ResourceLocation dimId = level.dimension().location();

        Long2ObjectMap<YRange> chunkMap = ACTIVE_CHUNKS.computeIfAbsent(dimId, k -> new Long2ObjectOpenHashMap<>());
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

                    YRange yRange = chunkMap.get(chunkKey);
                    if (yRange == null) {
                        yRange = new YRange(minY, maxY);
                        chunkMap.put(chunkKey, yRange);
                    } else {
                        if (minY < yRange.min) yRange.min = minY;
                        if (maxY > yRange.max) yRange.max = maxY;
                    }
                }
            }
        }
    }

    public static boolean isEntityNearPlayers(@NotNull ResourceLocation dimId, int entityY, long chunkKey) {
        final Long2ObjectMap<YRange> chunkMap = ACTIVE_CHUNKS.get(dimId);
        if (chunkMap == null) return false;

        final YRange yRange = chunkMap.get(chunkKey);
        if (yRange == null) return false;

        return entityY >= yRange.min && entityY <= yRange.max;
    }

    public static final class YRange {
        public int min;
        public int max;

        private YRange(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}
