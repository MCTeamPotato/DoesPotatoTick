package me.kall.doespotatotick.integration.ftb;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import me.kall.doespotatotick.integration.IChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class FTBChecker implements IChecker {
    @Override
    public boolean isClaimed(ServerLevel level, BlockPos pos) {
        FTBChunksAPI.API api = FTBChunksAPI.api();
        if (!api.isManagerLoaded()) return false;
        return api.getManager().getChunk(new ChunkDimPos(level, pos)) != null;
    }
}
