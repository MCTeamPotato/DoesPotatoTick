package me.kall.doespotatotick.ext;

public interface Tickable {
    boolean dpt$tickable();
    void dpt$setTickable(boolean tickable);

    boolean dpt$alwaysTick();
    void dpt$setAlwaysTick(boolean alwaysTick);
    boolean dpt$checkAlwaysTick();

    interface EntityType {
        boolean dpt$alwaysTick();
        void dpt$setAlwaysTick(boolean tick);

        boolean dpt$raidTick();
        void dpt$setRaidTick(boolean tick);
    }

    interface Level {
        boolean dpt$valid();
        void dpt$setValid(boolean valid);

        boolean dpt$hasRaids();
    }
}
