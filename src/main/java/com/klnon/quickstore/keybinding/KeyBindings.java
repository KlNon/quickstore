package com.klnon.quickstore.keybinding;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.gui.GuiSelection;
import com.klnon.quickstore.utils.Utils_Client;
import com.klnon.quickstore.utils.Utils_Server;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBindings {

    private static final String CATEGORY = "keys." + QuickStore.MOD_ID + ".title";

    private static List<KeyActionable> keyBindings = new ArrayList<>();

    public static KeyActionable toggleStore = new KeyActionable(GLFW.GLFW_KEY_V, I18n.format("keys." + QuickStore.MOD_ID + ".title"), Utils_Server::sendCommand);

    public static KeyActionable toggleSearch = new KeyActionable(GLFW.GLFW_KEY_G, I18n.format("keys." + QuickStore.MOD_ID + ".search"),Utils_Server::sendSearchCommand);

    //TODO gui打开按钮
//    public static KeyActionable toggleGui = new KeyActionable(GLFW.GLFW_KEY_B, I18n.format("keys." + QuickStore.MOD_ID + ".gui"), () -> Minecraft.getInstance().displayGuiScreen( new GuiSelectionScreen() ));
    public static final KeyBinding quickSee = new KeyBinding("keys." + QuickStore.MOD_ID + ".quicksee",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            CATEGORY);

    public static final KeyBinding quickSearch = new KeyBinding("keys." + QuickStore.MOD_ID + ".quicksearch",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            CATEGORY);

    public static void setup() {
        keyBindings.add(toggleStore);
        keyBindings.add(new KeyActionable(quickSee, Utils_Client::toggleQuickSee));
        keyBindings.add(toggleSearch);
        keyBindings.add(new KeyActionable(quickSearch, () -> Minecraft.getInstance().displayGuiScreen( new GuiSelection() )));
//        keyBindings.add(toggleGui);

        keyBindings.forEach(e -> ClientRegistry.registerKeyBinding(e.getKeyBinding()));
    }


    @SubscribeEvent
    public static void eventInput(TickEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.phase == TickEvent.Phase.START || Minecraft.getInstance().currentScreen != null || Minecraft.getInstance().world == null)
            return;

        keyBindings.forEach( e -> {
            if( e.keyBinding.isPressed() )
                e.onPress.run();
        });
    }


    public static final class KeyActionable {
        private KeyBinding keyBinding;
        private Runnable onPress;

        KeyActionable(int key, String description, Runnable onPress) {
            this.onPress = onPress;
            this.keyBinding = new KeyBinding(description, key, CATEGORY);
        }
        KeyActionable(KeyBinding keyBinding, Runnable onPress) {
            this.onPress = onPress;
            this.keyBinding = keyBinding;
        }

        public KeyBinding getKeyBinding() {
            return keyBinding;
        }
    }
}
