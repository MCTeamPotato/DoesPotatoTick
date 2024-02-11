package com.teampotato.doespotatotick.util.chunk;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DummyChunkClaimProvider implements IChunkClaimProvider {
    public boolean isInClaimedChunk(World world, BlockPos pos) {
        return false;
    }
}
