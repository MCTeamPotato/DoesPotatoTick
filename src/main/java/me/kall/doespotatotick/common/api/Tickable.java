package me.kall.doespotatotick.common.api;

public interface Tickable {
    boolean dpt$tickable();
    void dpt$setTickable(boolean renderable);

    boolean dpt$alwaysTick();

    interface EntityType {
        boolean dpt$alwaysTick();
        void dpt$setAsAlwaysTick();
        boolean dpt$alwaysTickInRaid();
        void dpt$setAlwaysTickInRaid();
    }

    interface Dim {
        boolean dpt$optimizableDim();
        void dpt$setAsOptimizable();
    }
}
