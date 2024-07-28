package com.mdshahsamir.makemymeal.ui.makemeal

import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content
import com.mdshahsamir.makemymeal.common.ImageCaptureUIState
import com.mdshahsamir.makemymeal.gemini.generativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class MakeMealViewModel: ViewModel() {

    private val _makeMealUIState = MutableStateFlow<MakeMealUIState>(MakeMealUIState.Idle)
    val makeMealUIState: StateFlow<MakeMealUIState> = _makeMealUIState

    private val _imageCaptureUIState = MutableStateFlow<ImageCaptureUIState>(ImageCaptureUIState.CameraPreview)
    val imageCaptureUIState: StateFlow<ImageCaptureUIState> = _imageCaptureUIState

    private val imageCaptureObserver = object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            super.onCaptureSuccess(image)

            image.toBitmap().let { imageBitmap ->
                _imageCaptureUIState.update { ImageCaptureUIState.ImagePreview(imageBitmap) }
                generateContent(imageBitmap)
            }
        }

        override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
        }
    }

    private fun generateContent(image: Bitmap) {
        _makeMealUIState.update { MakeMealUIState.Loading }
        viewModelScope.launch(Dispatchers.IO) {
            val contet = content {
                image(image)
                text("What is this thing in this image?")
            }

            try {
                val response = generativeModel.generateContent(contet)
                _makeMealUIState.update { MakeMealUIState.ContentGenerated(response.text.toString()) }
            } catch (e: Exception) {
                e.printStackTrace()
                _makeMealUIState.update { MakeMealUIState.Error(e.message.toString()) }
            }
        }
    }

    fun captureImage(
        imageCapture: ImageCapture,
    ) {
        imageCapture.takePicture(
            Executors.newSingleThreadExecutor(),
            imageCaptureObserver
        )
    }

    fun activateCameraPreviewMode() {
        _imageCaptureUIState.update { ImageCaptureUIState.CameraPreview }
        _makeMealUIState.update { MakeMealUIState.Idle }
    }
}

sealed interface MakeMealUIState {
    data object Idle: MakeMealUIState
    data object Loading: MakeMealUIState
    data class ContentGenerated(val content: String): MakeMealUIState
    data class Error(val errorMessage:String): MakeMealUIState
}
