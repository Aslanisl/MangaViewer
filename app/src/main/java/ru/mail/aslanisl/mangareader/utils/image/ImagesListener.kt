package ru.mail.aslanisl.mangareader.utils.image

import android.graphics.Bitmap

interface ImagesListener {
    fun currentImageProgressChanged(progress: Float)
    fun imageReady(bitmap: Bitmap)
    fun onFailure(e: Exception)
    fun onCancel()
}