package me.kall.doespotatotick.mixin.impl;

import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.ext.Tickable;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ProjectileMixin {
    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V"))
    private void hitEntity(HitResult result, CallbackInfo ci) {
        if (ConfigConstants.ignoreProjectileTargets) {
            ((Tickable)((EntityHitResult)result).getEntity()).dpt$setAlwaysTick(true);
        }
    }
}
