package com.example.marsroverexplorer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.marsroverexplorer.viewModel.MarsRoverViewModel
@Composable
fun MarsRoverScreen(apiKey: String, sol: Int, date: String?) {
    val viewModel: MarsRoverViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.fetchPhotos(apiKey, "Curiosity", sol, date) // Передаем имя марсохода
    }

    val photos = viewModel.photos
    val error = viewModel.error.collectAsState(initial = null).value

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
            }
            photos.isEmpty() -> {
                Text(
                    text = "Загрузка фотографий...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                for (photo in photos) {
                    Image(
                        painter = rememberImagePainter(photo.img_src),
                        contentDescription = photo.earth_date,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}