package com.klnon.quickstore.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ProxyServer extends Proxy {
    public void preInit() {}

    public void serverLoad() {}

    public void onKeyPressed() {}

    public void onWorldRenderLast() {}

    public EntityPlayer getPlayer() {
        return null;
    }

    public void sendCommand() {}

    public boolean isKeyPressed() {
        return false;
    }

    public boolean isKeyDown() {
        return false;
    }

    public void drawBoundingBox(Vec3d player_pos, Vec3d posA, Vec3d posB, boolean smooth, float width) {}

    public void startDrawing(Vec3d pos) {}

    public void endDrawing() {}
}
