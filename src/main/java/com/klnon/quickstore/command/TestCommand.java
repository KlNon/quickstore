package com.klnon.quickstore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author KlNon
 * @version 1.0
 * @className TestCommand
 * @description
 * @date 2022/10/11 1:01
 **/
public class TestCommand implements Command<CommandSource> {
    public static TestCommand instance = new TestCommand();

    @Override
    public int run(CommandContext<CommandSource> context) {

        context.getSource().sendFeedback(new TranslationTextComponent("cmd." + "quickstore" + ".hello"), false);
        return 0;
    }
}