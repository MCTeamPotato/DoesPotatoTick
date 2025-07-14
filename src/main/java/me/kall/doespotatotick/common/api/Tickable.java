package me.kall.doespotatotick.common.api;

public final class Tickable {
    public interface EntityType {
        boolean doesPotatoTick$shouldAlwaysTick();
        void doesPotatoTick$setShouldAlwaysTick();
        boolean doesPotatoTick$shouldAlwaysTickInRaid();
        void doesPotatoTick$setShouldAlwaysTickInRaid();
    }

    public interface Level {
        boolean doesPotatoTick$isInOptimizableDimension();
        void doesPotatoTick$setIsInOptimizableDimension();
    }
}
