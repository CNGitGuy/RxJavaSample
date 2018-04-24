// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.presenter.utils;

import com.rengwuxian.rxjavasamples.model.GankBeauty;
import com.rengwuxian.rxjavasamples.model.GankBeautyResult;
import com.rengwuxian.rxjavasamples.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * 将请求到的结果集转换成本地使用的List<Item>
 */
public class GankBeautyResultToItemsMapper implements Function<GankBeautyResult, List<Item>> {
    private static GankBeautyResultToItemsMapper INSTANCE = new GankBeautyResultToItemsMapper();

    private GankBeautyResultToItemsMapper() {
    }

    public static GankBeautyResultToItemsMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Item> apply(GankBeautyResult gankBeautyResult) {
        List<GankBeauty> gankBeauties = gankBeautyResult.beauties;//get Beauty list
        List<Item> items = new ArrayList<>(gankBeauties.size());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for (GankBeauty gankBeauty : gankBeauties) {//将每个GankBeauty转换成Item对象
            Item item = new Item();
            try {
                Date date = inputFormat.parse(gankBeauty.createdAt);
                item.description = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                item.description = "unknown date";
            }
            item.imageUrl = gankBeauty.url;
            items.add(item);
        }
        return items;
    }
}
