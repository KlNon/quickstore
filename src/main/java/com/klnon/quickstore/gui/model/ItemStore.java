package com.klnon.quickstore.gui.model;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemStore {
    private static HashMap<UUID, ItemData> store = new HashMap<>();
    private HashMap<String, UUID> storeReference = new HashMap<>();

    public void put(ItemData data) {
        if( this.storeReference.containsKey(data.getRegName()) )
            return;

        UUID uniqueId = UUID.randomUUID();
        store.put(uniqueId, data);
        this.storeReference.put(data.getRegName(), uniqueId);
    }

    public void remove(String itemRegistry) {
        if( !this.storeReference.containsKey(itemRegistry) )
            return;

        UUID uuid = this.storeReference.get(itemRegistry);
        this.storeReference.remove(itemRegistry);
        store.remove(uuid);
    }

    public HashMap<UUID, ItemData> getStore() {
        return store;
    }

    public void setStore(ArrayList<ItemData> store) {
        ItemStore.store.clear();
        this.storeReference.clear();

        store.forEach(this::put);
    }

    public Pair<ItemData, UUID> getStoreByReference(String name) {
        UUID uniqueId = storeReference.get(name);
        if( uniqueId == null )
            return null;

        ItemData itemData = store.get(uniqueId);
        if( itemData == null )
            return null;

        return new ImmutablePair<>(itemData, uniqueId);
    }

    public void toggleFinder(ItemData data) {
        UUID uniqueId = storeReference.get(data.getRegName());
        if( uniqueId == null )
            return;

        // We'd hope this never happens...
        ItemData itemData = store.get(uniqueId);
        if( itemData == null )
            return;

        itemData.setFinder(!itemData.isFinder());
    }

    public static ArrayList<ItemData> getFromSimpleItemList(List<ItemData.SerializableItemData> simpleList)
    {
        ArrayList<ItemData> itemData = new ArrayList<>();

        for (ItemData.SerializableItemData e : simpleList) {
            if( e == null )
                continue;

            ResourceLocation location = null;
            try {
                location = new ResourceLocation(e.getRegName());
            } catch (Exception ignored) {};
            if( location == null )
                continue;

            Item item = ForgeRegistries.ITEMS.getValue(location);
            if( item == null )
                continue;

            itemData.add(
                    new ItemData(
                            e.getName(),
                            e.getRegName(),
                            new ItemStack( item, 1),
                            e.isFinder(),
                            e.getOrder()
                    )
            );
        }

        return itemData;
    }

    public static ArrayList<ItemStack> getItemStack()
    {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();


        for (ItemData itemData : store.values()) {
            if( itemData == null || !itemData.isFinder())
                continue;

            itemStacks.add(itemData.getItemStack());
        }

        return itemStacks;
    }
}
