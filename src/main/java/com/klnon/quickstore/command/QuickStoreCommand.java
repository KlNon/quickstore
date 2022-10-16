package com.klnon.quickstore.command;

import java.util.*;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.config.StoreConfig_Client;
import com.klnon.quickstore.config.StoreConfig_Server;
import com.klnon.quickstore.gui.render.RenderBlockProps;
import com.klnon.quickstore.model.ContainerInformation;
import com.klnon.quickstore.model.ItemInfo;
import com.klnon.quickstore.networking.Networking;
import com.klnon.quickstore.networking.StoredChestsPack;
import com.klnon.quickstore.utils.Utils_Server;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class QuickStoreCommand implements Command<CommandSource> {

    public static QuickStoreCommand instance = new QuickStoreCommand();

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Utils_Server.storedList.clear();

        ServerPlayerEntity player = context.getSource().asPlayer();
        List<ContainerInformation> containers = Utils_Server.getNearbyContainers(player);
        Utils_Server.setSPlayer(player);
        QuickStore.nearbyContainers = containers;
        PlayerInventory inventoryPlayer = player.inventory;
        int playerInventorySize = inventoryPlayer.getSizeInventory();

        List<String> BanItems = Collections.singletonList(StoreConfig_Server.general.BanItems.get());
        List<String> itemSlotBan = Collections.singletonList(StoreConfig_Server.general.itemSlotBan.get());

        //遍历玩家背包
        for (int inventorySlot = 0; inventorySlot < playerInventorySize; inventorySlot++) {
            //标记当前物品是否储存到下列箱子
            boolean isStore = false;

            ItemStack playersItemStack = inventoryPlayer.getStackInSlot(inventorySlot);

            if (BanItems.contains(Objects.requireNonNull(playersItemStack.getItem().getRegistryName()).toString()))
                continue;
            if (inventorySlot <= StoreConfig_Server.general.slot.get())
                continue;
            if (((playersItemStack.getItem().getFood() != null) || itemSlotBan.contains(playersItemStack.getItem().getRegistryName().toString())) && inventorySlot <= StoreConfig_Server.general.itemSlot.get())
                continue;

            //玩家背包不为空,这个在玩家背包的物品堆叠最大数量大于1,数量大于0, 不为在物品栏的食物和火把
            if (!playersItemStack.isEmpty()
                    && (playersItemStack.getMaxStackSize() > 1 || StoreConfig_Server.switches.singleEnable.get())
                    && playersItemStack.getCount() > 0
                    && inventorySlot > StoreConfig_Server.general.slot.get()) {

                //遍历附近的箱子
                for (ContainerInformation ci : containers) {
                    boolean containsItem = false;
                    boolean includeItem = false;
                    boolean itemCompletlyAdded = false;
                    IInventory freeSlotInventory = null;
                    int freeSlotIndex = -1;


                    //TODO 如果有大箱子,改成for (IInventory containerInventory : inventories)
                    IInventory[] inventories = ci.getInventories();
                    //避免列表为空
                    if (inventories == null)
                        continue;

                    IInventory containerInventory = inventories[0];
                    int containerSize = containerInventory.getSizeInventory();
                    //遍历箱子的物品
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
                        } else if (ItemStack.areItemsEqual(playersItemStack, containerStack)) {
                            //箱子大小大于12格
                            if (containerSize > 12)
                                containsItem = true;
                            //添加物品
                            int oldCountInContainer = containerStack.getCount();
                            int newCountInContainer = containerStack.getCount() + playersItemStack.getCount();
                            if (newCountInContainer > containerStack.getMaxStackSize())
                                newCountInContainer = containerStack.getMaxStackSize();
                            int removedCountFromPlayer = newCountInContainer - oldCountInContainer;
                            if (removedCountFromPlayer > 0)
                                isStore = true;
                            if(removedCountFromPlayer==0)
                                includeItem = true;
                            containerStack.setCount(newCountInContainer);
                            int old_playerCount = playersItemStack.getCount();

                            String displayName = playersItemStack.getTextComponent().getString();

                            playersItemStack.setCount(playersItemStack.getCount() - removedCountFromPlayer);

                            int playerCount = playersItemStack.getCount();
                            //箱子是否满了
                            ci.isFull= playerCount != 0;
                            //统计贮藏物品,不统计空气
                            if (StoreConfig_Client.switches.detailInfoEnable.get()) {
                                if (QuickStore.storedItems.containsKey(displayName)) {
                                    ItemInfo itemInfo = QuickStore.storedItems.get(displayName);
                                    itemInfo.setAmount(itemInfo.getAmount() + old_playerCount - playerCount);
                                    if (removedCountFromPlayer > 0) {
                                        addPos(ci, displayName, itemInfo);
                                    }
                                } else {
                                    if (removedCountFromPlayer > 0) {
                                        QuickStore.storedItems.put(displayName, new ItemInfo(addNewPos(ci), old_playerCount - playerCount));
                                    }
                                }
                            }
                            if (playersItemStack.getCount() <= 0) {
                                inventoryPlayer.removeStackFromSlot(inventorySlot);
                                itemCompletlyAdded = true;
                            }
                        }
                    }
                    //如果箱子没有空间则提示
                    if (includeItem && freeSlotInventory == null && StoreConfig_Client.switches.fullInfoEnable.get() && ci.isFull) {
                        BlockPos pos = ci.getPos();
//                        player.sendMessage(new TranslationTextComponent("commands.quickstore.nospace", pos.getX(), pos.getY(), pos.getZ()), player.getUniqueID());
                        Utils_Server.storedList.add(new RenderBlockProps(pos, StoreConfig_Client.general.RED.get()));
                    }
                    //
                    if (containsItem && !itemCompletlyAdded && freeSlotInventory != null) {
                        if (StoreConfig_Client.switches.detailInfoEnable.get() && !playersItemStack.isEmpty()) {
                            String displayName = playersItemStack.getTextComponent().getString();
                            if (QuickStore.storedItems.containsKey(displayName)) {
                                ItemInfo itemInfo = QuickStore.storedItems.get(displayName);
                                itemInfo.setAmount(itemInfo.getAmount() + inventoryPlayer.getStackInSlot(inventorySlot).getCount());
                                if (!isStore)
                                    addPos(ci, displayName, itemInfo);
                            } else {
                                if (!isStore)
                                    QuickStore.storedItems.put(displayName, new ItemInfo(addNewPos(ci), inventoryPlayer.getStackInSlot(inventorySlot).getCount()));
                            }
                        }
                        freeSlotInventory.setInventorySlotContents(freeSlotIndex, inventoryPlayer.getStackInSlot(inventorySlot));
                        inventoryPlayer.setInventorySlotContents(inventorySlot, ItemStack.EMPTY);
                        isStore = true;
                    }
                }
            }
        }
        //发包给客户端,渲染相关箱子
        if(!player.world.isRemote)
            for (RenderBlockProps blockProps : Utils_Server.storedList) {
            Networking.INSTANCE.send(
                    PacketDistributor.PLAYER.with(
                            () ->  player
                    ),
                    new StoredChestsPack(blockProps)
            );
        }

        commandFeedback(player);

        //TODO 或者这里同步物品栏
