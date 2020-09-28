package com.myrole.dashboard.interfaces;

public interface InvokedFeedsFunctions {
    void showCommentSheet(String id, String pos);

    void downloadVideo(String url, int id, int position);

    void sharedVideo(String url, int id, int position);

    void dismissCommentSheet();

    void deleteVideo(String id, String pos);
}
