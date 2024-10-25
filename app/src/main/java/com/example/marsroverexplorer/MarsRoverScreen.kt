package com.example.marsroverexplorer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.marsroverexplorer.viewModel.MarsRoverViewModel
import android.util.Log // импортируем логирование

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
    Log.d("MarsRoverScreen", "Количество фотографий: ${photos.size}") // Логируем количество фотографий

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
                Log.e("MarsRoverScreen", "Ошибка загрузки: $error") // Логируем ошибку
            }
            loading -> {
                Text(
                    text = "Загрузка фотографий...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Log.d("MarsRoverScreen", "Фотографии отсутствуют, идет загрузка") // Логируем состояние загрузки
            }
            else -> {
                for (photo in photos) {
                    Log.d("MarsRoverScreen", "URL фотографии: ${photo.img_src}") // Логируем URL каждой фотографии
                    Image(
                        painter = rememberAsyncImagePainter(model = photo.img_src),
                        contentDescription = photo.earth_date,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    Log.d("MarsRoverScreen", "Фото успешно загружено: ${photo.img_src}") // Логируем успешную загрузку
                }
            }
        }
    }
}
