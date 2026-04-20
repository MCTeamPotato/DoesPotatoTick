package me.kall.doespotatotick.mixin.impl;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.events.ConfigEvents;
import me.kall.doespotatotick.ext.Tickable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements Tickable.Level {
    @Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
    private void onTick(Entity entity, CallbackInfo ci) {
        if (DoesPotatoTick.invalidThread()) return;
        Tickable tickable = (Tickable) entity;
        tickable.dpt$setTickable(DoesPotatoTick.isTickable(entity, (ServerLevel) (Object) this));
        if (!entity.getPersistentData().getBoolean("DPTCanUpdateFixed")) {
            entity.canUpdate(true);
            entity.getPersistentData().putBoolean("DPTCanUpdateFixed", true);
        }
        if (!tickable.dpt$tickable()) ci.cancel();
    }

    @Inject(method = "tickChunk", at = @At("HEAD"))
    private void tickChunk(@NotNull LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ConfigEvents.onChunkTick(chunk.getPos().toLong(), (ServerLevel) (Object) this);
    }
}
