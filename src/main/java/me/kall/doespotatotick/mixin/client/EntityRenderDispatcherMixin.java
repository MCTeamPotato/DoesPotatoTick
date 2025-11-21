package me.kall.doespotatotick.mixin.client;

import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.ext.Tickable;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    private <E extends Entity> void onCheckRenderable(E entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (!ConfigConstants.skipRenderingUntickable) return;
        if (cir.getReturnValue() && !((Tickable)entity).dpt$tickable()) {
            cir.setReturnValue(false);
        }
    }
}