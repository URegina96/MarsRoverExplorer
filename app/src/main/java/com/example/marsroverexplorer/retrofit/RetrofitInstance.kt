package com.example.marsroverexplorer.retrofit

import com.example.marsroverexplorer.api.MarsRoverApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.nasa.gov/"

    val api: MarsRoverApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarsRoverApi::class.java)
    }
}