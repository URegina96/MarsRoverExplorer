package com.example.marsroverexplorer.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarsRoverApi {
    @GET("mars-photos/api/v1/rovers/{rover}/photos")
    fun getPhotos(
        @Path("rover") rover: String,
        @Query("sol") sol: Int,
        @Query("earth_date") date: String? = null,
        @Query("api_key") apiKey: String
    ): Call<RoverResponse>
}
