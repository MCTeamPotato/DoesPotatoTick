package me.kall.doespotatotick.common.fps;

import me.kall.doespotatotick.common.config.PotatoConfig;

public class FpsViewer {
    private static int avgFps = 0;
    private static int interval = 0;
    private static volatile int candidate = 0;

    public static void updateFps(int fps) {
        avgFps = (avgFps + fps) / 2;
        interval++;
        if (interval == 100) {
            interval = 0;
            candidate = avgFps;
            avgFps = fps;
        }
    }

    public static int getAvgFps() {
        if (candidate == 0) return PotatoConfig.ENTITY_RENDERABLE_REFRESH_INTERVAL.get();
        return candidate;
    }
}
