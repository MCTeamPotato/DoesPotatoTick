package me.kall.doespotatotick.network;

import me.kall.doespotatotick.ext.Tickable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class TickablePacket {
    private final boolean tickable;
    private final int id;

    public TickablePacket(boolean tickable, int id) {
        this.tickable = tickable;
        this.id = id;
    }

    public TickablePacket(@NotNull FriendlyByteBuf buf) {
        this.tickable = buf.readBoolean();
        this.id = buf.readInt();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeBoolean(this.tickable);
        buf.writeInt(this.id);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Optional.ofNullable(Minecraft.getInstance().level).flatMap(level -> Optional.ofNullable(level.getEntity(this.id))).ifPresent(entity -> ((Tickable) entity).dpt$setTickable(this.tickable)));
        ctx.get().setPacketHandled(true);
    }
}
