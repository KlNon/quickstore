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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.klnon.quickstore.QuickStore.keyIsDown;
import static com.klnon.quickstore.QuickStore.proxy;

/**
 * @author KlNon
 * @version 1.0
 * @className KeyBoardInput
 * @description
 * @date 2022/10/11 10:34
 **/
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBoardInput {
    public static final KeyBinding storeKey = new KeyBinding("keys."+Utils.MOD_ID+".store",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "keys."+Utils.MOD_ID+".title");

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (storeKey.isPressed()&& !keyIsDown) {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("message." + Utils.MOD_ID + ".press"), Minecraft.getInstance().player.getUniqueID());
            proxy.onKeyPressed();
        }
    }

    public PlayerEntity getPlayer() {
        return (PlayerEntity)(Minecraft.getInstance()).player;
    }


    public void sendCommand() {
        (Minecraft.getInstance()).player.sendChatMessage("/quickstore");
    }


    public boolean isKeyPressed() {
        return storeKey.isPressed();
    }

    public boolean isKeyDown() {
        return storeKey.isKeyDown();
    }


    public void drawBoundingBox(Vector3d player_pos, Vector3d posA, Vector3d posB, boolean smooth, float width) {
        Color c = new Color(156, 255, 149, 255);
        GL11.glColor4d(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        GL11.glLineWidth(width);
        GL11.glDepthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        double dx = Math.abs(posA.x - posB.x);
        double dy = Math.abs(posA.y - posB.y);
        double dz = Math.abs(posA.z - posB.z);
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z + dz).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        bufferBuilder.pos(posA.x + dx, posA.y + dy, posA.z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
        tessellator.draw();
    }


    public void startDrawing(Vector3d pos) {
        GL11.glPushAttrib(8192);
        GL11.glDisable(2884);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslated(-pos.x, -pos.y, -pos.z);
        Color c = new Color(156, 255, 149, 255);
        GL11.glColor4d(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        GL11.glLineWidth(15.0F);
        GL11.glDepthMask(false);
    }


    public void endDrawing() {
        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }


    @SubscribeEvent
    public void onWorldRenderLast(RenderWorldLastEvent evt) {
        if (QuickStore.player == null || QuickStore.nearbyContainers == null || QuickStore.nearbyContainers.size() == 0)
            return;
        if (isKeyDown()) {
            //TODO 可能出错
            //evt.getPartialTicks()
            Vector3d pos = QuickStore.player.getLook(evt.getPartialTicks());
            startDrawing(pos);
            for (int n = 0; n < QuickStore.nearbyContainers.size(); n++)
                drawBoundingBox(pos, ((ContainerInformation)QuickStore.nearbyContainers.get(n)).getBoundsStart(), ((ContainerInformation)QuickStore.nearbyContainers.get(n)).getBoundsEnd(), true, 6.0F);
            endDrawing();
        }
    }

    //TODO 添加Gui
//    @SubscribeEvent
//    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
//        //TODO 可能出错
//        if (event.getGui() instanceof net.minecraft.client.gui.screen.inventory.InventoryScreen)
//            event.getButtonList().add(new GuiButton(964751345, (event.getGui()).width / 2 + 63, (event.getGui()).height / 2 - 14, 19, 14, "QS"));
//        if (event.getGui() instanceof net.minecraft.client.gui.screen.inventory.InventoryScreen)
//            event.getButtonList().add(new GuiButton(964751345, (event.getGui()).width / 2 + 63, (event.getGui()).height / 2 - 14, 19, 14, "QS"));
//    }


//    @SubscribeEvent
//    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
//        if ((event.getButton()).id == 964751345)
//            storeIntoChests();
//    }


    public void storeIntoChests() {
        QuickStore.player = getPlayer();
        QuickStore.currentItems = Utils.getCurrentItems(QuickStore.player);
        QuickStore.leftItemChecks = 1;
        QuickStore.lostItemsCheckCooldown = 0.75F;
        sendCommand();
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.PlayerTickEvent event) {
        if (isKeyDown()) {
            QuickStore.keyIsDown = true;
            QuickStore.player = getPlayer();
            if (QuickStore.nextUpdateCooldown <= 0.0F) {
                QuickStore.nearbyContainers = Utils.getNearbyContainers(QuickStore.player, 1.5F);
                QuickStore.nextUpdateCooldown = 0.14F;
            } else {
                QuickStore.nextUpdateCooldown -= 0.05F;
            }
        } else if (QuickStore.keyIsDown) {
            QuickStore.keyIsDown = false;
            storeIntoChests();
        }
        if (QuickStore.currentItems != null && QuickStore.leftItemChecks > 0)
            if (QuickStore.lostItemsCheckCooldown <= 0.0F) {
                QuickStore.lostItemsCheckCooldown = 0.2F;
                QuickStore.leftItemChecks--;
                List<Item> newItems = Utils.getCurrentItems(QuickStore.player);
                try {
                    QuickStore.player.sendMessage(new TranslationTextComponent("commands.quickstore.containers",QuickStore.nearbyContainers.size()),QuickStore.player.getUniqueID());
                    if (newItems.size() < QuickStore.currentItems.size()) {
                        QuickStore.player.sendMessage(new TranslationTextComponent("commands.quickstore.stored", (QuickStore.currentItems.size() - newItems.size())),QuickStore.player.getUniqueID());

                        if(StoreConfig.detailInfoEnable) {
                            for (Map.Entry<String, Integer> entry : QuickStore.storedItems.entrySet())
                                QuickStore.player.sendMessage(new TranslationTextComponent("commands.quickstore.storeditems", entry.getKey(), entry.getValue()),QuickStore.player.getUniqueID());
                            QuickStore.storedItems.clear();
                        }
                        //TODO 添加声音
//                        (Minecraft.getInstance()).player.playSound(Objects.requireNonNull(SoundEvent.REGISTRY.getObjectById(76)), 1.0F, 2.0F);
                        QuickStore.leftItemChecks = 0;
                    } else if (QuickStore.leftItemChecks <= 0) {
                        QuickStore.player.sendMessage(new TranslationTextComponent("commands.quickstore.nostored"),QuickStore.player.getUniqueID());
//                        (Minecraft.getInstance()).player.playSound(Objects.requireNonNull(SoundEvent.REGISTRY.getObjectById(76)), 1.0F, 0.55F);
                    }
                }catch (Exception e){
                    QuickStore.player.sendMessage(new TranslationTextComponent("commands.quickstore.wait"),QuickStore.player.getUniqueID());
                }
            } else {
                QuickStore.lostItemsCheckCooldown -= 0.05F;
            }
    }
}
