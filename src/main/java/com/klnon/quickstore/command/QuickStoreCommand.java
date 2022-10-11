package com.klnon.quickstore.command;

import java.util.*;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.config.StoreConfig;
import com.klnon.quickstore.container.ContainerInformation;
import com.klnon.quickstore.utils.Utils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

public class QuickStoreCommand implements Command<CommandSource> {

//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//
//    }
    public static QuickStoreCommand instance = new QuickStoreCommand();
    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        //TODO asPlayer()可能BUG
        PlayerEntity player = context.getSource().asPlayer();
        List<ContainerInformation> containers = Utils.getNearbyContainers(player, 2.0F);
        PlayerInventory inventoryPlayer = player.inventory;
        int playerInventorySize = inventoryPlayer.getSizeInventory();
        List<String> BanItems = Arrays.asList(StoreConfig.BanItems);
        List<String> itemSlotBan = Arrays.asList(StoreConfig.itemSlotBan);
        Map<String,Integer> map=QuickStore.storedItems;
        for (int inventorySlot = 0; inventorySlot < playerInventorySize; inventorySlot++) {
            ItemStack playersItemStack = inventoryPlayer.getStackInSlot(inventorySlot);

            if (BanItems.contains(Objects.requireNonNull(playersItemStack.getItem().getRegistryName()).toString())) {
                continue;
            }
            if (!(!((playersItemStack.getItem().getFood() != null) && itemSlotBan.contains(playersItemStack.getItem().getRegistryName().toString())) || inventorySlot > StoreConfig.itemSlot))
                continue;
            //玩家背包不为空,这个在玩家背包的物品堆叠最大数量大于1,数量大于0, 不为在物品栏的食物和火把
            if (!playersItemStack.isEmpty() && (playersItemStack.getMaxStackSize() > 1 || StoreConfig.singleEnable)
                    && playersItemStack.getCount() > 0
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
                                int old_playerCount=playersItemStack.getCount();
                                playersItemStack.setCount(playersItemStack.getCount() - removedCountFromPlayer);
                                int playerCount=playersItemStack.getCount();
                                //统计贮藏物品,不统计空气
                                if (StoreConfig.detailInfoEnable) {
                                    String displayName=playersItemStack.getDisplayName().toString();
                                    if (map.containsKey(displayName)) {
                                        map.put(displayName, map.get(displayName)+old_playerCount-playerCount);
                                    } else {
                                        map.put(displayName, old_playerCount-playerCount);
                                    }
                                }
                                if (playersItemStack.getCount() <= 0)
                                    itemCompletlyAdded = true;
                            }
                        }
                    }
                    //如果箱子没有空间则提示
                    if (freeSlotInventory == null && StoreConfig.fullInfoEnable && !ci.isFull) {
                        ci.isFull = true;
                        BlockPos pos = ci.chest1.getPos();
                        context.getSource().sendFeedback(new TranslationTextComponent("commands.quickstore.nospace", pos.getX(), pos.getY(), pos.getZ()), false);
                    }
                    //
                    if (containsItem && !itemCompletlyAdded && freeSlotInventory != null) {
                        freeSlotInventory.setInventorySlotContents(freeSlotIndex, inventoryPlayer.getStackInSlot(inventorySlot));
                        if (StoreConfig.detailInfoEnable) {
                            String displayName=playersItemStack.getDisplayName().toString();
                            if (map.containsKey(displayName)) {
                                map.put(displayName, map.get(displayName)+inventoryPlayer.getStackInSlot(inventorySlot).getCount());
                            } else {
                                map.put(displayName, inventoryPlayer.getStackInSlot(inventorySlot).getCount());
                            }
                        }
                        inventoryPlayer.setInventorySlotContents(inventorySlot, ItemStack.EMPTY);
                    }
                    //TODO 同步物品栏
                }
            }
        }
        //TODO 或者这里同步物品栏
//        player.inventoryContainer.detectAndSendChanges();
        return 0;
    }
}
