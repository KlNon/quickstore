package com.ekincan.quickstore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public abstract class Proxy {
    public abstract void preInit();

    public abstract void serverLoad();

    public abstract void onKeyPressed();

    public abstract void onWorldRenderLast();

    public abstract EntityPlayer getPlayer();

    public abstract void sendCommand();

    public abstract boolean isKeyPressed();

    public abstract boolean isKeyDown();

    public abstract void drawBoundingBox(Vec3d paramVec3d1, Vec3d paramVec3d2, Vec3d paramVec3d3, boolean paramBoolean, float paramFloat);

    public abstract void startDrawing(Vec3d paramVec3d);

    public abstract void endDrawing();
}
