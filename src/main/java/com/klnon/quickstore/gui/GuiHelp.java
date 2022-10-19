package com.klnon.quickstore.gui;

import com.klnon.quickstore.gui.utils.GuiBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.klnon.quickstore.gui.model.Args.*;

public class GuiHelp extends GuiBase {
    public GuiHelp() {
        super(false);
        this.setSize(380, 210);
    }

    private List<LinedText> areas = new ArrayList<>();

    @Override
    public void init() {
        super.init();

        areas.clear();
        areas.add(new LinedText("quickstore.help.gui"));
        areas.add(new LinedText("quickstore.help.gui2"));
        areas.add(new LinedText("quickstore.help.gui3"));
        areas.add(new LinedText("quickstore.help.gui4"));
        areas.add(new LinedText("quickstore.help.gui5"));
        areas.add(new LinedText("quickstore.help.gui6"));

        this.addButton(new Button((getWidth() / 2) + HELP_X_OFFSET, (getHeight() / 2) + HELP_Y_OFFSET, HELP_WIDTH, HELP_HEIGHT, new TranslationTextComponent("quickstore.single.close"), b -> {
            this.closeScreen();
            Minecraft.getInstance().displayGuiScreen(new GuiSelection());
        }));
    }

    @Override
    public void renderExtra(MatrixStack stack, int x, int y, float partialTicks) {
        float lineY = (getHeight() / 2f) - 85;
        for (LinedText linedText : areas) {
            for (String line : linedText.getLines()) {
                lineY += 12;
                this.getFontRender().drawStringWithShadow(stack, line,(getWidth() / 2f) + HELP_TITLE_X_OFFSET, lineY, Color.WHITE.getRGB());
            }
            lineY += 10;
        }
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    public ResourceLocation getBackground() {
        return BG_LARGE;
    }

    @Override
    public String title() {
        return I18n.format("quickstore.single.help");
    }

    private static class LinedText {
        private String[] lines;

        LinedText(String key) {
            this.lines = I18n.format(key).split("\\R");
        }

        String[] getLines() {
            return lines;
        }
    }
}
