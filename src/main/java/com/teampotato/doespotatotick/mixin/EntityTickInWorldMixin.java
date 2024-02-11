package com.teampotato.doespotatotick.mixin;

import com.teampotato.doespotatotick.util.DPTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public class EntityTickInWorldMixin {
    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private <T extends Entity> void onEntityTick(Consumer<T> consumer, T entity, CallbackInfo ci) {
        if (DPTUtils.shouldHandleGuardEntityTick(entity)) return;
        ci.cancel();
    }
}
