package com.klnon.quickstore.gui.manage;

import com.klnon.quickstore.ClientProxy;
import com.klnon.quickstore.gui.GuiSelection;
import com.klnon.quickstore.gui.model.ItemData;
import com.klnon.quickstore.gui.utils.GuiBase;
import com.klnon.quickstore.utils.Utils_Client;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.UUID;

import static com.klnon.quickstore.gui.model.Args.*;

public class GuiEdit extends GuiBase {
    private TextFieldWidget oreName;
    private ItemData item;

    public GuiEdit(ItemData item) {
        super(true); // Has a sidebar
        this.setSideTitle(I18n.format("quickstore.gui.tools"));

        this.item = item;
    }

    @Override
    public void init() {
        addButton(new Button((getWidth() / 2) + SED_X_OFFSET, getHeight() / 2 + SED_Y_OFFSET, SED_WIDTH, SED_HEIGHT, new TranslationTextComponent("quickstore.gui.delete"), b -> {
            Utils_Client.getItemStore().remove(item.getRegName());
            ClientProxy.itemStore.write(new ArrayList<>(Utils_Client.getItemStore().getStore().values()));

            this.closeScreen();
            getMinecraft().displayGuiScreen(new GuiSelection());
        }));

        addButton(new Button((getWidth() / 2) + SEC_X_OFFSET, getHeight() / 2 + SEC_Y_OFFSET, SEC_WIDTH, SEC_HEIGHT, new TranslationTextComponent("quickstore.gui.cancel"), b -> {
            this.closeScreen();
            this.getMinecraft().displayGuiScreen(new GuiSelection());
        }));
        addButton(new Button(getWidth() / 2 + SES_X_OFFSET, getHeight() / 2 + SES_Y_OFFSET, SES_WIDTH, SES_HEIGHT, new TranslationTextComponent("quickstore.gui.save"), b -> {
            ItemData item = new ItemData(
                    this.oreName.getText(),
                    this.item.getRegName(),
                    this.item.getItemStack(),
                    this.item.isFinder(),
                    this.item.getOrder()
            );

            Pair<ItemData, UUID> data = Utils_Client.getItemStore().getStoreByReference(item.getRegName());
            Utils_Client.getItemStore().getStore().remove(data.getValue());
            Utils_Client.getItemStore().getStore().put(data.getValue(), item);

            ClientProxy.itemStore.write(new ArrayList<>(Utils_Client.getItemStore().getStore().values()));
            this.closeScreen();
            getMinecraft().displayGuiScreen(new GuiSelection());
        }));

        oreName = new TextFieldWidget(getMinecraft().fontRenderer, getWidth() / 2 + NAME_EDIT_X_OFFSET, getHeight() / 2 + NAME_EDIT_Y_OFFSET, NAME_EDIT_WIDTH, NAME_EDIT_HEIGHT, new StringTextComponent(""));
        oreName.setText(this.item.getEntryName());
        this.children.add(oreName);
    }

    @Override
    public void tick() {
        super.tick();
        oreName.tick();
    }

    @Override
    public void renderExtra(MatrixStack stack, int x, int y, float partialTicks) {
        oreName.render(stack, x, y, partialTicks);

        RenderHelper.enableStandardItemLighting();
        this.itemRenderer.renderItemAndEffectIntoGUI(this.item.getItemStack(), getWidth() / 2 + EDIT_PIC_X_OFFSET, getHeight() / 2 + EDIT_PIC_Y_OFFSET);
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouse) {
        if( oreName.mouseClicked(x, y, mouse) )
            this.setListener(oreName);

        return super.mouseClicked(x, y, mouse);
    }

    @Override
    public boolean mouseReleased(double x, double y, int mouse) {

        return super.mouseReleased(x, y, mouse);
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    public String title() {
        return I18n.format("quickstore.title.edit");
    }
}
