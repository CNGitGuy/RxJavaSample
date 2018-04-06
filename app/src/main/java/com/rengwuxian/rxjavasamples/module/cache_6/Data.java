// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.module.cache_6;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.rengwuxian.rxjavasamples.App;
import com.rengwuxian.rxjavasamples.R;
import com.rengwuxian.rxjavasamples.model.Item;
import com.rengwuxian.rxjavasamples.network.Network;
import com.rengwuxian.rxjavasamples.util.GankBeautyResultToItemsMapper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

class Data {
    private static Data instance;
    private static final int DATA_SOURCE_MEMORY = 1;
    private static final int DATA_SOURCE_DISK = 2;
    private static final int DATA_SOURCE_NETWORK = 3;

    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK})
    @interface DataSource {
    }

    /**
     *
     */
    BehaviorSubject<List<Item>> cache;

    private int dataSource;

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    /**
     * 设置资源的来源，内存、本地、网络
     *
     * @param dataSource
     */
    private void setDataSource(@DataSource int dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceText() {
        int dataSourceTextRes;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataSourceTextRes = R.string.data_source_memory;
                break;
            case DATA_SOURCE_DISK:
                dataSourceTextRes = R.string.data_source_disk;
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceTextRes = R.string.data_source_network;
                break;
            default:
                dataSourceTextRes = R.string.data_source_network;
        }
        return App.getInstance().getString(dataSourceTextRes);
    }

    /**
     * 从网络请求数据：100张图片，1页的数据
     */
    public void loadFromNetwork() {
        Network.getApiGetGank()
                .getBeauties(100, 1)//从网络请求100张，1页的数据
                .subscribeOn(Schedulers.io())//订阅在io线程
                .map(GankBeautyResultToItemsMapper.getInstance())//得到的GankBeautyResult在这里转换成List<Item>
                .doOnNext(new Consumer<List<Item>>() {
                    @Override
                    public void accept(List<Item> items) {//获取数据成功，保存数据（description&image URL）到本地
                         DataCache.getInstance().writeItems(items);
                    }
                })
                .subscribe(new Consumer<List<Item>>() {
                    @Override
                    public void accept(List<Item> items) {
                        cache.onNext(items);//订阅获取到的数据
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        cache.onError(throwable);
                    }
                });
    }

    /**
     * 订阅数据（请求的数据是图片的描述和URL，图片数据用Glide加载显示），
     * 优先级高到低，内存、本地、网络
     *
     * @param onNext，成功获取数据的消费者
     * @param onError，获取数据失败的消费者
     * @return
     */
    public Disposable subscribeData(@NonNull Consumer<List<Item>> onNext,
                                    @NonNull Consumer<Throwable> onError) {
        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable.create(new ObservableOnSubscribe<List<Item>>() {
                @Override
                public void subscribe(ObservableEmitter<List<Item>> e) throws Exception {
                    List<Item> items = DataCache.getInstance().readItems();
                    if (items == null) {//本地无数据，网络下载数据
                        setDataSource(DATA_SOURCE_NETWORK);
                        loadFromNetwork();
                    } else {
                        setDataSource(DATA_SOURCE_DISK);//本地的数据
                        e.onNext(items);
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe(cache);
        } else {
            setDataSource(DATA_SOURCE_MEMORY);
        }
        return cache.doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                cache = null;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    public void clearMemoryCache() {
        cache = null;
    }

    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
        DataCache.getInstance().delete();
    }
}
