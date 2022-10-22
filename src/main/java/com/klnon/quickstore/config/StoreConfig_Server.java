package com.klnon.quickstore.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class StoreConfig_Server {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final General general = new General();
    public static final Switches switches = new Switches();
    public static class General{
        public final ForgeConfigSpec.IntValue distance;
        public final ForgeConfigSpec.IntValue searchDistance;
        public final ForgeConfigSpec.IntValue slot;
        public final ForgeConfigSpec.IntValue itemSlot;
        public final ForgeConfigSpec.IntValue checkSlot;
        public final ForgeConfigSpec.ConfigValue<String> BanItems;
        public final ForgeConfigSpec.ConfigValue<String> itemSlotBan;
        public final ForgeConfigSpec.BooleanValue IGNORE_ITEM_DAMAGE;

        General(){
            BUILDER.push("general");
            distance = BUILDER
                    .comment("距离")
                    .defineInRange("distance", 1,0,9);


            searchDistance = BUILDER
                    .comment("搜索距离")
                    .defineInRange("searchDistance", 2,0,9);

            BanItems = BUILDER
                    .comment("全局黑名单:以下物品不会被贮藏,示例 minecraft:stick")
                    .define("BanItems", "minecraft:stick");

            slot = BUILDER
                    .comment("玩家背包(36格)中有几格不参与贮藏,默认-1,全部参与贮藏")
                    .defineInRange("slot", 8, -1, 35);

            itemSlot = BUILDER
                    .comment("玩家背包(36格)中有几格仅保存食物和火把,默认8,即玩家整个物品栏")
                    .defineInRange("itemSlot", 8, -1, 35);


            checkSlot = BUILDER
                    .comment("检查格子数大于checkSlot的箱子")
                    .defineInRange("checkSlot", 12, 0, 100);

            itemSlotBan = BUILDER
                    .comment("局部黑名单:以下物品在ItemSlot的格子内不会被贮藏,示例 minecraft:torch")
                    .define("itemSlotBan", "minecraft:torch");

            IGNORE_ITEM_DAMAGE = BUILDER
                    .comment("是否忽略物品的耐久度")
                    .define("IGNORE_ITEM_DAMAGE", false);

            BUILDER.pop();
        }
    }

    public static class Switches{
        public final ForgeConfigSpec.BooleanValue singleEnable;
        Switches(){
            BUILDER.push("switches");
            singleEnable = BUILDER
                    .comment("是否允许堆叠数量为1的物品被整理,true:是,false:否")
                    .define("singleEnable", false);
            BUILDER.pop();
        }
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    static {

    }
}
