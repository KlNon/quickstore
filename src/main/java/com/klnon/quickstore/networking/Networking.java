package com.klnon.quickstore.networking;

import com.klnon.quickstore.utils.Utils_Server;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "ABSENT \uD83E\uDD14";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Utils_Server.MOD_ID, "block_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(StoredChestsPack.class, nextID())
                .encoder(StoredChestsPack::encode)
                .decoder(StoredChestsPack::decode)
                .consumer(StoredChestsPack::handler)
                .add();
    }
}