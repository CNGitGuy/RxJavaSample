// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.module.cache_6;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.rxjavasamples.App;
import com.rengwuxian.rxjavasamples.model.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

class DataCache {
    private static String DATA_FILE_NAME = "data.db";

    private static DataCache INSTANCE;

    File dataFile = new File(App.getInstance().getFilesDir(), DATA_FILE_NAME);
    Gson gson = new Gson();

    private DataCache() {
    }

    public static DataCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataCache();
        }
        return INSTANCE;
    }

    /**
     * 从本地文件获取图片资源的description和image URL
     * @return
     */
    public List<Item> readItems() {
        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = new FileReader(dataFile);
            return gson.fromJson(reader, new TypeToken<List<Item>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将所有的的items都存储到本地
     * @param items
     */
    public void writeItems(List<Item> items) {
        String json = gson.toJson(items);
        try {
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        dataFile.delete();
    }
}
