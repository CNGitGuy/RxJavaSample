// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.presenter.network;

import com.rengwuxian.rxjavasamples.model.GankBeautyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiGetGank {
    /**
     * get beautiful pictures
     *
     * @param number，一页有多少张图片
     * @param page，获取第几页
     * @return 获取图片的结果
     */
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
