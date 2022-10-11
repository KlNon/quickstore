package com.klnon.quickstore.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;


public class StoreConfig {
    public static ForgeConfigSpec.IntValue distance;
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
        distance = COMMON_BUILDER.comment("距离").defineInRange("config.quickstore.general.distance", 8, -1, 35);
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

//    @Config.Comment("全局黑名单:以下物品不会被贮藏,示例 minecraft:stick") // 有了这个就会多一个注释。
//    @Config.LangKey("config.quickstore.general.banitems") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节

//    @Config.Comment("玩家背包(36格)中有几格不参与贮藏,默认-1,全部参与贮藏") // 有了这个就会多一个注释。
//    @Config.LangKey("config.quickstore.general.slot") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
//    @Config.Name("Slot") // 默认配置选项名是字段名，如果需要别的名字就用这个。
//    @Config.RangeInt(min = -1, max = 36) // 整数值支持限定范围。
//    @Config.RequiresWorldRestart // meta 标记，代表需要重进存档才会生效

//    @Config.Comment("玩家背包(36格)中有几格仅保存食物和火把,默认8,即玩家整个物品栏") // 有了这个就会多一个注释。
//    @Config.LangKey("config.quickstore.general.item.slot") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
//    @Config.Name("ItemSlot") // 默认配置选项名是字段名，如果需要别的名字就用这个。
//    @Config.RangeInt(min = -1, max = 36) // 整数值支持限定范围。
//    @Config.RequiresWorldRestart // meta 标记，代表需要重进存档才会生效
//    public static int itemSlot = 8;

//    @Config.Comment("局部黑名单:以下物品在ItemSlot的格子内不会被贮藏,示例 minecraft:torch") // 有了这个就会多一个注释。
//    @Config.LangKey("config.quickstore.general.banitems") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节


//    @Config.Comment("是否允许堆叠数量为1的物品被整理,true:是,false:否") // 有了这个就会多一个注释。
//    @Config.LangKey("config.quickstore.general.singleenable") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
//    public static boolean singleEnable = false;


//    @Config.Comment("是否显示详细储存信息,true:是,false:否") // 有了这个就会多一个注释。
//    @Config.LangKey("config.quickstore.general.detailinfoenable") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
//    public static boolean detailInfoEnable = true;
//
//
//    @Config.Comment("是否显示箱子是否满了,true:是,false:否") // 有了这个就会多一个注释。
//    @Config.LangKey("config.quickstore.general.fullinfoenable") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
//    public static boolean fullInfoEnable = true;
//    @Config.RequiresMcRestart // meta 标记，代表需要重启游戏才会生效
//    public static int foo = 0;

//
//    //同步文件
//    @Mod.EventBusSubscriber(modid = "quickstore")
//    public static class ConfigSyncHandler {
//        @SubscribeEvent
//        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
//            if (event.getModID().equals("quickstore")) {
//                ConfigManager.sync("quickstore", Config.Type.INSTANCE);
//            }
//        }
//    }

}