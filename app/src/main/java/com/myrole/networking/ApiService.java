package com.myrole.networking;

import com.myrole.model.ModelDataAddShareCount;
import com.myrole.model.ShareDownloadCountModel;
import com.myrole.model.SongServiceResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.myrole.utils.Config.USER_SEARCH;

public interface ApiService {

    @POST("get_all_tracks")
    Call<SongServiceResponse> getSongs();

    @FormUrlEncoded
    @POST("add_share_download_video")
    Call<ShareDownloadCountModel> getShareCount(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("add_share_download_video")
    Call<ShareDownloadCountModel> getDownloadCount(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("add_share_post")
    Call<ModelDataAddShareCount> addShareCount(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("add_download_post")
    Call<ModelDataAddShareCount> addDownloadCount(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user_search")
    Call<ResponseBody> getSearchResult(@FieldMap Map<String, String> map);
}
