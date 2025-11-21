package me.kall.doespotatotick.mixin.access;

import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Raids.class)
public interface RaidsAccessor {
    @Accessor("raidMap")
    Map<Integer, Raid> dpt$getRaids();
}