//        player.inventoryContainer.detectAndSendChanges();

        return Command.SINGLE_SUCCESS;
    }

    private void commandFeedback(ServerPlayerEntity player) {
        try {
            player.sendMessage(new TranslationTextComponent("commands.quickstore.containers", QuickStore.nearbyContainers.size()), player.getUniqueID());
            if (QuickStore.storedItems.size() <= 0) {
                player.sendMessage(new TranslationTextComponent("commands.quickstore.nostored"), player.getUniqueID());
            } else {
                player.sendMessage(new TranslationTextComponent("commands.quickstore.stored", (QuickStore.storedItems.size())), player.getUniqueID());
                if (StoreConfig_Client.switches.detailInfoEnable.get()) {
                    if (StoreConfig_Client.switches.showDetailChestsEnable.get()) {
                        for (Map.Entry<String, ItemInfo> entry : QuickStore.storedItems.entrySet())
                            player.sendMessage(new TranslationTextComponent("commands.quickstore.storeditems", entry.getKey(), entry.getValue().getAmount(), entry.getValue().posToString()), player.getUniqueID());
                    } else {
                        for (Map.Entry<String, ItemInfo> entry : QuickStore.storedItems.entrySet())
                            player.sendMessage(new TranslationTextComponent("commands.quickstore.storeditems2", entry.getKey(), entry.getValue().getAmount()), player.getUniqueID());

                    }
                }
                //TODO 添加声音
//                        (Minecraft.getInstance()).player.playSound(Objects.requireNonNull(SoundEvent.REGISTRY.getObjectById(76)), 1.0F, 2.0F);
            }
            if (Utils_Server.isQuickSeeActive())
                player.sendMessage(new TranslationTextComponent("commands.quickstore.see"), player.getUniqueID());
            QuickStore.storedItems.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPos(ContainerInformation ci, String displayName, ItemInfo itemInfo) {
        BlockPos pos = ci.getPos();
        if (!itemInfo.isSamePosition(pos))
            itemInfo.getPosition().add(pos);
        Utils_Server.storedList.add(new RenderBlockProps(ci.getPos(), StoreConfig_Client.general.GREEN.get()));
        QuickStore.storedItems.put(displayName, itemInfo);
    }

    private List<BlockPos> addNewPos(ContainerInformation ci) {
        List<BlockPos> blockPos = new ArrayList<>();
        BlockPos pos = ci.getPos();
        blockPos.add(pos);
        Utils_Server.storedList.add(new RenderBlockProps(ci.getPos(), StoreConfig_Client.general.GREEN.get()));
        return blockPos;
    }


}
