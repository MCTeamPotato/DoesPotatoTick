package me.kall.doespotatotick.common.mixin;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.common.api.Tickable;
import me.kall.doespotatotick.common.config.PotatoConfig;
import me.kall.doespotatotick.common.fps.FpsViewer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements Tickable {
    @Unique private int client$renderCallCount;
    @Unique private volatile boolean client$isRenderable = true;

    @Override
    public boolean doesPotatoTick$isRenderable() {
        return this.client$isRenderable;
    }

    @Inject(method = "shouldRender", at = @At("RETURN"))
    private void onRenderCall(CallbackInfoReturnable<Boolean> cir) {
        if (PotatoConfig.STOP_RENDERING_SKIPPED_ENTITIES.get()) {
            this.client$renderCallCount++;
            int interval = FpsViewer.getAvgFps();
            if (this.client$renderCallCount >= interval) {
                this.client$renderCallCount = 0;
                this.client$isRenderable = DoesPotatoTick.isTickable((Entity) (Object) this);
            }
        }
    }
}
