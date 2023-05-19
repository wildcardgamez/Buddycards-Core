package com.wildcard.buddycards.battles;

import com.wildcard.buddycards.Buddycards;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class BuddycardsPackets {
    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Buddycards.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        INSTANCE.registerMessage(id++, BuddycardsSyncPacket.class, BuddycardsSyncPacket::write, BuddycardsSyncPacket::read, BuddycardsSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
