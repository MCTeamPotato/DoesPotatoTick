package com.teampotato.doespotatotick.mixin;

import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkManager;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = ClaimedChunkManager.class, remap = false)
public abstract class ClaimedChunkManagerMixin {
    @Mutable @Shadow @Final public Map<ChunkDimPos, ClaimedChunk> claimedChunks;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.claimedChunks = new Object2ObjectOpenHashMap<>(this.claimedChunks);
    }
}