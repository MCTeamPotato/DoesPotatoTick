package me.kall.doespotatotick.integration;

import me.kall.doespotatotick.integration.ftb.FTBChecker;
import me.kall.doespotatotick.integration.opac.OPACChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Nullable;

public class ClaimManager {
    public static final boolean FTB_LOADED = FMLLoader.getLoadingModList().getModFileById("ftbchunks") != null;
    public static final boolean OPAC_LOADED = FMLLoader.getLoadingModList().getModFileById("openpartiesandclaims") != null;
    private static final @Nullable IChecker FTB = FTB_LOADED ? new FTBChecker() : null;
    private static final @Nullable IChecker OPAC = OPAC_LOADED ? new OPACChecker() : null;

    public static boolean isClaimedChunk(ServerLevel level, BlockPos pos) {
        return (FTB != null && FTB.isClaimed(level, pos)) || (OPAC != null && OPAC.isClaimed(level, pos));
    }
}
