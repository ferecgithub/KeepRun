package com.ferechamitbeyli.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.net.URL

object HelperFunctions {
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun getByteArrayFromUrl(url: String): ByteArray =
        fromBitmap(BitmapFactory.decodeStream(URL(url).openConnection().getInputStream()))

    fun getBitmapFromUrl(url: String): Bitmap =
        BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
}