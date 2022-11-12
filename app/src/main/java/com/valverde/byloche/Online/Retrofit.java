package com.valverde.byloche.Online;

import com.valverde.byloche.Interfaz.iRestApi;
import com.valverde.byloche.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public Retrofit(String ip) {


        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(ip).addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        iRestApi restApi = retrofit.create(iRestApi.class);
    }


}
