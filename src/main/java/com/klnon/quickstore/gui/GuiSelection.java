package com.klnon.quickstore.gui;


import com.klnon.quickstore.ClientProxy;
import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.gui.manage.GuiAddItem;
import com.klnon.quickstore.gui.manage.GuiEdit;
import com.klnon.quickstore.gui.manage.GuiItemList;
import com.klnon.quickstore.gui.model.Args;
import com.klnon.quickstore.gui.model.ItemData;
import com.klnon.quickstore.gui.model.ItemStore;
import com.klnon.quickstore.gui.utils.GuiBase;
import com.klnon.quickstore.gui.utils.ScrollingList;
import com.klnon.quickstore.gui.utils.SupportButton;
import com.klnon.quickstore.keybinding.KeyBindings;
import com.klnon.quickstore.utils.Utils_Client;
import com.klnon.quickstore.utils.Utils_Server;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.MinecartItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.klnon.quickstore.gui.model.Args.*;

public class GuiSelection extends GuiBase {
    private static final ResourceLocation CIRCLE = new ResourceLocation(QuickStore.PREFIX_GUI + "circle.png");

    private Button distButtons;
    private TextFieldWidget search;
    public ItemRenderer render;

    private String lastSearch = "";

    private ArrayList<ItemData> itemList, originalList;
    private ScrollingItemList scrollList;

    public GuiSelection() {
        super(true);
        this.setSideTitle(I18n.format("quickstore.gui.tools"));

        // Inject this hear as everything is loaded
        if (ClientProxy.itemStore.created) {
            List<ItemData.SerializableItemData> items = ClientProxy.itemStore.populateDefault();
            Utils_Client.getItemStore().setStore(ItemStore.getFromSimpleItemList(items));

            ClientProxy.itemStore.created = false;
        }

        this.itemList = new ArrayList<>(Utils_Client.getItemStore().getStore().values());
        this.itemList.sort(Comparator.comparingInt(ItemData::getOrder));

        this.originalList = this.itemList;
    }

