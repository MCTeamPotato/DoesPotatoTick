package me.kall.doespotatotick.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface IChecker {
    boolean isClaimed(ServerLevel level, BlockPos pos);
}