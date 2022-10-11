package com.klnon.quickstore.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;


public class StoreConfig {
    public static ForgeConfigSpec.DoubleValue distance;
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.ConfigValue<String> BanItems;
    public static ForgeConfigSpec.IntValue slot;
    public static ForgeConfigSpec.IntValue itemSlot;
    public static ForgeConfigSpec.ConfigValue<String> itemSlotBan;
    public static ForgeConfigSpec.BooleanValue singleEnable;
    public static ForgeConfigSpec.BooleanValue detailInfoEnable;
    public static ForgeConfigSpec.BooleanValue fullInfoEnable;



    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push("general");
        distance = COMMON_BUILDER.comment("距离").defineInRange("config.quickstore.general.distance", 8.0, 1.0, 100.0);
        BanItems = COMMON_BUILDER.comment("全局黑名单:以下物品不会被贮藏,示例 minecraft:stick").define("config.quickstore.general.banitems", "minecraft:stick");
        slot = COMMON_BUILDER.comment("玩家背包(36格)中有几格不参与贮藏,默认-1,全部参与贮藏").defineInRange("config.quickstore.general.slot", -1, -1, 35);
        itemSlot = COMMON_BUILDER.comment("玩家背包(36格)中有几格仅保存食物和火把,默认8,即玩家整个物品栏").defineInRange("config.quickstore.general.item.slot", 8, -1, 35);
        itemSlotBan = COMMON_BUILDER.comment("局部黑名单:以下物品在ItemSlot的格子内不会被贮藏,示例 minecraft:torch").define("config.quickstore.general.banitems", "minecraft:torch");
        singleEnable = COMMON_BUILDER.comment("是否允许堆叠数量为1的物品被整理,true:是,false:否").define("config.quickstore.general.singleenable", false);
        detailInfoEnable = COMMON_BUILDER.comment("是否显示详细信息,true:是,false:否").define("config.quickstore.general.detailinfoenable", true);
        fullInfoEnable = COMMON_BUILDER.comment("是否显示箱子是否满,true:是,false:否").define("config.quickstore.general.fullinfoenable", true);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}