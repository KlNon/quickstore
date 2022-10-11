package com.klnon.quickstore.config;

import com.klnon.quickstore.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Collections;
import java.util.Set;


public final class ConfigGuiFactory extends Screen {
    private static final int TITLE_HEIGHT = 8;

    public ConfigGuiFactory() {
        // 通过父类构造器指定此界面的标题
        super(new TranslationTextComponent("hbwhelper.configGui.title",
                Utils.));
    }


    @Override
    public void render(MatrixStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        // 首先渲染界面背景
        this.renderBackground();
        // 渲染标题
        this.drawCenteredString(this.font, this.title.getFormattedText(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        // 调用父类对应方法，完成渲染
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new GuiConfig(parent, ConfigElement.from(StoreConfig.class).getChildElements(), "quickstore", false, false, "QuickStore", "store nearby!");
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        // 这个方法已经被 deprecated 了，但是迟迟没删除，而且并没有正确实现。
        // 所以我们就不管它了。目前来看，返回 null 不会有问题，但慎重起见，还是
        // 返回一个正常的 Set 比较稳妥。
        return Collections.emptySet();
    }

}
