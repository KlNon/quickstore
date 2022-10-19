package com.klnon.quickstore.gui.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

import static com.klnon.quickstore.gui.model.Args.*;

public class ScrollingList<E extends AbstractList.AbstractListEntry<E>> extends AbstractList<E> {
    private boolean renderSelection = true;
    private List<E> entries;
    private boolean scrolling;

    public ScrollingList(int x, int y, int width, int height, int slotHeightIn) {
        super(Minecraft.getInstance(), width, height, y - (height / 2), (y - (height / 2)) + height, slotHeightIn);
        this.setLeftPos(x);
        this.func_244605_b(false);
        this.func_244606_c(false); // removes background
    }



    @Override // @mcp: func_230952_d_ = getScrollbarPosition
    protected int getScrollbarPosition() {
        return (this.x0 + this.width) - 6;
    }

    @Override
    public int getRowWidth() {
        return ITEM_LIST_OUT_WIDTH;
    }

    @Override
    public void setLeftPos(int left) {
        this.x0 = left;
        this.x1 = left + this.width;
    }

    @Override
    protected int getMaxPosition() {
        //如果处于产生滑动条临界点,进行判断
        int shouldExtend =0;
        if(getItemCount()%ITEM_NUM>0)
            shouldExtend=1;
        return (this.getItemCount()/ITEM_NUM+shouldExtend) * this.itemHeight + this.headerHeight;
    }

    public int getRowBottom(int index) {
        return this.getRowTop(index) + this.itemHeight;
    }

    @Override
    public void setRenderSelection(boolean value) {
        this.renderSelection = value;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        double scale = Minecraft.getInstance().getMainWindow().getGuiScaleFactor();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)(this.x0  * scale), (int)(Minecraft.getInstance().getMainWindow().getFramebufferHeight() - ((this.y0 + this.height) * scale)),
                (int)(this.width * scale), (int)(this.height * scale));

        super.render(stack, mouseX, mouseY, partialTicks);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    //渲染物品列表物品排列
    @Override
    protected void renderList(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
        int size = this.getItemCount();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for(int i = 0; i < (size/ITEM_NUM)+1; i++) {
            
            int rowTop = this.getRowTop(i);
            int rowBottom = this.getRowBottom(i);
            
            for(int j=0;j<ITEM_NUM;j++){
                if (rowBottom >= this.y0 && rowTop <= this.y1) {
                    int i1 = y + j * this.itemHeight + this.headerHeight;
                    int ItemHeight = this.itemHeight - 4;
                    if((i*ITEM_NUM+j)>=size)
                        break;
                    E e = this.getEntry(i*ITEM_NUM+j);

                    int ItemWidth = ITEM_WIDTH;
                    if (this.renderSelection && this.isSelectedItem(j)) {
                        int l1 = this.x0 + this.width / 2 - ItemWidth / 2;
                        int i2 = this.x0 + this.width / 2 + ItemWidth / 2;
                        RenderSystem.disableTexture();
                        float f = this.isFocused() ? 1.0F : 0.5F;
                        RenderSystem.color4f(f, f, f, 1.0F);
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                        bufferbuilder.pos((double)l1, (double)(i1 + ItemHeight + 2), 0.0D).endVertex();
                        bufferbuilder.pos((double)i2, (double)(i1 + ItemHeight + 2), 0.0D).endVertex();
                        bufferbuilder.pos((double)i2, (double)(i1 - 2), 0.0D).endVertex();
                        bufferbuilder.pos((double)l1, (double)(i1 - 2), 0.0D).endVertex();
                        tessellator.draw();
                        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                        bufferbuilder.pos((double)(l1 + 1), (double)(i1 + ItemHeight + 1), 0.0D).endVertex();
                        bufferbuilder.pos((double)(i2 - 1), (double)(i1 + ItemHeight + 1), 0.0D).endVertex();
                        bufferbuilder.pos((double)(i2 - 1), (double)(i1 - 1), 0.0D).endVertex();
                        bufferbuilder.pos((double)(l1 + 1), (double)(i1 - 1), 0.0D).endVertex();
                        tessellator.draw();
                        RenderSystem.enableTexture();
                    }
                    int left = this.getRowLeft()+j*ItemWidth+ITEM_BORDER;
                    e.render(matrixStack, j, rowTop, left, ItemWidth, ItemHeight, mouseX, mouseY, this.isMouseOver((double)mouseX, (double)mouseY) && Objects.equals(this.getEntryAtPosition((double)mouseX, (double)mouseY), e), partialTicks);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        entries=this.getEventListeners();
        this.updateScrollingState(mouseX, mouseY, button);
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        } else {
            E e = this.getEntryWithPosition(mouseX, mouseY);
            if (e != null) {
                if (e.mouseClicked(mouseX, mouseY, button)) {
                    this.setListener(e);
                    this.setDragging(true);
                    return true;
                }
            } else if (button == 0) {
                this.clickedHeader((int)(mouseX - (double)(this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int)(mouseY - (double)this.y0) + (int)this.getScrollAmount() - 4);
                return true;
            }

            return this.scrolling;
        }
    }

    //用于返回点击处的物品
    public final E getEntryWithPosition(double mouseX, double mouseY) {

        //i是itemList外框宽度的一半
        int i = this.getRowWidth() / 2;
        //j是物品列表中间
        int j = this.x0 + this.width / 2;
        //k是物品列表左侧
        int k = j - i;
        //l是物品列表右侧
        int l = j + i;
        int i1 = MathHelper.floor(mouseY - (double)this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
        //行数
        int row = i1 / this.itemHeight;
        int col = (int) ((mouseX-k)/ITEM_WIDTH);

        if(row*ITEM_NUM+col>=this.getItemCount())
            return null;
        //鼠标位置_在左边_滚动条位置,鼠标位置在
        return (E)(mouseX < (double)this.getScrollbarPosition() && mouseX >= (double)k && mouseX <= (double)l && row >= 0 && i1 >= 0 && row < this.getItemCount() ? this.getEventListeners().get(row*ITEM_NUM+col) : null);
    }



    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
            return true;
        } else if (button == 0 && this.scrolling) {
            if (mouseY < (double)this.y0) {
                this.setScrollAmount(0.0D);
            } else if (mouseY > (double)this.y1) {
                this.setScrollAmount((double)this.getMaxScroll());
            } else {
                double d0 = (double)Math.max(1, this.getMaxScroll());
                int i = this.y1 - this.y0;
                int j = MathHelper.clamp((int)((float)(i * i) / (float)this.getMaxPosition()), 32, i - 8);
                double d1 = Math.max(1.0D, d0 / (double)(i - j));
                this.setScrollAmount(this.getScrollAmount() + dragY * d1);
            }

            return true;
        } else {
            return false;
        }
    }



    @Override
    protected void updateScrollingState(double mouseX, double mouseY, int button) {
        this.scrolling = button == 0 && mouseX >= (double)this.getScrollbarPosition() && mouseX < (double)(this.getScrollbarPosition() + 6);
    }
}
