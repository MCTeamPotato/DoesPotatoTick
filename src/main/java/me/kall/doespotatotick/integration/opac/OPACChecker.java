package me.kall.doespotatotick.integration.opac;

import me.kall.doespotatotick.integration.IChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class OPACChecker implements IChecker {
    @Override
    public boolean isClaimed(@NotNull ServerLevel level, BlockPos pos) {
        return false;
    }
}
