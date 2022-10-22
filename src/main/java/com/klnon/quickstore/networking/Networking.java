package com.klnon.quickstore.networking;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.utils.Utils_Server;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "2.1.2";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(QuickStore.MOD_ID, "block_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(StoredChestsPack.class, nextID())
                .encoder(StoredChestsPack::encode)
                .decoder(StoredChestsPack::decode)
                .consumer(StoredChestsPack::handler)
                .add();


        INSTANCE.messageBuilder(SearchChestsPack.class, nextID())
                .encoder(SearchChestsPack::encode)
                .decoder(SearchChestsPack::decode)
                .consumer(SearchChestsPack::handler)
                .add();

        INSTANCE.messageBuilder(StoreToServerPack.class, nextID())
                .encoder(StoreToServerPack::encode)
                .decoder(StoreToServerPack::decode)
                .consumer(StoreToServerPack::handler)
                .add();
    }
}