    @Override
    public void init() {
        if (getMinecraft().player == null)
            return;

        this.render = this.itemRenderer;
        this.buttons.clear();

        this.scrollList = new ScrollingItemList(getWidth() / 2 + ITEM_LIST_X_OFFSET, getHeight() / 2 + ITEM_LIST_Y_OFFSET, ITEM_LIST_WIDTH, ITEM_LIST_HEIGHT, this.itemList, this);
        this.children.add(this.scrollList);

        this.search = new TextFieldWidget(getFontRender(), getWidth() / 2 + SEARCH_X_OFFSET, getHeight() / 2 + SEARCH_Y_OFFSET, SEARCH_WIDTH, SEARCH_HEIGHT, StringTextComponent.EMPTY);
        this.search.setCanLoseFocus(true);

        // side bar buttons
        addButton(new SupportButtonInner((getWidth() / 2) + SIDE_AI_X_OFFSET, getHeight() / 2 + SIDE_AI_Y_OFFSET, SIDE_AI_WIDTH, SIDE_AI_HEIGHT, I18n.format("quickstore.input.add"), "quickstore.tooltips.add_item", button -> {
            getMinecraft().player.closeScreen();
            getMinecraft().displayGuiScreen(new GuiItemList());
        }));
        addButton(new SupportButtonInner(getWidth() / 2 + SIDE_AHI_X_OFFSET, getHeight() / 2 + SIDE_AHI_Y_OFFSET, SIDE_AHI_WIDTH, SIDE_AHI_HEIGHT, I18n.format("quickstore.input.add_hand"), "quickstore.tooltips.add_item_in_hand", button -> {
            getMinecraft().player.closeScreen();
            ItemStack handItem = getMinecraft().player.getHeldItem(Hand.MAIN_HAND);
            //不为空气
            if (handItem.isEmpty()) {
                getMinecraft().player.sendStatusMessage(new StringTextComponent("[quickSearch] " + I18n.format("quickstore.message.invalid_hand", handItem.getDisplayName().getString())), false);
                return;
            }
            // Check if the hand item is a item or not
            //TODO MinecraftItem可能有问题,物品不为手上目标
            getMinecraft().displayGuiScreen(new GuiAddItem(handItem.getItem(), GuiSelection::new));
        }));
        addButton(new SupportButtonInner(getWidth() / 2 + SIDE_SEARCH_X_OFFSET, getHeight() / 2 + SIDE_SEARCH_Y_OFFSET, SIDE_SEARCH_WIDTH, SIDE_SEARCH_HEIGHT, I18n.format("quickstore.input.search"), "quickstore.tooltips.search", button -> {
            getMinecraft().player.closeScreen();
            if (!Utils_Client.isQuickSeeActive())
                Utils_Client.toggleQuickSee();
            Utils_Server.sendSearchCommand();
        }));
        addButton(new Button(getWidth() / 2 + SIDE_HELP_X_OFFSET, getHeight() / 2 + SIDE_HELP_Y_OFFSET, SIDE_HELP_WIDTH, SIDE_HELP_HEIGHT, new TranslationTextComponent("quickstore.gui.help"), button -> {
            getMinecraft().player.closeScreen();
            getMinecraft().displayGuiScreen(new GuiHelp());
        }));
        addButton(new Button(getWidth() / 2 + SIDE_CLOSE_X_OFFSET, getHeight() / 2 + SIDE_CLOSE_Y_OFFSET, SIDE_CLOSE_WIDTH, SIDE_CLOSE_HEIGHT, new TranslationTextComponent("quickstore.gui.close"), button -> {
            this.closeScreen();
        }));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!search.isFocused() && keyCode == KeyBindings.quickSearch.getKeyBinding().getKey().getKeyCode()) {
            this.closeScreen();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateSearch() {
        if (lastSearch.equals(search.getText()))
            return;

        if (search.getText().equals("")) {
            this.itemList = this.originalList;
            this.scrollList.updateEntries(this.itemList);
            lastSearch = "";
            return;
        }

        this.itemList = this.originalList.stream()
                .filter(b -> b.getEntryName().toLowerCase().contains(search.getText().toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));

        this.itemList.sort(Comparator.comparingInt(ItemData::getOrder));

        this.scrollList.updateEntries(this.itemList);
        lastSearch = search.getText();
    }

    @Override
    public void tick() {
        super.tick();
        search.tick();

        updateSearch();
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouse) {
        if (search.mouseClicked(x, y, mouse))
            this.setListener(search);

        return super.mouseClicked(x, y, mouse);
    }

    @Override
    public void renderExtra(MatrixStack stack, int x, int y, float partialTicks) {
        this.search.render(stack, x, y, partialTicks);
        this.scrollList.render(stack, x, y, partialTicks);

        if (!search.isFocused() && search.getText().equals(""))
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(stack, I18n.format("quickstore.gui.search"), (float) getWidth() / 2 - 130, (float) getHeight() / 2 - 101, Color.GRAY.getRGB());
    }

    @Override
    public void onClose() {
        ClientProxy.itemStore.write(new ArrayList<>(Utils_Client.getItemStore().getStore().values()));

        Utils_Client.requestBlockFinder(true);
        super.onClose();
    }

    static final class SupportButtonInner extends SupportButton {
        public SupportButtonInner(int widthIn, int heightIn, int width, int height, String text, String i18nKey, Button.IPressable onPress) {
            super(widthIn, heightIn, width, height, new StringTextComponent(text), new TranslationTextComponent(i18nKey), onPress);
        }
    }

    static class ScrollingItemList extends ScrollingList<ScrollingItemList.ItemSlot> {
        public GuiSelection parent;

        ScrollingItemList(int x, int y, int width, int height, List<ItemData> items, GuiSelection parent) {
            super(x, y, width, height, ITEM_HEIGHT);
            this.updateEntries(items);
            this.parent = parent;
        }

        public void setSelected(@Nullable ItemSlot entry, int mouse) {
            if (entry == null)
                return;

            if (GuiSelection.hasShiftDown()) {
                Minecraft.getInstance().player.closeScreen();
                Minecraft.getInstance().displayGuiScreen(new GuiEdit(entry.item));
                return;
            }

            Utils_Client.getItemStore().toggleFinder(entry.item);
            ClientProxy.itemStore.write(new ArrayList<>(Utils_Client.getItemStore().getStore().values()));
        }

        void updateEntries(List<ItemData> items) {
            this.clearEntries();
            items.forEach(item -> this.addEntry(new ItemSlot(item, this)));
        }

        public static class ItemSlot extends AbstractList.AbstractListEntry<ScrollingItemList.ItemSlot> {
            ItemData item;
            ScrollingItemList parent;

            ItemSlot(ItemData item, ScrollingItemList parent) {
                this.item = item;
                this.parent = parent;
            }

            public ItemData getItem() {
                return item;
            }

            @Override
            public void render(MatrixStack stack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
                ItemData itemData = this.item;

                FontRenderer font = Minecraft.getInstance().fontRenderer;

                String itemName = itemData.getEntryName();
                if (Utils_Client.isContainChinese(itemName))
                    if (itemName.length() > 4) {
                        font.drawString(stack, itemName.substring(0, 3), left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET, Color.WHITE.getRGB());
                        font.drawString(stack, itemName.substring(3), left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET2, Color.WHITE.getRGB());
                    } else
                        font.drawString(stack, itemName, left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET2, Color.WHITE.getRGB());
                else {
                    //如果英文字符串有空格
                    if (itemName.contains(" ")) {
                        String[] itemSplit = itemName.split("\\s+");
                        StringBuilder itemSplitName = new StringBuilder();
                        int strPos = 0;
                        if (itemSplit[0].length() + itemSplit[1].length() > 9) {
                            font.drawString(stack, itemSplit[0], left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET, Color.WHITE.getRGB());
                            strPos = 1;
                            if (itemSplit.length > 2) {
                                font.drawString(stack, itemSplit[1], left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET2, Color.WHITE.getRGB());
                                strPos = 2;
                            }
                            for (int i = strPos; i < itemSplit.length; i++)
                                itemSplitName.append(itemSplit[i]).append(" ");
                            font.drawString(stack, itemSplitName.toString(), left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET3, Color.WHITE.getRGB());
                        }
                        if (itemSplit[0].length() + itemSplit[1].length() <= 9) {
                            font.drawString(stack, itemSplit[0] + " " + itemSplit[1], left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET, Color.WHITE.getRGB());
                            strPos = 2;
                            if (itemSplit.length > 3) {
                                font.drawString(stack, itemSplit[2], left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET2, Color.WHITE.getRGB());
                                strPos = 3;
                            }
                            for (int i = strPos; i < itemSplit.length; i++)
                                itemSplitName.append(itemSplit[i]).append(" ");
                            font.drawString(stack, itemSplitName.toString(), left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET3, Color.WHITE.getRGB());
                        }

                    } else
                        font.drawString(stack, itemName, left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET3, Color.WHITE.getRGB());
                }
                //TODO 开启或关闭物品搜寻的标识
                font.drawString(stack, itemData.isFinder() ? "On" : "Off", left + ITEM_STAT_X_OFFSET, top + ITEM_STAT_Y_OFFSET, itemData.isFinder() ? Color.GREEN.getRGB() : Color.RED.getRGB());

                RenderHelper.enableStandardItemLighting();
                Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(itemData.getItemStack(), left + ITEM_PIC_X_OFFSET, top + ITEM_PIC_Y_OFFSET);
                RenderHelper.disableStandardItemLighting();
            }

            @Override
            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int mouse) {
                this.parent.setSelected(this, mouse);
                return false;
            }
        }
    }
}
