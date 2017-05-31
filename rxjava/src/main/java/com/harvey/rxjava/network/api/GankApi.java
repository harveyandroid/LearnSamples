// (c)2016 Flipboard Inc, All Rights Reserved.

package com.harvey.rxjava.network.api;


import com.harvey.rxjava.model.GankBeautyResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
