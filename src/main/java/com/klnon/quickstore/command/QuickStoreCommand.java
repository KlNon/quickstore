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
import com.klnon.quickstore.utils.Utils_Client;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class QuickStoreCommand implements Command<CommandSource> {

    public static QuickStoreCommand instance = new QuickStoreCommand();

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Utils_Server.storedList.clear();

        ServerPlayerEntity player = context.getSource().asPlayer();
        List<ContainerInformation> containers = Utils_Server.getNearbyContainers(player, Utils_Server.distanceList[StoreConfig_Server.general.distance.get()]);
        Utils_Server.setSPlayer(player);
        QuickStore.nearbyContainers = containers;
        PlayerInventory inventoryPlayer = player.inventory;
        int playerInventorySize = inventoryPlayer.getSizeInventory();

        List<String> BanItems = Collections.singletonList(StoreConfig_Server.general.BanItems.get());
        List<String> itemSlotBan = Collections.singletonList(StoreConfig_Server.general.itemSlotBan.get());

        //遍历玩家背包
        for (int inventorySlot = 0; inventorySlot < playerInventorySize; inventorySlot++) {
            //标记当前物品是否储存到下列箱子
            boolean isStored = false;

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
                    if(isStored)
                        continue;
                    boolean includeItem = false;
                    boolean itemCompletlyAdded = false;                    //判断是否有空位或者物品已经添加
                    IInventory freeSlotInventory = null;
                    int freeSlotIndex = -1;


                    //TODO 如果有大箱子,改成for (IInventory containerInventory : inventories)
                    IInventory[] inventories = ci.getInventories();
                    //避免列表为空
                    if (inventories == null)
                        continue;

                    //获取是否为大箱子还是小箱子
                    try{
                        if(ci.chest!=null)
                            ci.chest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(handler -> {
                                inventories[0] = ((InvWrapper)handler).getInv();
                            });
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //遍历箱子
                    IInventory containerInventory = inventories[0];
                    int containerSize = containerInventory.getSizeInventory();
                    for (int containerSlot = 0; containerSlot < containerSize; containerSlot++) {
                        //判断是否已经有空位或者物品已经添加
                        if (freeSlotInventory != null && itemCompletlyAdded)
                            break;
                        //获取当前格子箱子物品
                        ItemStack containerStack = containerInventory.getStackInSlot(containerSlot);
                        //若箱子物品为空
                        if (containerStack.isEmpty()&&freeSlotInventory == null) {
                            freeSlotInventory = containerInventory;
                            freeSlotIndex = containerSlot;
                            //如果箱子里的物品(内循环)和身上物品(外循环)相同
                        } else if (compareItems(playersItemStack,containerStack) ){
                            //如果未储存但该箱子包含该物品
                            if(containerStack.getCount()==containerStack.getMaxStackSize()){
                                includeItem = true;
                            }
                            //添加物品
                            int oldCountInContainer = containerStack.getCount();//32
                            int newCountInContainer = containerStack.getCount() + playersItemStack.getCount();//32+64=96
                            newCountInContainer= Math.min(newCountInContainer, containerStack.getMaxStackSize());//96>64?64:96=64
                            int removedCountFromPlayer = newCountInContainer - oldCountInContainer;//64-32=32
                            containerStack.setCount(newCountInContainer);//=64

                            //玩家移除物品之前抢先记录该物品名称
                            String displayName = playersItemStack.getTextComponent().getString();

                            int old_playerCount = playersItemStack.getCount();//64
                            playersItemStack.setCount(playersItemStack.getCount() - removedCountFromPlayer);//64-32=32

                            //如果储存进去了
                            if (removedCountFromPlayer > 0)
                                isStored = true;

                            //如果玩家物品数量为0
                            int playerCount = playersItemStack.getCount();
                            //玩家手上如果还剩下物品说明箱子满了
                            ci.isFull = playerCount != 0;

                            //统计贮藏物品,不统计空气
                            if (StoreConfig_Client.switches.detailInfoEnable.get()) {
                                //如果已经储存的物品名称(Keys)中包括了当前物品名称,并且此次循环成功将玩家物品转移了一些到箱子 .storedItems中包括了储存该物品的箱子的位置与储存总数
                                if (QuickStore.storedItems.containsKey(displayName) && removedCountFromPlayer > 0) {
                                    ItemInfo itemInfo = QuickStore.storedItems.get(displayName);
                                    itemInfo.setAmount(itemInfo.getAmount() + removedCountFromPlayer);
                                    addPos(ci, displayName, itemInfo);
                                } else if (removedCountFromPlayer > 0) {//新建
                                    QuickStore.storedItems.put(displayName, new ItemInfo(addNewPos(ci), old_playerCount - playerCount));
                                }
                            }
                            //如果玩家物品数量为0
                            if (playersItemStack.getCount() <= 0) {
                                inventoryPlayer.removeStackFromSlot(inventorySlot);
                                itemCompletlyAdded = true;
                            }
                        }
                    }
                    //物品没有被完全储存,且该箱子中有空位,且该箱子包含该物品
                    if (!itemCompletlyAdded && freeSlotInventory != null && includeItem) {
                        if (StoreConfig_Client.switches.detailInfoEnable.get() && !playersItemStack.isEmpty()) {
                            String displayName = playersItemStack.getTextComponent().getString();
                            if (QuickStore.storedItems.containsKey(displayName) && !isStored) {
                                ItemInfo itemInfo = QuickStore.storedItems.get(displayName);
                                itemInfo.setAmount(itemInfo.getAmount() + inventoryPlayer.getStackInSlot(inventorySlot).getCount());
                                addPos(ci, displayName, itemInfo);
                            } else if (!isStored){
                                QuickStore.storedItems.put(displayName, new ItemInfo(addNewPos(ci), inventoryPlayer.getStackInSlot(inventorySlot).getCount()));
                            }
                        }
                        freeSlotInventory.setInventorySlotContents(freeSlotIndex, inventoryPlayer.getStackInSlot(inventorySlot));
                        inventoryPlayer.setInventorySlotContents(inventorySlot, ItemStack.EMPTY);
                        isStored = true;
                    }


                    //如果箱子 包含该物品 没有空间 允许显示满了的箱子 箱子确实满了
                    if (includeItem && freeSlotInventory == null && StoreConfig_Client.switches.fullInfoEnable.get() && ci.isFull) {
                        BlockPos pos = ci.getPos();
                        //player.sendMessage(new TranslationTextComponent("commands.quickstore.nospace", pos.getX(), pos.getY(), pos.getZ()), player.getUniqueID());
                        Utils_Server.storedList.add(new RenderBlockProps(pos, StoreConfig_Client.general.RED.get()));
                    }
                }
            }
        }
        //发包给客户端,渲染相关箱子
        if (!player.world.isRemote)
            for (RenderBlockProps blockProps : Utils_Server.storedList) {
                Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(
                                () -> player
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
            QuickStore.storedItems.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPos(ContainerInformation ci, String displayName, ItemInfo itemInfo) {
        BlockPos pos = ci.getPos();
        if (!itemInfo.isSamePosition(pos))
            itemInfo.getPosition().add(pos);
        checkRepeat(pos);
    }

    private List<BlockPos> addNewPos(ContainerInformation ci) {
        List<BlockPos> blockPos = new ArrayList<>();
        BlockPos pos = ci.getPos();
        blockPos.add(pos);
        checkRepeat(pos);
        return blockPos;
    }

    private void checkRepeat(BlockPos pos) {
        if (!Utils_Server.storedList.contains(new RenderBlockProps(pos, StoreConfig_Client.general.GREEN.get())))
            Utils_Server.storedList.add(new RenderBlockProps(pos, StoreConfig_Client.general.GREEN.get()));
    }

    //比较物品
    private boolean compareItems(ItemStack first, ItemStack second) {
        if(StoreConfig_Server.general.IGNORE_ITEM_DAMAGE.get())
            return first.isItemEqualIgnoreDurability(second);
        return first.isItemEqual(second);
    }

}
