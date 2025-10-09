package me.kall.doespotatotick.common.mixin.impl;

import me.kall.doespotatotick.common.api.IRaids;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Raids.class)
public abstract class RaidsMixin implements IRaids {
    @Shadow @Final private Map<Integer, Raid> raidMap;

    @Override
    public boolean dpt$hasRaid() {
        return !this.raidMap.isEmpty();
    }
}
