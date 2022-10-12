package com.klnon.quickstore.keyBoard;

import com.klnon.quickstore.QuickStore;
import com.klnon.quickstore.config.StoreConfig;
import com.klnon.quickstore.container.ContainerInformation;
import com.klnon.quickstore.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.klnon.quickstore.QuickStore.keyIsDown;
//import static com.klnon.quickstore.QuickStore.proxy;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBoardInput {
    public static final KeyBinding storeKey = new KeyBinding("keys." + Utils.MOD_ID + ".store",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "keys." + Utils.MOD_ID + ".title");

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (storeKey.isPressed() && !keyIsDown) {
            assert Minecraft.getInstance().player != null;
            QuickStore.keyIsDown = true;
            //TODO 不知道 proxy是否该去除
//            proxy.onKeyPressed();
        }
    }
}
