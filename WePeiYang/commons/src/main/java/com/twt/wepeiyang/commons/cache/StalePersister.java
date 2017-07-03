package com.twt.wepeiyang.commons.cache;

import com.nytimes.android.external.fs.SourcePersister;
import com.nytimes.android.external.fs.filesystem.FileSystem;
import com.nytimes.android.external.store.base.RecordProvider;
import com.nytimes.android.external.store.base.RecordState;
import com.nytimes.android.external.store.base.impl.BarCode;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Created by tjliqy on 2017/7/2.
 */

public class StalePersister extends SourcePersister implements RecordProvider<BarCode> {


    private boolean isStale = false;

    @Inject
    private StalePersister(FileSystem fileSystem) {
        super(fileSystem);
    }

    public static StalePersister create(FileSystem fileSystem){
        return new StalePersister(fileSystem);
    }

    @Nonnull
    @Override
    public RecordState getRecordState(@Nonnull BarCode barCode) {
//        return sourceFileReader;
        return null;
    }
}
