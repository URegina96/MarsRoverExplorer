package com.example.marsroverexplorer

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.os.Bundle

class MainActivity : ComponentActivity() {
    private val apiKey: String = "EuZkDqT4bbFTeFeVbNHxRc5KuGMCsNXq1edi4lc4" // Замените на ваш действительный API-ключ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarsRoverScreen(apiKey, sol = 1000, date = "2024-10-01") // Передаем sol и дату
        }
    }
}
