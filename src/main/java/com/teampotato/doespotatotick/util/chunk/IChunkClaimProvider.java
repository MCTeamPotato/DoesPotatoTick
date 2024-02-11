package com.teampotato.doespotatotick.util.chunk;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChunkClaimProvider {
    boolean isInClaimedChunk(World paramWorld, BlockPos paramBlockPos);
}