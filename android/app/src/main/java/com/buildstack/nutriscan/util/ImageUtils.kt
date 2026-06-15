package com.buildstack.nutriscan.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    
    fun uriToFile(context: Context, uri: Uri, fileName: String = "temp_image.jpg"): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return compressImage(file)
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        
        // Resize if too large
        val maxDimension = 1920
        var width = bitmap.width
        var height = bitmap.height
        
        if (width > maxDimension || height > maxDimension) {
            val ratio = width.toFloat() / height.toFloat()
            if (ratio > 1) {
                width = maxDimension
                height = (maxDimension / ratio).toInt()
            } else {
                height = maxDimension
                width = (maxDimension * ratio).toInt()
            }
        }
        
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        
        // Compress
        val bos = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        
        val compressedFile = File(file.absolutePath)
        val fos = FileOutputStream(compressedFile)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
        
        return compressedFile
    }
}
