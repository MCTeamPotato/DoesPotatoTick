package me.kall.doespotatotick.mixin.impl;

import me.kall.doespotatotick.events.PlayerTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends EntityMixin {
    @Unique protected int dpt$lastHeight = Integer.MAX_VALUE;
    @Unique private long dpt$lastChunk;

    @Override
    protected void beforePosChange(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        this.dpt$lastChunk = ChunkPos.asLong(player.blockPosition().getX() >> 4, player.blockPosition().getZ() >> 4);
    }

    @Override
    protected void afterPosChange(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        int currentHeight = player.getBlockY();
        MinecraftServer server = player.server;
        if (server == null) return;
        ResourceLocation dim = player.level().dimension().location();
        if (Math.abs(currentHeight - this.dpt$lastHeight) >= 4) {
            server.execute(() -> PlayerTracker.UPDATE_REQUIRED.add(dim));
            this.dpt$lastHeight = currentHeight;
            return;
        }

        long currentChunk = ChunkPos.asLong(player.blockPosition().getX() >> 4, player.blockPosition().getZ() >> 4);
        if (this.dpt$lastChunk != currentChunk) {
            server.execute(() -> PlayerTracker.UPDATE_REQUIRED.add(dim));
        }
    }
}