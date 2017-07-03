package com.twtstudio.retrox.gpa;

import com.google.gson.Gson;
import com.nytimes.android.external.store.base.impl.BarCode;
import com.nytimes.android.external.store.base.impl.Store;
import com.nytimes.android.external.store.base.impl.StoreBuilder;
import com.twt.wepeiyang.commons.cache.CacheProvider2;
import com.twt.wepeiyang.commons.network.RetrofitProvider;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by tjliqy on 2017/7/2.
 */

public class GpaCacheProvider2 {

    private Store<GpaBean, BarCode> store;

    private GpaCacheProvider2() {
        try {
            store = StoreBuilder.<BarCode, BufferedSource, GpaBean>parsedWithKey()
                    .fetcher(barcode -> RetrofitProvider.getRetrofit().create(GpaApi.class).getGpa4Rersister().map(ResponseBody::source))
                    .persister(CacheProvider2.persister())
                    .parser(bufferedSource -> {
                        try (InputStreamReader reader = new InputStreamReader(bufferedSource.inputStream(), Charset.forName("UTF-8"))) {
                            MyGpaBean myGpaBean = new Gson().fromJson(reader, (Type)MyGpaBean.class);
                            if (myGpaBean.getError_code() != -1){
                                throw new RuntimeException();
                            }else {
                                return myGpaBean.getData();
//                                throw new RuntimeException();

                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .networkBeforeStale()
                    .open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SingletonHolder{
        private static final GpaCacheProvider2 INSTANCE = new GpaCacheProvider2();
    }

    public static Store<GpaBean,BarCode> getStore(){
        return SingletonHolder.INSTANCE.store;
    }
}
