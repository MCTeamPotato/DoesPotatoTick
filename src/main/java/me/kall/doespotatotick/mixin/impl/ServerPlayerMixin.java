package me.kall.doespotatotick.mixin.impl;

import me.kall.doespotatotick.data.PlayerTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends EntityMixin {
    @Unique protected int dpt$lastHeight = Integer.MAX_VALUE;

    @Override
    protected void afterPosChange(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        int currentHeight = player.getBlockY();
        MinecraftServer server = player.server;
        if (server == null) return;
        if (Math.abs(currentHeight - this.dpt$lastHeight) >= 4 || this.dpt$lastHeight == Integer.MAX_VALUE) {
            ResourceLocation dim = player.level().dimension().location();
            server.execute(() -> PlayerTracker.UPDATE_REQUIRED.add(dim));
            this.dpt$lastHeight = currentHeight;
        }
    }
}