package com.mdshahsamir.makemymeal.ui.createrecipe

import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.makemymeal.common.ImageCaptureUIState
import com.mdshahsamir.makemymeal.data.ai.GenerativeModelService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject


@HiltViewModel
class CreateRecipeViewModel @Inject constructor(
    private val generativeModelService: GenerativeModelService
): ViewModel() {

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
            val prompt = "Accurately identify the food in the image and provide an appropriate and recipe consistent with your analysis. Do not ask any question"

            try {
                val response = generativeModelService.generateResponse(prompt, image)
                _createRecipeUIState.update { CreateRecipeUIState.ContentGenerated(response) }
            } catch (e: Exception) {
                e.printStackTrace()
                _createRecipeUIState.update { CreateRecipeUIState.Error(e.message.toString()) }
            }
        }
    }

    fun onPhotoPickedFromGallery(bitmap: Bitmap) {
        generateContent(bitmap)
        _imageCaptureUIState.update { ImageCaptureUIState.ImagePreview(bitmap) }
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