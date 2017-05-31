// (c)2016 Flipboard Inc, All Rights Reserved.

package com.harvey.rxjava.network.api;


import com.harvey.rxjava.model.ZhuangbiImage;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZhuangbiApi {
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
