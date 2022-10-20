package com.klnon.quickstore.gui.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.klnon.quickstore.QuickStore;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JsonStore
{
    private static final String FILE = "item_store.json";
    private static final String CONFIG_DIR = Minecraft.getInstance().gameDir + "/config/";
    private static final Random rand = new Random();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean created = false;
    private File jsonFile;

    // This should only be initialised once
    public JsonStore()
    {
        File configDir = new File(CONFIG_DIR, QuickStore.MOD_ID);

        if( !configDir.exists() )
            configDir.mkdirs();

        jsonFile = new File(CONFIG_DIR + QuickStore.MOD_ID, FILE);
        if( !jsonFile.exists() ) {
            this.created = true;

            // Create a file with nothing inside
            this.write(new ArrayList<ItemData.SerializableItemData>());
        }
    }

    public void write(ArrayList<ItemData> ItemData) {
        List<ItemData.SerializableItemData> simpleItemData = new ArrayList<>();
        ItemData.forEach( e -> simpleItemData.add(new ItemData.SerializableItemData(e.getEntryName(), e.getRegName(), e.isFinder(), e.getOrder())) );

        this.write(simpleItemData);
    }

    private void write(List<ItemData.SerializableItemData> simpleItemData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile)))
        {
            gson.toJson(simpleItemData, writer);
        }
        catch (IOException e) {
            QuickStore.logger.log(Level.ERROR, "Failed to write json data to " + FILE);
        }
    }

    public List<ItemData.SerializableItemData> read() {
        if( !jsonFile.exists() )
            return new ArrayList<>();

        try
        {
            Type type = new TypeToken<List<ItemData.SerializableItemData>>() {}.getType();
            try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile)))
            {
                return gson.fromJson(reader, type);
            }
            catch (JsonSyntaxException ex) {
                QuickStore.logger.log(Level.ERROR, "Failed to read json data from " + FILE);
            }
        }
        catch (IOException e)
        {
            QuickStore.logger.log(Level.ERROR, "Failed to read json data from " + FILE);
        }

        return new ArrayList<>();
    }

    //加载默认内容
    public List<ItemData.SerializableItemData> populateDefault() {
        List<ItemData.SerializableItemData> itemData = new ArrayList<>();
        // 此处是加载默认的东西

        Tags.Items.CHESTS_WOODEN.getAllElements().forEach(e -> {
            if( e.getRegistryName() == null )
                return;

            itemData.add(new ItemData.SerializableItemData(new TranslationTextComponent(e.getTranslationKey()).getString(),
                    e.getRegistryName().toString(),
                    false,
                    0)
            );
        });
//        for (int i = 0; i < itemData.size(); i++)
//            itemData.get(i).setOrder(i);

        QuickStore.logger.info("Setting up default syncRenderList");
        this.write(itemData);
        return itemData;
    }
}