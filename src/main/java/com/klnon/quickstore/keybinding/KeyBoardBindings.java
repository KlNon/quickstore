package com.klnon.quickstore.keybinding;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.klnon.quickstore.QuickStore.keyIsDown;
//import static com.klnon.quickstore.QuickStore.proxy;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBoardBindings {

    public static final KeyBinding storeKey = new KeyBinding("keys." + Utils.MOD_ID + ".store",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "keys." + Utils.MOD_ID + ".title");

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (storeKey.isPressed() && !keyIsDown) {
            QuickStore.keyIsDown = true;
        }
    }
}
