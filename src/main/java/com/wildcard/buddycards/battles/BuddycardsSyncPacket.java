package com.wildcard.buddycards.battles;

import com.wildcard.buddycards.menu.PlaymatMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class BuddycardsSyncPacket {

    private final ListTag data;

    public BuddycardsSyncPacket(ListTag data) {
        this.data = data;
    }

    public void write(FriendlyByteBuf buffer) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("data", data);
        buffer.writeNbt(nbt);
    }

    public static BuddycardsSyncPacket read(FriendlyByteBuf buffer) {
        return new BuddycardsSyncPacket(buffer.readNbt().getList("data", Tag.TAG_COMPOUND));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientClassLoadingProtection.handlePacket(this));
        ctx.get().setPacketHandled(true);
    }

    private static final class ClientClassLoadingProtection {
        private static void handlePacket(BuddycardsSyncPacket packet) {
            Optional.ofNullable(Minecraft.getInstance().player).map(player -> player.containerMenu).ifPresent(menu -> {
                if (menu instanceof PlaymatMenu playmatMenu) {
                    playmatMenu.consumeSync(packet.data);
                }
            });
        }
    }
}
