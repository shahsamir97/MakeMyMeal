package com.mdshahsamir.makemymeal.ui.createrecipe

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


class CreateRecipeViewModel: ViewModel() {

    private val _createRecipeUIState = MutableStateFlow<CreateRecipeUIState>(CreateRecipeUIState.Idle)
    val createRecipeUIState: StateFlow<CreateRecipeUIState> = _createRecipeUIState

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
        _createRecipeUIState.update { CreateRecipeUIState.Loading }
        viewModelScope.launch(Dispatchers.IO) {
            val contet = content {
                image(image)
                text("What is this thing in this image?")
            }

            try {
                val response = generativeModel.generateContent(contet)
                _createRecipeUIState.update { CreateRecipeUIState.ContentGenerated(response.text.toString()) }
            } catch (e: Exception) {
                e.printStackTrace()
                _createRecipeUIState.update { CreateRecipeUIState.Error(e.message.toString()) }
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
        _createRecipeUIState.update { CreateRecipeUIState.Idle }
    }
}

sealed interface CreateRecipeUIState {
    data object Idle: CreateRecipeUIState
    data object Loading: CreateRecipeUIState
    data class ContentGenerated(val content: String): CreateRecipeUIState
    data class Error(val errorMessage:String): CreateRecipeUIState
}