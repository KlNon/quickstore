package com.klnon.quickstore.gui.manage;

import com.klnon.quickstore.ClientProxy;
import com.klnon.quickstore.gui.GuiSelection;
import com.klnon.quickstore.gui.model.GameItemStore;
import com.klnon.quickstore.gui.utils.GuiBase;
import com.klnon.quickstore.gui.utils.ScrollingList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.klnon.quickstore.gui.model.Args.*;

public class GuiItemList extends GuiBase {
    private ScrollingItemList itemList;
    private ArrayList<GameItemStore.ItemWithItemStack> items;
    private TextFieldWidget search;
    private String lastSearched = "";

    public GuiItemList() {
        super(false);
        this.items = ClientProxy.gameItemStore.getStore();
    }

    @Override
    public void init() {
        this.itemList = new ScrollingItemList((getWidth() / 2) + AIL_X_OFFSET, getHeight() / 2 + AIL_Y_OFFSET, AIL_WIDTH, AIL_HEIGHT, this.items);
        this.children.add(this.itemList);

        search = new TextFieldWidget(getFontRender(), getWidth() / 2 + ADD_SEARCH_X_OFFSET, getHeight() / 2 + ADD_SEARCH_Y_OFFSET, ADD_SEARCH_WIDTH, ADD_SEARCH_HEIGHT, new StringTextComponent(""));
        search.changeFocus(true);
        this.setListener(search);

        addButton(new Button(getWidth() / 2 + ADD_CANCEL_X_OFFSET, getHeight() / 2 + ADD_CANCEL_Y_OFFSET, ADD_CANCEL_WIDTH, ADD_CANCEL_HEIGHT, new TranslationTextComponent("quickstore.single.cancel"), b -> {
            this.closeScreen();
            Minecraft.getInstance().displayGuiScreen(new GuiSelection());
        }));
    }

    @Override
    public void tick() {
        search.tick();
        if (!search.getText().equals(this.lastSearched))
            reloadItems();

        super.tick();
    }

    private void reloadItems() {
        if (this.lastSearched.equals(search.getText()))
            return;

        this.itemList.updateEntries(
                search.getText().length() == 0
                        ? this.items
                        : this.items.stream()
                            .filter(e -> e.getItemStack().getDisplayName().getString().toLowerCase().contains(search.getText().toLowerCase()))
                            .collect(Collectors.toList())
        );

        lastSearched = search.getText();
        this.itemList.setScrollAmount(0);
    }

    @Override
    public void renderExtra(MatrixStack stack, int x, int y, float partialTicks) {
        search.render(stack, x, y, partialTicks);
        itemList.render(stack, x, y, partialTicks);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if( this.search.mouseClicked (x, y, button) )
            this.setListener(this.search);

        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        itemList.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
        return super.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }

    static class ScrollingItemList extends ScrollingList<ScrollingItemList.ItemSlot> {

        ScrollingItemList(int x, int y, int width, int height, List<GameItemStore.ItemWithItemStack> items) {
            super(x, y, width, height, ITEM_HEIGHT);
            this.updateEntries(items);
        }

        @Override
        public void setSelected(@Nullable ItemSlot entry) {
            if (entry == null)
                return;

            Minecraft.getInstance().player.closeScreen();
            Minecraft.getInstance().displayGuiScreen(new GuiAddItem(entry.getItem().getItem(), GuiItemList::new));
        }

        void updateEntries(List<GameItemStore.ItemWithItemStack> items) {
            this.clearEntries(); // @mcp: func_230963_j_ = clearEntries
            items.forEach(item -> this.addEntry(new ItemSlot(item, this)));
        }

        public static class ItemSlot extends AbstractList.AbstractListEntry<ScrollingItemList.ItemSlot> {
            GameItemStore.ItemWithItemStack item;
            ScrollingItemList parent;

            ItemSlot(GameItemStore.ItemWithItemStack item, ScrollingItemList parent) {
                this.item = item;
                this.parent = parent;
            }

            public GameItemStore.ItemWithItemStack getItem() {
                return item;
            }

            @Override
            public void render(MatrixStack stack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
                FontRenderer font = this.parent.minecraft.fontRenderer;

                ResourceLocation resource = this.item.getItemStack().getItem().getRegistryName();
                font.drawString(stack,this.item.getItemStack().getItem().getName().getString(), left + ITEM_NAME_X_OFFSET, top + ITEM_NAME_Y_OFFSET, Color.WHITE.getRGB());
                // @mcp: func_240652_a_ = unknown... Code recommendation

                RenderHelper.enableStandardItemLighting();
                this.parent.minecraft.getItemRenderer().renderItemAndEffectIntoGUI(this.item.getItemStack(), left + 8, top + 7);
                RenderHelper.disableStandardItemLighting();
            }

            @Override
            public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
                this.parent.setSelected(this);
                return false;
            }
        }
    }
}
