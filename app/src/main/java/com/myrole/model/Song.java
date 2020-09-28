package com.myrole.model;

import com.google.gson.annotations.SerializedName;

public class Song
{

    @SerializedName("id")
    private int songId;
    @SerializedName("song_name")
    private String songName;
    @SerializedName("thumbnail")
    private String songThumbnail;
    @SerializedName("song_path")
    private String absoluteSongPath;
    @SerializedName("singer_name")
    private String songSingerName;
    boolean isDownloading = false;
    boolean isPlaying = false;
    boolean showPlayButton = false;

    public boolean isShowPlayButton() {
        return showPlayButton;
    }

    public void setShowPlayButton(boolean showPlayButton) {
        this.showPlayButton = showPlayButton;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public int getSongId()
    {
        return songId;
    }

    public void setSongId(int mId)
    {
        songId = mId;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String mName)
    {
        songName = mName;
    }

    public String getSongThumbnail()
    {
        return songThumbnail;
    }

    public void setSongThumbnail(String mTitle)
    {
        songThumbnail = mTitle;
    }

    public String getAbsoluteSongPath()
    {
        return absoluteSongPath;
    }

    public void setAbsoluteSongPath(String songPath)
    {
        absoluteSongPath = songPath;
    }

    public String getSongSingerName()
    {
        return songSingerName;
    }

    public void setSongSingerName(String singerName)
    {
        songSingerName = singerName;
    }

}
