package com.klnon.quickstore.gui.model;

import com.klnon.quickstore.utils.Utils_Client;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class GameItemStore {

    private ArrayList<ItemWithItemStack> store = new ArrayList<>();

    public void populate()
    {
        // Avoid doing the logic again unless repopulate is called
        if( this.store.size() != 0 )
            return;

        for ( Item item : ForgeRegistries.ITEMS ) {
            if(item == null)
                continue;

            if (item == Items.AIR || Utils_Client.blackList.contains(item))
                continue; // avoids troubles

            store.add(new ItemWithItemStack(item, new ItemStack(item)));
        }
    }

    public void repopulate()
    {
        this.store.clear();
        this.populate();
    }

    public ArrayList<ItemWithItemStack> getStore() {
        return this.store;
    }

    public static final class ItemWithItemStack {
        private Item item;
        private ItemStack itemStack;

        public ItemWithItemStack(Item item, ItemStack itemStack) {
            this.item = item;
            this.itemStack = itemStack;
        }

        public Item getItem() {
            return item;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }
}
