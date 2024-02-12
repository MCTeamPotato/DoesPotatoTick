package com.teampotato.doespotatotick.mixin;

import com.teampotato.doespotatotick.DoesPotatoTick;
import com.teampotato.doespotatotick.api.Tickable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(value = Level.class, priority = 2000)
public abstract class WorldMixin implements Tickable.Level {
    @Unique private boolean doespotatotick$isInOptimizableDimension;

    @Override
    public boolean doespotatotick$isInOptimizableDimension() {
        return this.doespotatotick$isInOptimizableDimension;
    }

    @Override
    public void doespotatotick$setIsInOptimizableDimension() {
        this.doespotatotick$isInOptimizableDimension = true;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(WritableLevelData levelData, ResourceKey<Level> dimension, DimensionType dimensionType, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, CallbackInfo ci) {
        if (dimension == null) return;
        if (DoesPotatoTick.DIMENSION_WHITELIST.get().contains(dimension.location().toString())) this.doespotatotick$setIsInOptimizableDimension();
    }

    @Inject(method = "guardEntityTick", at = @At("HEAD"), cancellable = true)
    private void optimizeEntitiesTick(Consumer<Entity> consumerEntity, Entity entity, CallbackInfo ci) {
        if (!DoesPotatoTick.isTickable(entity)) ci.cancel();
    }
}
