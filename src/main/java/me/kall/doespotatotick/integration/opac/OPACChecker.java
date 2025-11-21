package me.kall.doespotatotick.integration.opac;

import me.kall.doespotatotick.integration.IChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import xaero.pac.common.server.api.OpenPACServerAPI;

public class OPACChecker implements IChecker {
    @Override
    public boolean isClaimed(@NotNull ServerLevel level, BlockPos pos) {
        return OpenPACServerAPI.get(level.getServer()).getServerClaimsManager().get(level.dimension().location(), pos) != null;
    }
}
