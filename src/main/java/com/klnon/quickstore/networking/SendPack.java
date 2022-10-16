package com.klnon.quickstore.networking;


import java.util.function.Supplier;

import com.klnon.quickstore.gui.render.RenderBlockProps;
import com.klnon.quickstore.utils.Utils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SendPack {
    private final RenderBlockProps blockProps;

    public SendPack(RenderBlockProps blockProps) {
        this.blockProps = blockProps;
    }

    public static void encode(SendPack sendPack, PacketBuffer buf) {
        buf.writeBlockPos(sendPack.blockProps.getPos());
        buf.writeInt(sendPack.blockProps.getColor());
    }

    public static SendPack decode(PacketBuffer buf) {
        return new SendPack(new RenderBlockProps(buf.readBlockPos(),buf.readInt()));
    }

    public static void handler(SendPack sendPack,Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Utils.storedList.add(sendPack.blockProps);
        });
        ctx.get().setPacketHandled(true);
    }
}