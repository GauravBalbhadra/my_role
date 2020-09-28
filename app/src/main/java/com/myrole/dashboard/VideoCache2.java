package com.myrole.dashboard;

import android.content.Context;

import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class VideoCache2 {
    private static SimpleCache sDownloadCache2;

    public static SimpleCache getInstance(Context context) {
        if (sDownloadCache2 == null) sDownloadCache2 = new SimpleCache(new File(context.getCacheDir(), "exoCache2"), new NoOpCacheEvictor(), new ExoDatabaseProvider(context));
        return sDownloadCache2;
    }
}
