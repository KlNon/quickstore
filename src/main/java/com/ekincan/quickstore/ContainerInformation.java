package com.ekincan.quickstore;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ContainerInformation {
    public TileEntityChest chest1;

    public TileEntityChest chest2;

    public IInventory inventoryObject;

    public TileEntity blockPositionOfInventory;

    public int ignoredSlot = -100;

    public TileEntityChest[] getChests() {
        if (this.chest2 != null) {
            TileEntityChest[] arrayOfTileEntityChest = new TileEntityChest[2];
            arrayOfTileEntityChest[0] = this.chest1;
            arrayOfTileEntityChest[1] = this.chest2;
            return arrayOfTileEntityChest;
        }
        TileEntityChest[] chests = new TileEntityChest[1];
        chests[0] = this.chest1;
        return chests;
    }

    public IInventory[] getInventories() {
        if (this.inventoryObject != null) {
            IInventory[] arrayOfIInventory = new IInventory[1];
            arrayOfIInventory[0] = this.inventoryObject;
            return arrayOfIInventory;
        }
        if (this.chest2 != null) {
            IInventory[] arrayOfIInventory = new IInventory[2];
            arrayOfIInventory[0] = (IInventory)this.chest1;
            arrayOfIInventory[1] = (IInventory)this.chest2;
            return arrayOfIInventory;
        }
        IInventory[] chests = new IInventory[1];
        chests[0] = (IInventory)this.chest1;
        return chests;
    }

    public Vec3d getBoundsStart() {
        if (this.blockPositionOfInventory != null) {
            BlockPos pos = this.blockPositionOfInventory.getPos();
            return new Vec3d(pos.getX(), pos.getY() + 1.63D, pos.getZ());
        }
        if (this.chest1 != null) {
            BlockPos pos = this.chest1.getPos();
            return new Vec3d(pos.getX(), pos.getY() + 1.63D, pos.getZ());
        }
        if (this.chest2 != null) {
            BlockPos pos = this.chest2.getPos();
            return new Vec3d(pos.getX(), pos.getY() + 1.63D, pos.getZ());
        }
        return new Vec3d(0.0D, 101.63D, 0.0D);
    }

    public Vec3d getBoundsEnd() {
        if (this.blockPositionOfInventory != null) {
            BlockPos pos = this.blockPositionOfInventory.getPos();
            return new Vec3d((pos.getX() + 1), pos.getY() + 2.63D, (pos.getZ() + 1));
        }
        if (this.chest1 != null && this.chest2 != null) {
            BlockPos pos = this.chest2.getPos();
            return new Vec3d((pos.getX() + 1), pos.getY() + 2.63D, (pos.getZ() + 1));
        }
        if (this.chest1 != null) {
            BlockPos pos = this.chest1.getPos();
            return new Vec3d((pos.getX() + 1), pos.getY() + 2.63D, (pos.getZ() + 1));
        }
        if (this.chest2 != null) {
            BlockPos pos = this.chest2.getPos();
            return new Vec3d((pos.getX() + 1), pos.getY() + 2.63D, (pos.getZ() + 1));
        }
        return new Vec3d(1.0D, 102.63D, 1.0D);
    }

    public Vec3d getLidLocation() {
        Vec3d from = getBoundsStart();
        Vec3d to = getBoundsEnd();
        return new Vec3d(from.x + (to.x - from.x) * 0.5D, from.y + (to.y - from.y) * 0.5D + 0.1D, from.z + (to.z - from.z) * 0.5D);
    }
}
