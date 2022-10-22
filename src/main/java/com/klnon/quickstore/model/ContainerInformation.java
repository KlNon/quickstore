package com.klnon.quickstore.model;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;

public class ContainerInformation {
    public ChestTileEntity chest;

    public IInventory inventoryObject;

    public TileEntity blockPositionOfInventory;

    public boolean isFull=false;

    public ChestTileEntity[] getChests() {
        ChestTileEntity[] chests = new ChestTileEntity[1];
        chests[0] = this.chest;
        return chests;
    }

    public IInventory[] getInventories() {
        if (this.inventoryObject != null) {
            IInventory[] arrayOfIInventory = new IInventory[1];
            arrayOfIInventory[0] = this.inventoryObject;
            return arrayOfIInventory;
        }
        if (this.chest != null){
            IInventory[] chests = new IInventory[1];
            chests[0] = this.chest;
            return chests;
        }
        return null;
    }

    public BlockPos getPos(){
        if (this.blockPositionOfInventory != null) {
            return this.blockPositionOfInventory.getPos();
        }
        return this.chest.getPos();
    }
}
