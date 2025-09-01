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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements Tickable {
    @Shadow public abstract net.minecraft.world.level.Level level();
    @Unique private int client$tickCount;
    @Unique private volatile boolean client$isTickable;

    @Override
    public boolean doesPotatoTick$isTickable() {
        return this.client$isTickable;
    }

    @Inject(method = "shouldRender", at = @At("RETURN"))
    private void onClientTick(CallbackInfoReturnable<Boolean> cir) {
        if (this.level().isClientSide()) {
            this.client$tickCount++;
            int interval = PotatoConfig.ENTITY_RENDERABLE_REFRESH_INTERVAL.get();
            if (this.client$tickCount >= interval) {
                this.client$tickCount = 0;

                this.client$isTickable = DoesPotatoTick.isTickable((Entity) (Object) this);
            }
        }
    }
}
