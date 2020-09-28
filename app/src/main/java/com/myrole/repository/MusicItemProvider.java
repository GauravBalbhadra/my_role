package com.myrole.repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.util.Log;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.myrole.customData.MyDemo;
import com.myrole.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicItemProvider implements LoaderManager.LoaderCallbacks<Cursor>
{
    private Context context = MyDemo.getAppContext();

    public static final String TAG = "MusicItemProvider";
    public static final boolean DEBUG = true;

    private static final Uri URI = Audio.Media.EXTERNAL_CONTENT_URI;

    private static final String[] PROJECTION = {
        Audio.Media._ID,
        Audio.Media.TITLE, 
        Audio.Media.DATA
    };

    private static final String[] FROM_COLUMNS = {
        Audio.Media.TITLE
    };

    public static List<Song> songList;
    private static Song songModel;
    private Uri uri;
    private Cursor cursor;

    static {
        songList = new ArrayList<>();

    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(context, URI, PROJECTION, null, null, Audio.Media.TITLE);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if(loader != null && data != null) {
            
            while(data.moveToNext()) {

                songModel = new Song();

                int id = data.getInt(0);
                String title = data.getString(1);
                String path = data.getString(2);

                String name = path.substring(path.lastIndexOf("/") + 1);

                songModel.setSongId(id);
                songModel.setSongName(name);
                songModel.setAbsoluteSongPath(path);

                if(DEBUG) Log.d(TAG, "Name :" + title);
                if(DEBUG) Log.d(TAG, "Path :" + path);

                songList.add(songModel);
            }
        }

    }

    public void onLoaderReset(Loader<Cursor> loader)
    {

    }

}
