package com.example.marsroverexplorer.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.marsroverexplorer.viewModel.MarsRoverViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.marsroverexplorer.R
import com.example.marsroverexplorer.ui.theme.Purple40
import com.example.marsroverexplorer.ui.theme.PurpleGrey40
import com.example.marsroverexplorer.ui.theme.PurpleGrey80
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.marsroverexplorer.saveImg.saveImageToGallery
import java.net.URL
import coil.request.ImageRequest
import coil.request.SuccessResult

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MarsRoverScreen(apiKey: String, sol: Int, date: String?) {
    val viewModel: MarsRoverViewModel = viewModel()
    var loading by remember { mutableStateOf(true) }
    var newComment by remember { mutableStateOf("") }
    val likedStates = remember { mutableStateListOf<Boolean>() }
    val commentsList = remember { mutableStateListOf<MutableList<String>>() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.fetchPhotos(apiKey, "Curiosity", sol, date)
        loading = false
    }

    val photos = viewModel.photos.value
    val error = viewModel.error.collectAsState(initial = null).value
    val pagerState = rememberPagerState()

    // Инициализация состояний лайков и комментариев для каждой фотографии
    if (likedStates.size != photos.size) {
        likedStates.clear()
        commentsList.clear()
        for (i in photos.indices) {
            likedStates.add(false)
            commentsList.add(mutableListOf())
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Mars Rover Photos",
            style = MaterialTheme.typography.headlineSmall.copy(color = Purple40),
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
            loading -> {
                Text(
                    text = "Загрузка фотографий...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
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

                        // Кнопка для сохранения изображения
                        val context = LocalContext.current
                        Button(onClick = {
                            scope.launch {
                                try {
                                    // Загрузка изображения с помощью Coil
                                    val request = ImageRequest.Builder(context)
                                        .data(photo.img_src)
                                        .build()

                                    val result = (context.imageLoader.execute(request) as SuccessResult)
                                    val bitmap = result.drawable.toBitmap() // Преобразование drawable в bitmap

                                    // Сохранение изображения в галерею
                                    if (bitmap != null) {
                                        saveImageToGallery(context, bitmap, "MarsRover_${System.currentTimeMillis()}")
                                        Toast.makeText(context, "Фотография успешно сохранена в галерею!", Toast.LENGTH_LONG).show()
                                    } else {
                                        Log.e("MarsRoverScreen", "Не удалось загрузить изображение: bitmap равен null")
                                    }
                                } catch (e: Exception) {
                                    Log.e("MarsRoverScreen", "Ошибка при загрузке изображения: ${e.message}")
                                    Toast.makeText(context, "Ошибка при загрузке изображения: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }) {
                            Text("Сохранить в галерею")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Лайк
                        IconButton(onClick = {
                            likedStates[page] = !likedStates[page]
                        }) {
                            Icon(
                                painter = painterResource(id = if (likedStates[page]) R.drawable.ic_liked else R.drawable.ic_like),
                                contentDescription = "Like",
                                tint = if (likedStates[page]) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Комментарии
                        TextField(
                            value = newComment,
                            onValueChange = { newComment = it },
                            label = { Text("Добавить комментарий") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Purple40,
                                unfocusedIndicatorColor = PurpleGrey40,
                                disabledIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            if (newComment.isNotBlank()) {
                                commentsList[page].add(":User     $newComment") // Добавляем фейковое имя пользователя
                                newComment = ""
                            }
                        }) {
                            Text("Отправить")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Отображение комментариев
                        Column {
                            commentsList[page].reversed().forEach { comment -> // Отображаем новые комментарии сверху
                                Text(
                                    text = comment,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = PurpleGrey40), // Цвет текста комментария
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(
                                            PurpleGrey80,
                                            RoundedCornerShape(4.dp)
                                        ) // Фон комментария
                                        .padding(8.dp)
                                )
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