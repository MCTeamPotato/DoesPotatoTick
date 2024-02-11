package com.teampotato.doespotatotick.mixin;

import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkManager;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = ClaimedChunkManager.class, remap = false)
public interface ClaimedChunkManagerAccessor {
    @Accessor("claimedChunks")
    Map<ChunkDimPos, ClaimedChunk> getClaimedChunks();
}