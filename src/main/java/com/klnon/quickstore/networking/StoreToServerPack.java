package com.klnon.quickstore.networking;


import com.klnon.quickstore.utils.Utils_Server;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class StoreToServerPack {
    private final ItemStack item;

    public StoreToServerPack(ItemStack item) {
        this.item = item;
    }


    public static void encode(StoreToServerPack searchChestsPack, PacketBuffer buf) {
            buf.writeItemStack(searchChestsPack.item);
    }


    public static StoreToServerPack decode(PacketBuffer buf) {
        return new StoreToServerPack(buf.readItemStack());
    }

    public static void handler(StoreToServerPack searchChestsPack, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(Utils_Server.getItems().size()==0)
                Utils_Server.getItems().add(searchChestsPack.item);
            for (ItemStack itemStack: Utils_Server.getItems()) {
                if(Objects.equals(searchChestsPack.item.getItem().getRegistryName(), itemStack.getItem().getRegistryName())){
                    break;
                }else {
                    Utils_Server.getItems().add(searchChestsPack.item);
                }
            }
        });
    }
}
