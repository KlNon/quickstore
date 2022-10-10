package com.ekincan.quickstore.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Config(modid = "quickstore") // 相当于入口标记一样的东西。`modid` 一定要填你的 mod id。
@Config.LangKey("config.quickstore.general") // 这个用于本地化，稍后会讲
public class StoreConfig {

    @Config.Comment("全局黑名单:以下物品不会被贮藏,示例 minecraft:stick") // 有了这个就会多一个注释。
    @Config.LangKey("config.quickstore.general.banitems") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
    public static String[] BanItems = new String[] { "" };

    @Config.Comment("是否允许堆叠数量为1的物品被整理,true:是,false:否") // 有了这个就会多一个注释。
    @Config.LangKey("config.quickstore.general.singleenable") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
    public static boolean singleEnable = false;

    @Config.Comment("玩家背包(36格)中有几格不参与贮藏,默认0") // 有了这个就会多一个注释。
    @Config.LangKey("config.quickstore.general.slot") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
    @Config.Name("Slot") // 默认配置选项名是字段名，如果需要别的名字就用这个。
    @Config.RangeInt(min = 0, max = 36) // 整数值支持限定范围。
    @Config.RequiresWorldRestart // meta 标记，代表需要重进存档才会生效
    public static int slot = 0;

    @Config.Comment("玩家背包(36格)中有几格仅保存食物和火把,默认8,即玩家整个物品栏") // 有了这个就会多一个注释。
    @Config.LangKey("config.quickstore.general.item.slot") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
    @Config.Name("ItemSlot") // 默认配置选项名是字段名，如果需要别的名字就用这个。
    @Config.RangeInt(min = 0, max = 36) // 整数值支持限定范围。
    @Config.RequiresWorldRestart // meta 标记，代表需要重进存档才会生效
    public static int itemSlot = 8;

    @Config.Comment("局部黑名单:以下物品在ItemSlot的格子内不会被贮藏,示例 minecraft:torch") // 有了这个就会多一个注释。
    @Config.LangKey("config.quickstore.general.banitems") // 供配置 GUI 界面使用的本地化键，参阅“可视化配置文件编辑界面”一节
    public static String[] itemSlotBan = new String[] { "minecraft:torch" };

//    @Config.RequiresMcRestart // meta 标记，代表需要重启游戏才会生效
//    public static int foo = 0;


    //同步文件
    @Mod.EventBusSubscriber(modid = "quickstore")
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("quickstore")) {
                ConfigManager.sync("quickstore", Config.Type.INSTANCE);
            }
        }
    }

}