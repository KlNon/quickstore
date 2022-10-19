package com.klnon.quickstore.gui.model;

import net.minecraft.item.ItemStack;

public class ItemData {
    private String entryName;
    private String regName;
    private ItemStack itemStack;
    private boolean finder;
    private int order;

    public ItemData(String entryName, String regName, ItemStack itemStack, boolean finder, int order) {
        this.entryName = entryName;
        this.regName = regName;
        this.itemStack = itemStack;
        this.finder = finder;
        this.order = order;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getRegName() {
        return regName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isFinder() {
        return finder;
    }

    public void setFinder(boolean finder) {
        this.finder = finder;
    }

    public int getOrder() {
        return order;
    }

    // It's pretty annoying to serialize an ItemStack so we dont :D
    public static class SerializableItemData {

        private String name;
        private String regName;
        private int order;
        private boolean finder;

        public SerializableItemData(String name, String regName, boolean finder, int order) {
            this.name = name;
            this.regName = regName;
            this.finder = finder;
            this.order = order;
        }

        public String getName() {
            return name;
        }

        public String getRegName() {
            return regName;
        }

        public boolean isFinder() {
            return finder;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }
}
