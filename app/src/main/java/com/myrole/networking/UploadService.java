package com.myrole.networking;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadService {

    @Multipart
    @POST("/upload_post")
    Call<UploadServiceResponse> uploadVideo(@PartMap() Map<String, RequestBody> partMap,
                                            @Part MultipartBody.Part video);

}
