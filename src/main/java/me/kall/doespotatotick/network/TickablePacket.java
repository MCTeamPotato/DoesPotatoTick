package me.kall.doespotatotick.network;

import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.ext.Tickable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record TickablePacket(boolean tickable, int id) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, TickablePacket> CODEC = CustomPacketPayload.codec(TickablePacket::toBytes, TickablePacket::new);
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(DoesPotatoTick.MOD_ID, "tickable");
    public static final Type<TickablePacket> TYPE = new Type<>(ID);

    public TickablePacket(@NotNull FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readInt());
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeBoolean(this.tickable);
        buf.writeInt(this.id);
    }

    public static void handle(TickablePacket packet, @NotNull IPayloadContext ctx) {
        ctx.enqueueWork(() -> Optional.ofNullable(Minecraft.getInstance().level).flatMap(level -> Optional.ofNullable(level.getEntity(packet.id))).ifPresent(entity -> ((Tickable) entity).dpt$setTickable(packet.tickable)));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register(@NotNull RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(TYPE, CODEC, TickablePacket::handle);
    }

    public static void send(Entity entity, boolean tickable) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, new TickablePacket(tickable, entity.getId()));
    }
}
