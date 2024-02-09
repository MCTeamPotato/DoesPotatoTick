package com.teampotato.doespotatotick.integration;

import dev.ftb.mods.ftbchunks.data.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class ChunkClaimProvider {
    public boolean isInClaimedChunk(Level level, BlockPos pos) {
        if (!FTBChunksAPI.isManagerLoaded()) return true;
        return FTBChunksAPI.getManager().claimedChunks.containsKey(new ChunkDimPos(level, pos));
    }
}
