package me.kall.doespotatotick.common.api;

public interface Tickable {
    boolean dpt$tickable();
    void dpt$setTickable(boolean renderable);

    interface EntityType {
        boolean dpt$alwaysTick();
        void dpt$setAsAlwaysTick();
        boolean dpt$alwaysTickInRaid();
        void dpt$setAlwaysTickInRaid();
    }

    interface Level {
        boolean dpt$optimizableDim();
        void dpt$setAsOptimizable();
    }
}
