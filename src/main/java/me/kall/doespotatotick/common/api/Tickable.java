package me.kall.doespotatotick.common.api;

public interface Tickable {
    boolean doesPotatoTick$isRenderable();

    interface EntityType {
        boolean doesPotatoTick$shouldAlwaysTick();
        void doesPotatoTick$setShouldAlwaysTick();
        boolean doesPotatoTick$shouldAlwaysTickInRaid();
        void doesPotatoTick$setShouldAlwaysTickInRaid();
    }

    interface Level {
        boolean doesPotatoTick$isInOptimizableDimension();
        void doesPotatoTick$setIsInOptimizableDimension();
    }
}
