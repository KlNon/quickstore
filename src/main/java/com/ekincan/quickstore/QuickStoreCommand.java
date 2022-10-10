package com.ekincan.quickstore;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class QuickStoreCommand extends CommandBase {
    public String getName() {
        return "quickstore";
    }

    public String getUsage(ICommandSender sender) {
        return "Will sort your items into nearby chests.";
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
        for (int inventorySlot = 0; inventorySlot < playerInventorySize; inventorySlot++) {
            ItemStack playersItemStack = inventoryPlayer.getStackInSlot(inventorySlot);
            //TODO getCount可能有误
            if (playersItemStack != null && playersItemStack.getMaxStackSize() > 1 && playersItemStack.getCount() > 0 && Item.getIdFromItem(playersItemStack.getItem()) != 0 && ((!(playersItemStack.getItem() instanceof net.minecraft.item.ItemFood) && Item.getIdFromItem(playersItemStack.getItem()) != 50) || inventorySlot > 8)) {
                Item playersItem = playersItemStack.getItem();
                for (ContainerInformation ci : containers) {
                    boolean containsItem = false;
                    boolean itemCompletlyAdded = false;
                    IInventory freeSlotInventory = null;
                    int freeSlotIndex = -1;
                    for (IInventory containerInventory : ci.getInventories()) {
                        int containerSize = containerInventory.getSizeInventory();
                        for (int containerSlot = 0; containerSlot < containerSize; containerSlot++) {
                            if (freeSlotInventory != null && itemCompletlyAdded == true)
                                break;
                            ItemStack containerStack = containerInventory.getStackInSlot(containerSlot);
                            if (containerStack == null || Item.getIdFromItem(containerStack.getItem()) == 0) {
                                if (freeSlotInventory == null) {
                                    freeSlotInventory = containerInventory;
                                    freeSlotIndex = containerSlot;
                                }
                            } else if (ItemStack.areItemsEqual(playersItemStack, containerStack) && containerSlot != ci.ignoredSlot) {
                                if (containerSize > 8)
                                    containsItem = true;
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
                    }
                    if (containsItem && !itemCompletlyAdded && freeSlotInventory != null)
                        //TODO 可能有误getStackInSlot
                        freeSlotInventory.setInventorySlotContents(freeSlotIndex, inventoryPlayer.getStackInSlot(inventorySlot));
                }
            }
        }
        player.inventoryContainer.detectAndSendChanges();
    }
}
