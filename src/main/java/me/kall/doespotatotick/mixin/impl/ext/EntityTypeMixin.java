package me.kall.doespotatotick.mixin.impl.ext;

import me.kall.doespotatotick.ext.Tickable;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements Tickable.EntityType {
    @Unique private boolean dpt$alwaysTick, dpt$raidTick;

    @Override
    public boolean dpt$alwaysTick() {
        return this.dpt$alwaysTick;
    }

    @Override
    public void dpt$setAlwaysTick(boolean tick) {
        this.dpt$alwaysTick = tick;
    }

    @Override
    public boolean dpt$raidTick() {
        return this.dpt$raidTick;
    }

    @Override
    public void dpt$setRaidTick(boolean tick) {
        this.dpt$raidTick = tick;
    }
}
