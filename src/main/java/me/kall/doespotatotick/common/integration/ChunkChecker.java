package me.kall.doespotatotick.common.integration;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class ChunkChecker {
    public boolean isClaimed(Level level, BlockPos pos) {
        FTBChunksAPI.API api = FTBChunksAPI.api();
        if (!api.isManagerLoaded()) return false;
        return api.getManager().getChunk(new ChunkDimPos(level, pos)) != null;
    }
}
