package com.example.marsroverexplorer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsroverexplorer.Photo
import com.example.marsroverexplorer.api.RoverResponse
import com.example.marsroverexplorer.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverViewModel : ViewModel() {
    var photos: List<Photo> = listOf()

    fun fetchPhotos(apiKey: String) {
        val call = RetrofitInstance.api.getPhotos(sol = 1000, apiKey = apiKey)
        call.enqueue(object : Callback<RoverResponse> {
            override fun onResponse(call: Call<RoverResponse>, response: Response<RoverResponse>) {
                if (response.isSuccessful) {
                    photos = response.body()?.photos ?: emptyList()
                }
            }

            override fun onFailure(call: Call<RoverResponse>, t: Throwable) {
                // Handle error
            }
        })
    }
}
