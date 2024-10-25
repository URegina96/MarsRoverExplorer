package com.example.marsroverexplorer.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.marsroverexplorer.viewModel.MarsRoverViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.marsroverexplorer.R

@Composable
fun MarsRoverScreen(apiKey: String, sol: Int, date: String?) {
    val viewModel: MarsRoverViewModel = viewModel()
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchPhotos(apiKey, "Curiosity", sol, date)
        loading = false
    }

    val photos = viewModel.photos.value
    val error = viewModel.error.collectAsState(initial = null).value
    Log.d("MarsRoverScreen", "Количество фотографий: ${photos.size}")

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Mars Rover Photos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        when {
            error != null -> {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Log.e("MarsRoverScreen", "Ошибка загрузки: $error")
            }
            loading -> {
                Text(
                    text = "Загрузка фотографий...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Log.d("MarsRoverScreen", "Фотографии отсутствуют, идет загрузка")
            }
            else -> {
                LazyColumn {
                    items(photos) { photo ->
                        Log.d("MarsRoverScreen", "URL фотографии: ${photo.img_src}")

                        AsyncImage(
                            model = photo.img_src,
                            contentDescription = photo.earth_date,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(200.dp),
                            onSuccess = {
                                Log.d("MarsRoverScreen", "Фото загружено успешно: ${photo.img_src}")
                            },
                            onError = {
                                Log.e(
                                    "MarsRoverScreen",
                                    "Ошибка загрузки изображения: ${it.result.throwable}"
                                )
                            }
                        )
                        // Статическое изображение для теста
                        Image(
                            painter = painterResource(R.drawable.static_image),
                            contentDescription = "Static Image",
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Log.d("MarsRoverScreen", "Фото успешно загружено: ${photo.img_src}")
                    }
                }
            }
        }
    }
}
