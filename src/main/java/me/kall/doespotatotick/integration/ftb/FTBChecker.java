package me.kall.doespotatotick.integration.ftb;

import dev.ftb.mods.ftbchunks.data.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import me.kall.doespotatotick.integration.IChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class FTBChecker implements IChecker {
    @Override
    public boolean isClaimed(ServerLevel level, BlockPos pos) {
        if (!FTBChunksAPI.isManagerLoaded()) return false;
        return FTBChunksAPI.getManager().getChunk(new ChunkDimPos(level, pos)) != null;
    }
}
