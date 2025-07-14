package me.kall.doespotatotick.common.integration;

import me.kall.doespotatotick.common.integration.ftbchunks.FTBChecker;
import me.kall.doespotatotick.common.integration.openpartiesandclaims.OPACChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Nullable;

public class ClaimManager {
    public static final boolean FTB_CHUNKS = FMLLoader.getLoadingModList().getModFileById("ftbchunks") != null;
    public static final boolean OPEN_PARTIES_AND_CLAIMS = FMLLoader.getLoadingModList().getModFileById("openpartiesandclaims") != null;

    public static final @Nullable IChecker FTB_CHECKER = FTB_CHUNKS ? new FTBChecker() : null;
    public static final @Nullable IChecker OPAC_CHECKER = OPEN_PARTIES_AND_CLAIMS ? new OPACChecker() : null;

    public static boolean isClaimed(Level level, BlockPos pos) {
        return (FTB_CHECKER != null && FTB_CHECKER.isClaimed(level, pos)) || (OPAC_CHECKER != null && OPAC_CHECKER.isClaimed(level, pos));
    }
}
