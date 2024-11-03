package com.example.marsroverexplorer.saveImg

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun saveImageToGallery(context: Context, bitmap: Bitmap, imageName: String) {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(storageDir, "$imageName.jpg")

    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            Toast.makeText(context, "Изображение сохранено в галерею!", Toast.LENGTH_SHORT).show()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Ошибка при сохранении изображения!", Toast.LENGTH_SHORT).show()
    }
}