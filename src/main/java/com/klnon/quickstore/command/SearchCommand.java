package com.klnon.quickstore.command;


import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.config.StoreConfig_Client;
import com.klnon.quickstore.config.StoreConfig_Server;
import com.klnon.quickstore.gui.model.ItemStore;
import com.klnon.quickstore.gui.render.RenderBlockProps;
import com.klnon.quickstore.model.ContainerInformation;
import com.klnon.quickstore.model.ItemInfo;
import com.klnon.quickstore.networking.Networking;
import com.klnon.quickstore.networking.SearchChestsPack;
import com.klnon.quickstore.networking.StoredChestsPack;
import com.klnon.quickstore.utils.Utils_Server;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public class SearchCommand implements Command<CommandSource> {


    public static SearchCommand instance = new SearchCommand();

    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        List<ItemStack> items= Utils_Server.getItems();
        List<ContainerInformation> containers = Utils_Server.getNearbyContainers(player,Utils_Server.distanceList[StoreConfig_Server.general.searchDistance.get()]);

        //遍历玩家背包
        for (ItemStack itemStacks : items) {
                //遍历附近的箱子
                for (ContainerInformation ci : containers) {

                    IInventory[] inventories = ci.getInventories();
                    //避免列表为空
                    if (inventories == null)
                        continue;

                    IInventory containerInventory = inventories[0];
                    int containerSize = containerInventory.getSizeInventory();
                    //遍历箱子的物品
                    for (int containerSlot = 0; containerSlot < containerSize; containerSlot++) {
                        ItemStack containerStack = containerInventory.getStackInSlot(containerSlot);
                        if (ItemStack.areItemsEqual(itemStacks, containerStack)) {
                            if(!Utils_Server.searchList.contains(new RenderBlockProps(ci.getPos(),StoreConfig_Client.general.YELLOW.get())))
                                Utils_Server.searchList.add(new RenderBlockProps(ci.getPos(), StoreConfig_Client.general.YELLOW.get()));
                            }
                    }
                }
        }
        //发包给客户端,渲染相关箱子
        if(!player.world.isRemote)
            for (RenderBlockProps blockProps : Utils_Server.searchList) {
                Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(
                                () ->  player
                        ),
                        new SearchChestsPack(blockProps)
                );
            }
        Utils_Server.searchList.clear();
        Utils_Server.getItems().clear();

        return Command.SINGLE_SUCCESS;
    }
}
