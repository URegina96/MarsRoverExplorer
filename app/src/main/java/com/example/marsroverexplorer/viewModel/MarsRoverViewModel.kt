package com.example.marsroverexplorer.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.marsroverexplorer.model.Photo
import com.example.marsroverexplorer.model.RoverResponse
import com.example.marsroverexplorer.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverViewModel : ViewModel() {
    var photos = mutableStateOf<List<Photo>>(emptyList())
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchPhotos(apiKey: String, rover: String, sol: Int, date: String? = null) {
        Log.d("MarsRoverViewModel", "Запрос к API: sol=$sol, date=$date, apiKey=$apiKey")
        val call = RetrofitInstance.api.getPhotos(rover, sol, date, apiKey)
        call.enqueue(object : Callback<RoverResponse> {
            override fun onResponse(call: Call<RoverResponse>, response: Response<RoverResponse>) {
                if (response.isSuccessful) {
                    photos.value = response.body()?.photos ?: emptyList()
                    _error.update { null }
                    Log.d("MarsRoverViewModel", "Успешный ответ: количество фотографий = ${photos.value.size}")
                } else {
                    _error.update { "Ошибка: ${response.code()} ${response.message()}" }
                    Log.e("MarsRoverViewModel", "Ошибка ответа: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RoverResponse>, t: Throwable) {
                Log.e("MarsRoverViewModel", "Ошибка: ${t.message}")
                _error.update { "Ошибка: ${t.localizedMessage}" }
            }
        })
    }
}
