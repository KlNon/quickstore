package com.klnon.quickstore.model;

import net.minecraft.util.math.BlockPos;

import java.util.List;

public class ItemInfo {
    private static List<BlockPos> position;
    private int amount;

    public ItemInfo(List<BlockPos> position, int amount) {
        ItemInfo.position = position;
        this.amount = amount;
    }

    public List<BlockPos> getPosition() {
        return position;
    }

    public void setPosition(List<BlockPos> position) {
        ItemInfo.position = position;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public boolean isSamePosition(BlockPos position1) {
        for (BlockPos pos: position) {
            if (pos.equals(position1)) {
                return true;
            }
        }
        return false;
    }

    public String posToString(){
        StringBuilder sb = new StringBuilder();
        for (BlockPos pos: position) {
            sb.append("[X:").append(pos.getX()).append(" Y:").append(pos.getY()).append(" Z:").append(pos.getZ()).append("]");
        }
        return sb.toString();
    }
}
