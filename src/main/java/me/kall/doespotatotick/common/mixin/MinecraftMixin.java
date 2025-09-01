package me.kall.doespotatotick.common.mixin;

import me.kall.doespotatotick.common.config.PotatoConfig;
import me.kall.doespotatotick.common.fps.FpsViewer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow private static int fps;

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;fps:I", ordinal = 0, shift = At.Shift.AFTER))
    private void updateFps(CallbackInfo ci) {
        if (PotatoConfig.USE_AVG_FPS_AS_REFRESH_INTERVAL.get()) {
            FpsViewer.updateFps(fps);
        }
    }
}
