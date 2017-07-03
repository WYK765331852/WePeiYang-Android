package com.twt.wepeiyang.commons.cache;

import com.nytimes.android.external.fs.RecordPersister;
import com.nytimes.android.external.fs.RecordPersisterFactory;
import com.nytimes.android.external.fs.SourcePersisterFactory;
import com.nytimes.android.external.fs.filesystem.FileSystemFactory;
import com.nytimes.android.external.store.base.Persister;
import com.nytimes.android.external.store.base.RecordProvider;
import com.nytimes.android.external.store.base.impl.BarCode;
import com.nytimes.android.external.store.base.impl.Store;
import com.twt.wepeiyang.commons.utils.App;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okio.BufferedSource;


/**
 * Created by tjliqy on 2017/7/1.
 */

public class CacheProvider2 {

    private HashMap<String, Store> storeHashMap;


    private CacheProvider2() {
        storeHashMap = new HashMap<>();
    }


    private static class SingletonHolder {
        private static final CacheProvider2 INSTANCE = new CacheProvider2();
    }

    public static Store getStore(String key) {
        return SingletonHolder.INSTANCE.storeHashMap.get(key);
    }

    public static Persister<BufferedSource, BarCode> persister() throws IOException {
        return SourcePersisterFactory.create(App.getApplicationContext().getCacheDir());
    }

    public static RecordPersister recordPersister() throws IOException {
        return (RecordPersister) RecordPersisterFactory.create(FileSystemFactory.create(App.getApplicationContext().getCacheDir()),0,TimeUnit.MICROSECONDS);
    }

    public static void ClearCache() {
        File[] files = App.getApplicationContext().getCacheDir().listFiles();
        for (File file : files) {
            file.delete();
        }

    }
}
