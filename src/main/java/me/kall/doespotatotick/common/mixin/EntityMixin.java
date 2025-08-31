package me.kall.doespotatotick.common.mixin;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.common.api.Tickable;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin implements Tickable {
    @Override
    public boolean doesPotatoTick$isTickable() {
        return DoesPotatoTick.isTickable((Entity) (Object) this);
    }
}
