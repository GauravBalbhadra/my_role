package com.myrole.networking;

import com.myrole.utils.Config;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadServiceClient {


    public static final String BASE_URL = Config.BASEURL;
    public static Retrofit retrofit;

    /*
     * This public static method will return retrofit client
     * anywhere in the app
     */
    public static Retrofit getRetrofitClient() {

        //If condition to ensure we don't create multiple retrofit instances in a single application

        if (retrofit == null) {
            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }

        return retrofit;

    }

}
