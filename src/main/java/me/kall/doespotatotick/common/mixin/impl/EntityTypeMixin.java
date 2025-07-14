package me.kall.doespotatotick.common.mixin.impl;

import me.kall.doespotatotick.common.api.Tickable;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements Tickable.EntityType {
    @Unique private boolean doesPotatoTick$shouldAlwaysTick, doesPotatoTick$shouldAlwaysTickInRaid;

    @Override
    public boolean doesPotatoTick$shouldAlwaysTick() {
        return this.doesPotatoTick$shouldAlwaysTick;
    }

    @Override
    public void doesPotatoTick$setShouldAlwaysTick() {
        this.doesPotatoTick$shouldAlwaysTick = true;
    }

    @Override
    public boolean doesPotatoTick$shouldAlwaysTickInRaid() {
        return this.doesPotatoTick$shouldAlwaysTickInRaid;
    }

    @Override
    public void doesPotatoTick$setShouldAlwaysTickInRaid() {
        this.doesPotatoTick$shouldAlwaysTickInRaid = true;
    }
}
