package com.klnon.quickstore.networking;

import com.klnon.quickstore.gui.render.RenderBlockProps;
import com.klnon.quickstore.utils.Utils_Server;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SearchChestsPack {
    private final RenderBlockProps blockProps;

    public SearchChestsPack(RenderBlockProps blockProps) {
        this.blockProps = blockProps;
    }

    public static void encode(SearchChestsPack searchChestsPack, PacketBuffer buf) {
        buf.writeBlockPos(searchChestsPack.blockProps.getPos());
        buf.writeInt(searchChestsPack.blockProps.getColor());
    }

    public static SearchChestsPack decode(PacketBuffer buf) {
        return new SearchChestsPack(new RenderBlockProps(buf.readBlockPos(),buf.readInt()));
    }

    public static void handler(SearchChestsPack searchChestsPack, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Utils_Server.searchList.add(searchChestsPack.blockProps);
        });
        ctx.get().setPacketHandled(true);
    }
}
