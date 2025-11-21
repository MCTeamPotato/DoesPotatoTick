package me.kall.doespotatotick.mixin.impl;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.ext.Tickable;
import me.kall.doespotatotick.network.TickablePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements Tickable {
    @Unique private volatile boolean dpt$tickable = true;
    @Unique private boolean dpt$alwaysTick;

    @Override
    public boolean dpt$tickable() {
        return this.dpt$tickable;
    }

    @Override
    public void dpt$setTickable(boolean tickable) {
        if (this.dpt$tickable == tickable) return;

        this.dpt$tickable = tickable;
        Entity entity = (Entity) (Object) this;
        DoesPotatoTick.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new TickablePacket(tickable, entity.getId()));
    }

    @Override
    public boolean dpt$alwaysTick() {
        return this.dpt$alwaysTick;
    }

    @Override
    public void dpt$setAlwaysTick(boolean alwaysTick) {
        this.dpt$alwaysTick = alwaysTick;
    }

    @Override
    public boolean dpt$checkAlwaysTick() {
        Entity entity = (Entity) (Object) this;
        if (!ConfigConstants.entityOptimizable) return true;
        if (entity.isAlwaysTicking()) return true;
        if (entity instanceof FallingBlockEntity) return true;

        if (ConfigConstants.onlyLiving && !(entity instanceof LivingEntity)) return true;
        if (ConfigConstants.onlyAnimals && !(entity instanceof Animal)) return true;
        if (ConfigConstants.onlyEnemies && !(entity instanceof Enemy)) return true;

        if (entity instanceof Projectile && ConfigConstants.ignoreProjectiles) return true;
        if (entity instanceof ItemEntity && ConfigConstants.ignoreItems) return true;
        if (entity instanceof Enemy && ConfigConstants.ignoreEnemies) return true;
        if (entity instanceof Animal && ConfigConstants.ignoreAnimals) return true;

        return ((EntityType)entity.getType()).dpt$alwaysTick();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.dpt$setAlwaysTick(this.dpt$checkAlwaysTick());
    }

    @Inject(method = "setPosRaw", at = @At("HEAD")) protected void beforePosChange(CallbackInfo ci) {}
    @Inject(method = "setPosRaw", at = @At("TAIL")) protected void afterPosChange(CallbackInfo ci) {}
}