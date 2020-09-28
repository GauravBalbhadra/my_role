package com.myrole.networking;

import com.myrole.model.SongServiceResponse;

import retrofit2.Call;
import retrofit2.http.POST;

public interface SongService {

    @POST("/get_all_tracks")
    Call<SongServiceResponse> getSongs();

}
