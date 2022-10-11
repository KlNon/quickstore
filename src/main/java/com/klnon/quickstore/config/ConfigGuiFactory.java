//package com.klnon.quickstore.config;
//
//import com.klnon.quickstore.utils.Utils;
//import com.mojang.blaze3d.matrix.MatrixStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.button.Button;
//import net.minecraft.client.gui.widget.list.OptionsRowList;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.TranslationTextComponent;
//import net.minecraftforge.common.config.ConfigElement;
//import net.minecraftforge.fml.client.IModGuiFactory;
//import net.minecraftforge.fml.client.config.GuiConfig;
//
//import java.util.Collections;
//import java.util.Set;
//
//
//public final class ConfigGuiFactory extends Screen {
//    private static final int TITLE_HEIGHT = 8;
//    /** 从屏幕顶部到选项列表顶端的距离 */
//    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
//    /** 从屏幕底部到选项列表底端的距离 */
//    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
//    /** 选项列表里每个选项的高度 */
//    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
//
//    /** 按钮宽度 */
//    private static final int BUTTON_WIDTH = 200;
//    /** 按钮高度 */
//    private static final int BUTTON_HEIGHT = 20;
//    /** 从屏幕底部到“完成”按钮顶端的距离 */
//    private static final int DONE_BUTTON_TOP_OFFSET = 26;
//
//    /** 选项列表 */
//    // 如下所述，此字段无法在对象构造时初始化，故无法使用 'final' 关键字
//    private OptionsRowList optionsRowList;
//
//    public ConfigGuiFactory() {
//        // 通过父类构造器指定此界面的标题
//        super(new TranslationTextComponent("keys."+Utils.MOD_ID+"title"));
//    }
//
//
//    @Override
//    protected void init() {
//        // 创建选项列表
//        // 如果不在这里创建，而在构造器里创建的话，会造成选项列表渲染错误
//        this.optionsRowList = new OptionsRowList(
//                this.minecraft, this.width, this.height,
//                OPTIONS_LIST_TOP_HEIGHT,
//                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
//                OPTIONS_LIST_ITEM_HEIGHT
//        );
//
//        // 将选项列表加入到此界面的子元素中
//        // 如果不加，用户就无法点击列表中的元素
//        this.children.add(this.optionsRowList);
//
//        // 添加“完成”按钮
//        this.addButton(new Button(
//                (this.width - BUTTON_WIDTH) / 2,
//                this.height - DONE_BUTTON_TOP_OFFSET,
//                BUTTON_WIDTH, BUTTON_HEIGHT,
//                // 按钮上显示的文字
//                new TranslationTextComponent("gui.done"),
//                // 按钮被点击时执行的操作
//                button -> this.onClose()
//        ));
//    }
//
//    @Override
//    public void render(MatrixStack matrixStack,
//                       int mouseX, int mouseY, float partialTicks) {
//        this.renderBackground(matrixStack);
//        // 为避免显示错乱，需要在这个时机渲染选项列表
//        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
//        drawCenteredString(matrixStack, this.font, this.title.getString(),
//                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
//        super.render(matrixStack, mouseX, mouseY, partialTicks);
//    }
//
//    @Override
//    public GuiScreen createConfigGui(GuiScreen parent) {
//        return new GuiConfig(parent, ConfigElement.from(StoreConfig.class).getChildElements(), "quickstore", false, false, "QuickStore", "store nearby!");
//    }
//
//}
