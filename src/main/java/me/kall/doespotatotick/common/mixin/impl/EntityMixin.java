package me.kall.doespotatotick.common.mixin.impl;

import me.kall.doespotatotick.common.api.Tickable;
import me.kall.doespotatotick.common.config.PotatoConfig;
import me.kall.doespotatotick.common.data.PlayerTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements Tickable {
    @Shadow public abstract BlockPos blockPosition();

    @Unique
    private volatile boolean dpt$tickable = true;

    @Unique private int dpt$lastY = Integer.MAX_VALUE;

    @Override
    public boolean dpt$tickable() {
        return this.dpt$tickable;
    }

    @Override
    public void dpt$setTickable(boolean tickable) {
        this.dpt$tickable = tickable;
    }

    @Inject(method = "setPosRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;<init>(III)V", shift = At.Shift.AFTER))
    private void checkYChange(double x, double y, double z, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        net.minecraft.world.level.Level level = entity.level();
        ResourceLocation dim = level.dimension().location();
        if (PotatoConfig.threadSupported(level) && entity instanceof Player) {
            if (this.dpt$lastY == Integer.MAX_VALUE) {
                this.dpt$lastY = this.blockPosition().getY();
            } else {
                int moveDistY = Math.abs(this.dpt$lastY - this.blockPosition().getY());
                if (moveDistY >= 4) {
                    PlayerTracker.UPDATE_REQUIRED.add(dim);
                    this.dpt$lastY = this.blockPosition().getY();
                }
            }
        }
    }

    @Inject(method = "setPosRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;<init>(Lnet/minecraft/core/BlockPos;)V"))
    private void onCrossChunk(double x, double y, double z, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        net.minecraft.world.level.Level level = entity.level();
        ResourceLocation dim = level.dimension().location();
        if (PotatoConfig.threadSupported(level) && entity instanceof Player) {
            PlayerTracker.UPDATE_REQUIRED.add(dim);
        }
    }
}
