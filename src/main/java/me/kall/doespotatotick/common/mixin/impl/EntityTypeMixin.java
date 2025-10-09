package me.kall.doespotatotick.common.mixin.impl;

import me.kall.doespotatotick.common.api.Tickable;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements Tickable.EntityType {
    @Unique private boolean dpt$alwaysTick, dpt$alwaysTickInRaid;

    @Override
    public boolean dpt$alwaysTick() {
        return this.dpt$alwaysTick;
    }

    @Override
    public void dpt$setAsAlwaysTick() {
        this.dpt$alwaysTick = true;
    }

    @Override
    public boolean dpt$alwaysTickInRaid() {
        return this.dpt$alwaysTickInRaid;
    }

    @Override
    public void dpt$setAlwaysTickInRaid() {
        this.dpt$alwaysTickInRaid = true;
    }
}
