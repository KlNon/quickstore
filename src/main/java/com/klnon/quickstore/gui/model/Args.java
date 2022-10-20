package com.klnon.quickstore.gui.model;


public class Args {
    public static double widthProp = 0.7;

    public static double heightProp = 1.0;


    //===========================================基础UI===================================================
    //基础Gui宽高
    public static int BASE_WIDTH = (int) (300 * widthProp);
    public static int BASE_HEIGHT = (int) (235 * heightProp);

    //基础Gui偏移值
    public static int BASE_X_OFFSET = -150;
    public static int BASE_Y_OFFSET = -118;

    //===========================================搜索框
    //搜索框
    public static int SEARCH_WIDTH = 8 * BASE_WIDTH / 10;//原先202
    public static int SEARCH_HEIGHT = 18;

    public static int SEARCH_X_OFFSET = BASE_X_OFFSET + 13;
    public static int SEARCH_Y_OFFSET = BASE_Y_OFFSET + 13;

    //===========================================物品列表框架
    public static int ITEM_LIST_OUT_WIDTH = BASE_WIDTH - 20;

    //===========================================物品列表
    public static int ITEM_LIST_WIDTH = ITEM_LIST_OUT_WIDTH - 10;
    public static int ITEM_LIST_HEIGHT = BASE_HEIGHT - 50;
    public static int ITEM_LIST_X_OFFSET = BASE_X_OFFSET + 15;
    public static int ITEM_LIST_Y_OFFSET = 10;
    //=============================物品
    public static int ITEM_NUM = 3;
    public static int ITEM_BORDER = 0;

    //整个物品元素(包括标志,名称,状态)的参数
    public static int ITEM_WIDTH = (ITEM_LIST_OUT_WIDTH - 10) / ITEM_NUM;
    public static int ITEM_HEIGHT = 55;
    //偏移量在ScrollingList的getEntryWithPosition中

    //物品标志参数
    public static int ITEM_PIC_X_OFFSET = 5;
    public static int ITEM_PIC_Y_OFFSET = 33;

    //物品名称参数
    public static int ITEM_NAME_X_OFFSET = 5;
    public static int ITEM_NAME_Y_OFFSET = 0;

    public static int ITEM_NAME_Y_OFFSET2 = ITEM_NAME_Y_OFFSET + 10;
    public static int ITEM_NAME_Y_OFFSET3 = ITEM_NAME_Y_OFFSET2 + 10;

    //物品状态参数
    public static int ITEM_STAT_X_OFFSET = 25;
    public static int ITEM_STAT_Y_OFFSET = ITEM_NAME_Y_OFFSET2 + 20;


    //===========================================侧边栏===================================================
    //侧边栏偏移值
    public static int SIDE_X_OFFSET = BASE_X_OFFSET + BASE_WIDTH + 5;
    public static int SIDE_Y_OFFSET = -90;
    public static int SIDE_WIDTH = (int) (BASE_WIDTH / 2 * widthProp);
    public static int SIDE_HEIGHT = (int) (2 * BASE_HEIGHT / 3 * heightProp);

    //侧边栏标题
    public static int SIDE_TITLE_X_OFFSET = SIDE_X_OFFSET + 10;
    public static int SIDE_TITLE_Y_OFFSET = SIDE_Y_OFFSET + 15;

    //添加物品按钮
    public static int SIDE_AI_X_OFFSET = SIDE_X_OFFSET + SIDE_WIDTH / 10;
    public static int SIDE_AI_Y_OFFSET = SIDE_Y_OFFSET + 30;
    public static int SIDE_AI_WIDTH = 4 * SIDE_WIDTH / 5;
    public static int SIDE_AI_HEIGHT = 20;

    //添加手上物品按钮
    public static int SIDE_AHI_X_OFFSET = SIDE_X_OFFSET + SIDE_WIDTH / 10;
    public static int SIDE_AHI_Y_OFFSET = SIDE_AI_Y_OFFSET + 30;
    public static int SIDE_AHI_WIDTH = 4 * SIDE_WIDTH / 5;
    public static int SIDE_AHI_HEIGHT = 20;


    //添加物品按钮
    public static int SIDE_SEARCH_X_OFFSET = SIDE_X_OFFSET + SIDE_WIDTH / 10;
    public static int SIDE_SEARCH_Y_OFFSET = SIDE_AHI_Y_OFFSET + 30;
    public static int SIDE_SEARCH_WIDTH = 4 * SIDE_WIDTH / 5;
    public static int SIDE_SEARCH_HEIGHT = 20;
    
    //帮助
    public static int SIDE_HELP_X_OFFSET = SIDE_X_OFFSET + SIDE_WIDTH / 10;
    public static int SIDE_HELP_Y_OFFSET = SIDE_Y_OFFSET + SIDE_HEIGHT - 30;
    public static int SIDE_HELP_WIDTH = SIDE_WIDTH / 3;
    public static int SIDE_HELP_HEIGHT = 20;


