package com.myrole.networking;

import com.myrole.utils.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SongServiceClient {

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
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }

        return retrofit;


    }

}
