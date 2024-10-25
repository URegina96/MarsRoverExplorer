package com.example.marsroverexplorer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.marsroverexplorer.viewModel.MarsRoverViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarsRoverScreen(apiKey: String) {
    val viewModel: MarsRoverViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.fetchPhotos(apiKey)
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Mars Rover Photos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        if (viewModel.photos.isEmpty()) {
            Text("Loading photos...", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
        } else {
            for (photo in viewModel.photos) {
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
