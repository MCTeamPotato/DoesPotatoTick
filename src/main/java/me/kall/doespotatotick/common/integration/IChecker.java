package me.kall.doespotatotick.common.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IChecker {
    boolean isClaimed(Level level, BlockPos pos);
}
