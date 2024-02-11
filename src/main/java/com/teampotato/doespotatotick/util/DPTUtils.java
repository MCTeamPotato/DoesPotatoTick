package com.teampotato.doespotatotick.util;

import com.teampotato.doespotatotick.DoesPotatoTick;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class DPTUtils {
    private static Identifier getRegistryName(EntityType<? extends Entity> type){
        return Registry.ENTITY_TYPE.getId(type);
    }

    public static boolean isInClaimedChunk(World world, BlockPos pos) {
        if (DoesPotatoTick.chunkClaimProvider == null) return false;
        return DoesPotatoTick.chunkClaimProvider.isInClaimedChunk(world, pos);
    }

    public static boolean isNearPlayer(World world, BlockPos pos, int maxHeight, int maxDistanceSquare) {
        return world.getPlayers().stream().anyMatch(player -> {
            if (Math.abs(player.getY() - pos.getY()) < maxHeight) {
                double x = player.getX() - pos.getX();
                double z = player.getZ() - pos.getZ();
                return (x * x + z * z) < maxDistanceSquare;
            }
            return false;
        });
    }

    public static boolean shouldHandleGuardEntityTick(Entity entity) {
        BlockPos pos = entity.getBlockPos();
        int horizontal = DoesPotatoTick.LIVING_ENTITIES_HORIZONTAL_TICK_DISTANCE.get();
        int vertical = DoesPotatoTick.LIVING_ENTITIES_VERTICAL_TICK_DISTANCE.get();
        if (isInClaimedChunk(entity.world, pos) || entity instanceof PlayerEntity) return true;
        Identifier name = getRegistryName(entity.getType());
        String regName = name.toString();
        if (entity instanceof ItemEntity) {
            if (!DoesPotatoTick.OPTIMIZE_ITEM_MOVEMENT.get() || DoesPotatoTick.ITEM_LIST.get().contains(regName)) return true;
            return (entity.world.random.nextInt(3) > 1);
        }
        return !(entity instanceof LivingEntity) ||
                (DoesPotatoTick.IGNORE_DEAD_ENTITIES.get() && ((LivingEntity) entity).isDead()) ||
                isNearPlayer(entity.world, pos, vertical, horizontal * horizontal) ||
                DoesPotatoTick.ENTITIES_WHITELIST.get().contains(regName) ||
                DoesPotatoTick.ENTITIES_MOD_ID_LIST.get().contains(name.getNamespace());
    }
}
