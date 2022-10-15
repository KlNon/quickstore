package com.klnon.quickstore.model;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;

public class ContainerInformation {
    public ChestTileEntity chest1;

    public ChestTileEntity chest2;

    public IInventory inventoryObject;

    public TileEntity blockPositionOfInventory;

    public boolean isFull=false;

    public ChestTileEntity[] getChests() {
        if (this.chest2 != null) {
            ChestTileEntity[] arrayOfChestTileEntity = new ChestTileEntity[2];
            arrayOfChestTileEntity[0] = this.chest1;
            arrayOfChestTileEntity[1] = this.chest2;
            return arrayOfChestTileEntity;
        }
        ChestTileEntity[] chests = new ChestTileEntity[1];
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
            arrayOfIInventory[0] = this.chest1;
            arrayOfIInventory[1] = this.chest2;
            return arrayOfIInventory;
        }
        if (this.chest1 != null){
            IInventory[] chests = new IInventory[1];
            chests[0] = this.chest1;
            return chests;
        }
        return null;
    }

    public BlockPos getPos(){
        if (this.blockPositionOfInventory != null) {
            return this.blockPositionOfInventory.getPos();
        }
        return this.chest1.getPos();
    }
}
