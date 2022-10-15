package com.klnon.quickstore.config;

import net.minecraftforge.common.ForgeConfigSpec;


public class StoreConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final General general = new General();
    public static final Switches switches = new Switches();
    public static class General{
        public final ForgeConfigSpec.IntValue distance;
        public final ForgeConfigSpec.IntValue slot;
        public final ForgeConfigSpec.IntValue itemSlot;
        public final ForgeConfigSpec.IntValue checkSlot;
        public final ForgeConfigSpec.ConfigValue<String> BanItems;
        public final ForgeConfigSpec.ConfigValue<String> itemSlotBan;
        public final ForgeConfigSpec.BooleanValue showOverlay;
        public final ForgeConfigSpec.DoubleValue outlineThickness;

        General(){
            BUILDER.push("general");
            distance = BUILDER
                    .comment("距离")
                    .defineInRange("distance", 1,0,9);

            BanItems = BUILDER
                    .comment("全局黑名单:以下物品不会被贮藏,示例 minecraft:stick")
                    .define("BanItems", "minecraft:stick");

            slot = BUILDER
                    .comment("玩家背包(36格)中有几格不参与贮藏,默认-1,全部参与贮藏")
                    .defineInRange("slot", -1, -1, 35);

            itemSlot = BUILDER
                    .comment("玩家背包(36格)中有几格仅保存食物和火把,默认8,即玩家整个物品栏")
                    .defineInRange("itemSlot", 8, -1, 35);


            checkSlot = BUILDER
                    .comment("检查格子数大于checkSlot的箱子")
                    .defineInRange("checkSlot", 12, 0, 100);

            itemSlotBan = BUILDER
                    .comment("局部黑名单:以下物品在ItemSlot的格子内不会被贮藏,示例 minecraft:torch")
                    .define("itemSlotBan", "minecraft:torch");

            showOverlay = BUILDER
                    .comment("启用或关闭叠加层")
                    .define("showOverlay", true);

            outlineThickness = BUILDER
                    .comment("这允许您设置自己的轮廓粗细,最大5")
                    .defineInRange("outlineThickness", 10.0, 1.0, 20.0);

            BUILDER.pop();
        }
    }

    public static class Switches{
        public final ForgeConfigSpec.BooleanValue singleEnable;
        public final ForgeConfigSpec.BooleanValue detailInfoEnable;
        public final ForgeConfigSpec.BooleanValue showDetailChestsEnable;
        public final ForgeConfigSpec.BooleanValue fullInfoEnable;
        Switches(){
            BUILDER.push("switches");
            singleEnable = BUILDER
                    .comment("是否允许堆叠数量为1的物品被整理,true:是,false:否")
                    .define("singleEnable", false);
            detailInfoEnable = BUILDER
                    .comment("是否显示详细信息,true:是,false:否")
                    .define("detailInfoEnable", true);
            showDetailChestsEnable = BUILDER
                    .comment("是否显示每个物品详细储存信息,true:是,false:否")
                    .define("showDetailChestsEnable", true);
            fullInfoEnable = BUILDER
                    .comment("是否显示箱子是否满,true:是,false:否")
                    .define("fullInfoEnable", true);
            BUILDER.pop();
        }
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}