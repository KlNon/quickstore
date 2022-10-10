package com.klnon.quickstore.proxy;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.klnon.quickstore.config.StoreConfig;
import com.klnon.quickstore.container.ContainerInformation;
import com.klnon.quickstore.QuickStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ProxyClient extends Proxy {
    public static KeyBinding storeKey = new KeyBinding("keys.quickstore.store",Keyboard.KEY_V, "keys.quickstore.title");

    public void preInit() {
        ClientRegistry.registerKeyBinding(storeKey);
    }

    public void serverLoad() {}

    public void onKeyPressed() {
        EntityPlayerSP entityPlayerSP = (Minecraft.getMinecraft()).player;
        entityPlayerSP.playSound((SoundEvent)SoundEvent.REGISTRY.getObjectById(21), 1.0F, 1.5F);
    }

    public void onWorldRenderLast() {}

    public EntityPlayer getPlayer() {
        return (EntityPlayer)(Minecraft.getMinecraft()).player;
    }

    public void sendCommand() {
        (Minecraft.getMinecraft()).player.sendChatMessage("/quickstore");
    }

    public boolean isKeyPressed() {
        return storeKey.isPressed();
    }

    public boolean isKeyDown() {
        return storeKey.isKeyDown();
    }

    public void drawBoundingBox(Vec3d player_pos, Vec3d posA, Vec3d posB, boolean smooth, float width) {
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

    public void startDrawing(Vec3d pos) {
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
            Vec3d pos = QuickStore.player.getPositionEyes(evt.getPartialTicks());
            startDrawing(pos);
            for (int n = 0; n < QuickStore.nearbyContainers.size(); n++)
                drawBoundingBox(pos, ((ContainerInformation)QuickStore.nearbyContainers.get(n)).getBoundsStart(), ((ContainerInformation)QuickStore.nearbyContainers.get(n)).getBoundsEnd(), true, 6.0F);
            endDrawing();
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof net.minecraft.client.gui.inventory.GuiInventory)
            event.getButtonList().add(new GuiButton(964751345, (event.getGui()).width / 2 + 63, (event.getGui()).height / 2 - 14, 19, 14, "QS"));
        if (event.getGui() instanceof net.minecraft.client.gui.inventory.GuiCrafting)
            event.getButtonList().add(new GuiButton(964751345, (event.getGui()).width / 2 + 63, (event.getGui()).height / 2 - 14, 19, 14, "QS"));
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if ((event.getButton()).id == 964751345)
            storeIntoChests();
    }

    public void storeIntoChests() {
        QuickStore.player = getPlayer();
        QuickStore.currentItems = QuickStore.getCurrentItems(QuickStore.player);
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
                QuickStore.nearbyContainers = QuickStore.getNearbyContainers(QuickStore.player, 1.5F);
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
                List<Item> newItems = QuickStore.getCurrentItems(QuickStore.player);
                try {
                    QuickStore.player.sendMessage(new TextComponentTranslation("commands.quickstore.containers",QuickStore.nearbyContainers.size()));
                    if (newItems.size() < QuickStore.currentItems.size()) {
                        QuickStore.player.sendMessage(new TextComponentTranslation("commands.quickstore.stored", (QuickStore.currentItems.size() - newItems.size())));

                        if(StoreConfig.detailInfoEnable) {
                            for (Map.Entry<String, Integer> entry : QuickStore.storedItems.entrySet())
                                QuickStore.player.sendMessage(new TextComponentTranslation("commands.quickstore.storeditems", entry.getKey(), entry.getValue()));
                            QuickStore.storedItems.clear();
                        }
                        (Minecraft.getMinecraft()).player.playSound(Objects.requireNonNull(SoundEvent.REGISTRY.getObjectById(76)), 1.0F, 2.0F);
                        QuickStore.leftItemChecks = 0;
                    } else if (QuickStore.leftItemChecks <= 0) {
                        QuickStore.player.sendMessage(new TextComponentTranslation("commands.quickstore.nostored"));
                        (Minecraft.getMinecraft()).player.playSound(Objects.requireNonNull(SoundEvent.REGISTRY.getObjectById(76)), 1.0F, 0.55F);
                    }
                }catch (Exception e){
                    QuickStore.player.sendMessage(new TextComponentTranslation("commands.quickstore.wait"));
                }
            } else {
                QuickStore.lostItemsCheckCooldown -= 0.05F;
            }
    }
}
