package com.mdshahsamir.makemymeal.unil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

fun Bitmap.fixOrientation(): Bitmap {
    return if (this.width > this.height) {
        val matrix = Matrix()
        matrix.postRotate(90F)
        Bitmap.createBitmap(
            this,
            0,
            0,
            this.width,
            this.height,
            matrix,
            true
        )
    } else this
}