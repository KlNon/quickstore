package com.klnon.quickstore.config;

import net.minecraftforge.common.ForgeConfigSpec;


public class StoreConfig_Client {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final General general = new General();
    public static final Switches switches = new Switches();

    public static class General{
        public final ForgeConfigSpec.IntValue GREEN;
        public final ForgeConfigSpec.IntValue RED;
        public final ForgeConfigSpec.BooleanValue showOverlay;
        public final ForgeConfigSpec.DoubleValue outlineThickness;

        General(){
            BUILDER.push("general");

            GREEN = BUILDER
                    .comment("颜色(默认绿色)")
                    .defineInRange("green", 63232, 0, 100000000);

            RED = BUILDER
                    .comment("颜色(默认红色)")
                    .defineInRange("red", 16711680, 0, 100000000);

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
        public final ForgeConfigSpec.BooleanValue detailInfoEnable;
        public final ForgeConfigSpec.BooleanValue showDetailChestsEnable;
        public final ForgeConfigSpec.BooleanValue fullInfoEnable;
        Switches(){
            BUILDER.push("switches");
            detailInfoEnable = BUILDER
                    .comment("是否显示详细信息,true:是,false:否")
                    .define("detailInfoEnable", true);
            showDetailChestsEnable = BUILDER
                    .comment("是否显示每个物品详细储存信息,true:是,false:否")
                    .define("showDetailChestsEnable", false);
            fullInfoEnable = BUILDER
                    .comment("是否渲染显示箱子是否满,true:是,false:否")
                    .define("fullInfoEnable", true);
            BUILDER.pop();
        }
    }
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}