    //关闭
    public static int SIDE_CLOSE_WIDTH = SIDE_WIDTH / 3;
    public static int SIDE_CLOSE_HEIGHT = 20;
    public static int SIDE_CLOSE_X_OFFSET = SIDE_HELP_X_OFFSET + 8 * SIDE_WIDTH / 10 - SIDE_CLOSE_WIDTH;
    public static int SIDE_CLOSE_Y_OFFSET = SIDE_Y_OFFSET + SIDE_HEIGHT - 30;

    //===========================================添加物品===================================================
    public static int AIL_X_OFFSET = -BASE_WIDTH / 2 + 20;
    public static int AIL_Y_OFFSET = -10;
    public static int AIL_WIDTH = ITEM_LIST_WIDTH;
    public static int AIL_HEIGHT = ITEM_LIST_HEIGHT;


    public static int ADD_SEARCH_WIDTH = 3 * (BASE_WIDTH / 5);
    public static int ADD_SEARCH_HEIGHT = 18;
    public static int ADD_SEARCH_X_OFFSET = -BASE_WIDTH / 2 + 10;
    public static int ADD_SEARCH_Y_OFFSET = 85;


    //取消
    public static int ADD_CANCEL_WIDTH = BASE_WIDTH / 5;
    public static int ADD_CANCEL_HEIGHT = 20;
    public static int ADD_CANCEL_X_OFFSET = BASE_WIDTH / 2 - ADD_CANCEL_WIDTH - 10;
    public static int ADD_CANCEL_Y_OFFSET = 85;

    //===========================================具体添加页

    //物品图片
    public static int ADD_PIC_X_OFFSET = -BASE_WIDTH / 2 + BASE_WIDTH / 10;
    public static int ADD_PIC_Y_OFFSET = -60;

    //物品名称
    public static int AIN_X_OFFSET = ADD_PIC_X_OFFSET + 20;
    public static int AIN_Y_OFFSET = -60;
    //添加物品按钮
    public static int AI_WIDTH = 2 * (BASE_WIDTH / 5);
    public static int AI_HEIGHT = 20;
    public static int AI_X_OFFSET = -BASE_WIDTH / 2 + BASE_WIDTH / 10;
    public static int AI_Y_OFFSET = 85;


    //取消
    public static int CANCEL_WIDTH = BASE_WIDTH / 3;
    public static int CANCEL_HEIGHT = 20;
    public static int CANCEL_X_OFFSET = BASE_WIDTH / 2 - CANCEL_WIDTH - BASE_WIDTH / 10;
    public static int CANCEL_Y_OFFSET = 85;

    //===========================================编辑===================================================

    //物品图片
    public static int EDIT_PIC_X_OFFSET = BASE_X_OFFSET +10;
    public static int EDIT_PIC_Y_OFFSET = -90;

    //修改名字的栏位
    public static int NAME_EDIT_X_OFFSET = EDIT_PIC_X_OFFSET + 20;
    public static int NAME_EDIT_Y_OFFSET = -90;
    public static int NAME_EDIT_WIDTH = 3 * BASE_WIDTH / 5;
    public static int NAME_EDIT_HEIGHT = 18;

    //===========================================侧边栏
    //编辑删除按钮
    public static int SED_WIDTH = 4 * SIDE_WIDTH / 5;
    public static int SED_HEIGHT = 20;
    public static int SED_X_OFFSET = SIDE_X_OFFSET + SIDE_WIDTH/10;
    public static int SED_Y_OFFSET = SIDE_Y_OFFSET + 30;

    //编辑删除按钮
    public static int SES_WIDTH = 4 * SIDE_WIDTH / 5;
    public static int SES_HEIGHT = 20;
    public static int SES_X_OFFSET = SIDE_X_OFFSET + SIDE_WIDTH/10;
    public static int SES_Y_OFFSET = SED_Y_OFFSET + 30;


    //编辑删除按钮
    public static int SEC_WIDTH = 4 * SIDE_WIDTH / 5;
    public static int SEC_HEIGHT = 20;
    public static int SEC_X_OFFSET = SIDE_X_OFFSET + SIDE_WIDTH/10;
    public static int SEC_Y_OFFSET = SES_Y_OFFSET + 30;

    //===========================================帮助===================================================
    public static int HELP_WIDTH = 2 * BASE_WIDTH / 3;
    public static int HELP_HEIGHT = 20;
    public static int HELP_X_OFFSET = -BASE_WIDTH / 3;
    public static int HELP_Y_OFFSET = 80;

    public static int HELP_TITLE_X_OFFSET = -176;

}
