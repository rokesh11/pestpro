package com.example.pestpro.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofitClient=null;

    public static Retrofit getClient()
    {
        if(retrofitClient==null)
        {
            retrofitClient=new Retrofit.Builder()
                    .baseUrl("http://codewizard.pythonanywhere.com") // is localhost in the phone
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofitClient;
    }
}
