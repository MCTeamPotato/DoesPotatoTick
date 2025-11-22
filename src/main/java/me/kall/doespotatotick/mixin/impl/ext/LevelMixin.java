package me.kall.doespotatotick.mixin.impl.ext;

import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.ext.Tickable;
import me.kall.doespotatotick.mixin.access.RaidsAccessor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public abstract class LevelMixin implements Tickable.Level {
    @Shadow public abstract ResourceKey<Level> dimension();

    @Unique private boolean dpt$valid;

    @Override
    public boolean dpt$valid() {
        return this.dpt$valid;
    }

    @Override
    public void dpt$setValid(boolean valid) {
        this.dpt$valid = valid;
    }

    @Override
    public boolean dpt$hasRaids() {
        Level level = (Level) (Object) this;
        return !(level instanceof ServerLevel) || !((RaidsAccessor) ((ServerLevel)level).getRaids()).dpt$getRaids().isEmpty();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.dpt$setValid(ConfigConstants.validDimensions.isEmpty() || ConfigConstants.validDimensions.contains(this.dimension().location()));
    }
}
