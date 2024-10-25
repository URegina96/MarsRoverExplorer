package com.example.marsroverexplorer.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsRoverApi {
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getPhotos(
        @Query("sol") sol: Int,
        @Query("api_key") apiKey: String
    ): Call<RoverResponse>
}
