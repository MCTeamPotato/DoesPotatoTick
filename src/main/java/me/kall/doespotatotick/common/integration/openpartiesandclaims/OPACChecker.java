package me.kall.doespotatotick.common.integration.openpartiesandclaims;

import me.kall.doespotatotick.common.integration.IChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.common.server.api.OpenPACServerAPI;

public class OPACChecker implements IChecker {
    @Override
    public boolean isClaimed(Level level, BlockPos pos) {
        if (level.isClientSide()) return OpenPACClientAPI.get().getClaimsManager().get(level.dimension().location(), pos) != null;
        if (level.getServer() != null) return OpenPACServerAPI.get(level.getServer()).getServerClaimsManager().get(level.dimension().location(), pos) != null;
        return false;
    }
}
