package me.kall.doespotatotick.common.mixin.impl;

import me.kall.doespotatotick.common.api.Tickable;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class EntityMixin implements Tickable {
    @Unique
    private volatile boolean dpt$tickable = true;

    @Override
    public boolean dpt$tickable() {
        return this.dpt$tickable;
    }

    @Override
    public void dpt$setTickable(boolean tickable) {
        this.dpt$tickable = tickable;
    }
}
