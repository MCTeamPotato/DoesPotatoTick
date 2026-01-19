package me.kall.doespotatotick.ext;

public interface Tickable {
    boolean dpt$tickable();
    void dpt$setTickable(boolean tickable);

    boolean dpt$alwaysTick();
    void dpt$setAlwaysTick(boolean alwaysTick);
    boolean dpt$checkAlwaysTick();

    interface Level {
        boolean dpt$valid();
        void dpt$setValid(boolean valid);

        boolean dpt$hasRaids();
    }
}
