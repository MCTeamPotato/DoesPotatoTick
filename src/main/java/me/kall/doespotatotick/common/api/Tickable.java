package me.kall.doespotatotick.common.api;

public interface Tickable {
    boolean doesPotatoTick$isTickable();

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
