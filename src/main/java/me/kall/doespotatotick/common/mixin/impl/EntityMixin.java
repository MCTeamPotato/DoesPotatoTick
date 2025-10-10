package me.kall.doespotatotick.common.mixin.impl;

import me.kall.doespotatotick.common.api.Tickable;
import me.kall.doespotatotick.common.config.PotatoConfig;
import me.kall.doespotatotick.common.data.PlayerTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements Tickable {
    @Shadow private net.minecraft.world.level.Level level;
    @Unique
    private volatile boolean dpt$tickable = true;

    @Override
    public boolean dpt$tickable() {
        return this.dpt$tickable;
    }

    @Override
    public void dpt$setTickable(boolean tickable) {
        this.dpt$tickable = tickable;
    }

    @Inject(method = "setPosRaw", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;<init>(Lnet/minecraft/core/BlockPos;)V"))
    private void onChunkUpdate(double x, double y, double z, CallbackInfo ci) {
        net.minecraft.world.level.Level level = this.level;
        if (!PotatoConfig.threadSupported(level)) return;

        Entity entity = (Entity) (Object) this;

        if (entity instanceof Player) {
            PlayerTracker.UPDATE_REQUIRED.add(level.dimension().location());
        }
    }
}
