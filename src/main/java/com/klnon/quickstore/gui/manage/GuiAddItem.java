package com.klnon.quickstore.gui.manage;

import com.klnon.quickstore.ClientProxy;
import com.klnon.quickstore.gui.GuiSelection;
import com.klnon.quickstore.gui.model.ItemData;
import com.klnon.quickstore.gui.utils.GuiBase;
import com.klnon.quickstore.utils.Utils_Client;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

import static com.klnon.quickstore.gui.model.Args.*;

public class GuiAddItem extends GuiBase {
    private TextFieldWidget itemName;
    private Button addBtn;
    private Item selectItem;
    private ItemStack itemStack;

    private boolean oreNameCleared = false;
    private Supplier<GuiBase> previousScreenCallback;

    public GuiAddItem(Item selectedBlock, Supplier<GuiBase> previousScreenCallback) {
        super(false);
        this.selectItem = selectedBlock;
        this.previousScreenCallback = previousScreenCallback;
        this.itemStack = new ItemStack(selectItem, 1);
    }

    @Override
    public void init() {
        // Called when the gui should be (re)created
        addButton(addBtn = new Button(getWidth() / 2 +AI_X_OFFSET, getHeight() / 2 + AI_Y_OFFSET, AI_WIDTH, AI_HEIGHT, new TranslationTextComponent("quickstore.single.add"), b -> {
            this.closeScreen();

            if (selectItem.getRegistryName() == null)
                return;

            // Push the Item to the render stack
            Utils_Client.getItemStore().put(
                    new ItemData(
                            itemName.getText(),
                            selectItem.getRegistryName().toString(),
                            this.itemStack,
                            false,
                            Utils_Client.getItemStore().getStore().size() + 1
                    )
            );

            ClientProxy.itemStore.write(new ArrayList<>(Utils_Client.getItemStore().getStore().values()));
            getMinecraft().displayGuiScreen(new GuiSelection());
        }));
        addButton(new Button(getWidth() / 2 + CANCEL_X_OFFSET, getHeight() / 2 + CANCEL_Y_OFFSET, CANCEL_WIDTH, CANCEL_HEIGHT, new TranslationTextComponent("quickstore.single.cancel"), b -> {
            this.closeScreen();
            Minecraft.getInstance().displayGuiScreen(this.previousScreenCallback.get());
        }));

        itemName = new TextFieldWidget(getMinecraft().fontRenderer, getWidth() / 2 - 100, getHeight() / 2 - 70, 202, 20, StringTextComponent.EMPTY);
        //TODO  名字可能出错
        itemName.setText(this.selectItem.getName().getString());
        this.children.add(itemName);
    }

    @Override
    public void tick() {
        super.tick();
        itemName.tick();
    }

    @Override
    public void renderExtra(MatrixStack stack, int x, int y, float partialTicks) {
        getFontRender().drawStringWithShadow(stack, selectItem.getName().getString(), getWidth() / 2f +AIN_X_OFFSET, getHeight() / 2f +AIN_Y_OFFSET, 0xffffff);

        RenderHelper.enableStandardItemLighting();
        this.itemRenderer.renderItemAndEffectIntoGUI(this.itemStack, getWidth() / 2 + ADD_PIC_X_OFFSET, getHeight() / 2 +ADD_PIC_Y_OFFSET);
        RenderHelper.disableStandardItemLighting();
    }

    // FIXME: 28/06/2020 replace with matrix system instead of the tess
    static void renderPreview(int x, int y, float r, float g, float b) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder tessellate = tessellator.getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(r/255, g/255, b/255, 1);
        tessellate.begin(7, DefaultVertexFormats.POSITION);
        tessellate.pos(x, y, 0.0D).endVertex();
        tessellate.pos(x, y + 45, 0.0D).endVertex();
        tessellate.pos(x + 202, y + 45, 0.0D).endVertex();
        tessellate.pos(x + 202, y, 0.0D).endVertex();
        tessellator.draw();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouse) {
        if (itemName.mouseClicked(x, y, mouse))
            this.setListener(itemName);

        if (itemName.isFocused() && !oreNameCleared) {
            itemName.setText("");
            oreNameCleared = true;
        }

        if (!itemName.isFocused() && oreNameCleared && Objects.equals(itemName.getText(), "")) {
            oreNameCleared = false;
            itemName.setText(this.selectItem.getName().getString());
        }

        return super.mouseClicked(x, y, mouse);
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    public String title() {
        return I18n.format("quickstore.title.config");
    }
}
