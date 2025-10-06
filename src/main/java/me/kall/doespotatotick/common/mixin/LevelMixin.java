package me.kall.doespotatotick.common.mixin;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.common.config.PotatoConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(value = Level.class, priority = 2000)
public abstract class LevelMixin {
    @Inject(method = "guardEntityTick", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void onEntityTick(Consumer<T> consumerEntity, T entity, CallbackInfo ci) {
        Level level = (Level) (Object) this;
        if (PotatoConfig.ONLY_WORKS_ON_SERVER_THREAD.get()) {
            if (!(level instanceof ServerLevel)) return;
            if (!((ServerLevel)level).getServer().isSameThread()) return;
        }
        if (DoesPotatoTick.isTickable(entity)) return;
        ci.cancel();
    }
}
