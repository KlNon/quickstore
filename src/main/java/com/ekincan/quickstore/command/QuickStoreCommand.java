package com.ekincan.quickstore.command;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ekincan.quickstore.QuickStore;
import com.ekincan.quickstore.config.StoreConfig;
import com.ekincan.quickstore.container.ContainerInformation;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class QuickStoreCommand extends CommandBase {
    public String getName() {
        return "quickstore";
    }

    public String getUsage(ICommandSender sender) {
        return "commands.quickstore.des";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
        List<ContainerInformation> containers = QuickStore.getNearbyContainers(player, 2.0F);
        InventoryPlayer inventoryPlayer = player.inventory;
        int playerInventorySize = inventoryPlayer.getSizeInventory();
        List<String> BanItems = Arrays.asList(StoreConfig.BanItems);
        List<String> itemSlotBan = Arrays.asList(StoreConfig.itemSlotBan);
        for (int inventorySlot = 0; inventorySlot < playerInventorySize; inventorySlot++) {
            ItemStack playersItemStack = inventoryPlayer.getStackInSlot(inventorySlot);

            if(BanItems.contains(Objects.requireNonNull(playersItemStack.getItem().getRegistryName()).toString())){
                continue;
            }
            //玩家背包不为空,这个在玩家背包的物品堆叠最大数量大于1,数量大于0, 不为在物品栏的食物和火把
            if (!playersItemStack.isEmpty() && (playersItemStack.getMaxStackSize() > 1 || StoreConfig.singleEnable)
                    && playersItemStack.getCount() > 0
                    && ((!(playersItemStack.getItem() instanceof net.minecraft.item.ItemFood) && (itemSlotBan.contains(playersItemStack.getItem().getRegistryName().toString()) || inventorySlot > StoreConfig.itemSlot)))
                    && inventorySlot > StoreConfig.slot) {
                for (ContainerInformation ci : containers) {
                    boolean containsItem = false;
                    boolean itemCompletlyAdded = false;
                    IInventory freeSlotInventory = null;
                    int freeSlotIndex = -1;
                    for (IInventory containerInventory : ci.getInventories()) {
                        int containerSize = containerInventory.getSizeInventory();
                        for (int containerSlot = 0; containerSlot < containerSize; containerSlot++) {
                            //判断是否有空位
                            if (freeSlotInventory != null && itemCompletlyAdded)
                                break;
                            ItemStack containerStack = containerInventory.getStackInSlot(containerSlot);
                            if (containerStack.isEmpty()) {
                                if (freeSlotInventory == null) {
                                    freeSlotInventory = containerInventory;
                                    freeSlotIndex = containerSlot;
                                }
                                //如果箱子里的物品(内循环)和身上物品(外循环)相同,且箱子格子不为2格
                            } else if (ItemStack.areItemsEqual(playersItemStack, containerStack) && containerSlot != ci.ignoredSlot) {
                                //箱子大小大于八格
                                if (containerSize > 8)
                                    containsItem = true;
                                //添加物品
                                int oldCountInContainer = containerStack.getCount();
                                int newCountInContainer = containerStack.getCount() + playersItemStack.getCount();
                                if (newCountInContainer > containerStack.getMaxStackSize())
                                    newCountInContainer = containerStack.getMaxStackSize();
                                int removedCountFromPlayer = newCountInContainer - oldCountInContainer;
                                containerStack.setCount(newCountInContainer);
                                playersItemStack.setCount(playersItemStack.getCount() - removedCountFromPlayer);
                                if (playersItemStack.getCount() <= 0)
                                    itemCompletlyAdded = true;
                            }
                        }
                        if(freeSlotInventory == null){
                            BlockPos pos=ci.blockPositionOfInventory.getPos();
                            QuickStore.player.sendMessage(new TextComponentTranslation("commands.quickstore.nospace",pos.getX(),pos.getY(),pos.getZ()));
                        }
                    }
                    if (containsItem && !itemCompletlyAdded && freeSlotInventory != null){
                        freeSlotInventory.setInventorySlotContents(freeSlotIndex, inventoryPlayer.getStackInSlot(inventorySlot));
                        inventoryPlayer.setInventorySlotContents(inventorySlot, ItemStack.EMPTY);
                    }
                }
            }
        }
        player.inventoryContainer.detectAndSendChanges();
    }
}
