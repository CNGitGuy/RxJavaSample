// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.presenter.network;

import com.rengwuxian.rxjavasamples.model.ZhuangbiImage;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiGetZhuangbi {
    /**
     * @param query
     * @return
     */
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
