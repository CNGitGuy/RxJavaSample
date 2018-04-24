// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 请求的Beauty结果集数据，一个GankBeauty的list
 */
public class GankBeautyResult {
    public boolean error;
    public @SerializedName("results")
    List<GankBeauty> beauties;
}
