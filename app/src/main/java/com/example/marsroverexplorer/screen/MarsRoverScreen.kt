package com.example.marsroverexplorer.screen

import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.marsroverexplorer.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MarsRoverScreen(apiKey: String, sol: Int, date: String?) {
    val viewModel: MarsRoverViewModel = viewModel()
    var loading by remember { mutableStateOf(true) }
    var comments by remember { mutableStateOf(mutableListOf<String>()) }
    var newComment by remember { mutableStateOf("") }
    var liked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchPhotos(apiKey, "Curiosity", sol, date)
        loading = false
    }

    val photos = viewModel.photos.value
    val error = viewModel.error.collectAsState(initial = null).value
    val pagerState = rememberPagerState()

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
                HorizontalPager(
                    state = pagerState,
                    count = photos.size,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) { page ->
                    val photo = photos[page]
                    Log.d("MarsRoverScreen", "URL фотографии: ${photo.img_src}")
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = photo.img_src,
                            contentDescription = photo.earth_date,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Лайк
                        IconButton(onClick = {
                            liked = !liked
                        }) {
                            Icon(
                                painter = painterResource(id = if (liked) R.drawable.ic_liked else R.drawable.ic_like),
                                contentDescription = "Like",
                                tint = if (liked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Комментарии
                        TextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            label = { Text("Добавить комментарий") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            if (newComment.isNotBlank()) {
                                comments.add(newComment)
                                newComment = ""
                            }
                        }) {
                            Text("Отправить")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Отображение комментариев
                        Column {
                            comments.forEach { comment ->
                                Text(text = comment, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
        }
    }
}