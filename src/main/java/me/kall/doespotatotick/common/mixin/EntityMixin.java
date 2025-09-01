package me.kall.doespotatotick.common.mixin;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.common.api.Tickable;
import me.kall.doespotatotick.common.config.PotatoConfig;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements Tickable {
    @Shadow public abstract net.minecraft.world.level.Level level();
    @Unique private int client$tickCount;
    @Unique private volatile boolean client$isTickable;

    @Override
    public boolean doesPotatoTick$isTickable() {
        return this.client$isTickable;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onClientTick(CallbackInfo ci) {
        if (this.level().isClientSide()) {
            this.client$tickCount++;
            int interval = PotatoConfig.ENTITY_TICKABLE_REFRESH_INTERVAL.get();
            if (this.client$tickCount >= interval) {
                this.client$tickCount = 0;

                this.client$isTickable = DoesPotatoTick.isTickable((Entity) (Object) this);
            }
        }
    }
}
