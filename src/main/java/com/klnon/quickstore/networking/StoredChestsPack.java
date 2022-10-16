package com.klnon.quickstore.networking;


import java.util.function.Supplier;

import com.klnon.quickstore.gui.render.RenderBlockProps;
import com.klnon.quickstore.utils.Utils_Server;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class StoredChestsPack {
    private final RenderBlockProps blockProps;

    public StoredChestsPack(RenderBlockProps blockProps) {
        this.blockProps = blockProps;
    }

    public static void encode(StoredChestsPack storedChestsPack, PacketBuffer buf) {
        buf.writeBlockPos(storedChestsPack.blockProps.getPos());
        buf.writeInt(storedChestsPack.blockProps.getColor());
    }

    public static StoredChestsPack decode(PacketBuffer buf) {
        return new StoredChestsPack(new RenderBlockProps(buf.readBlockPos(),buf.readInt()));
    }

    public static void handler(StoredChestsPack storedChestsPack, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Utils_Server.storedList.add(storedChestsPack.blockProps);
        });
        ctx.get().setPacketHandled(true);
    }
}