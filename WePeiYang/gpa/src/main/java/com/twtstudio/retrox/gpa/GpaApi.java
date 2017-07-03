package com.twtstudio.retrox.gpa;



import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by retrox on 2017/1/26.
 */

public interface GpaApi {
    @GET("gpa")
    Observable<MyGpaBean> getGpa();

    @GET("gpa")
    Observable<ResponseBody> getGpa4Rersister();
}
