package com.mdshahsamir.makemymeal.ui.createrecipe

import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.makemymeal.common.ImageCaptureUIState
import com.mdshahsamir.makemymeal.domain.CreateRecipeUseCase
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
    private val createRecipeUseCase: CreateRecipeUseCase
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

        viewModelScope.launch {
            createRecipeUseCase(image)
                .onSuccess { content ->
                    _createRecipeUIState.update { CreateRecipeUIState.ContentGenerated(content) }
                }
                .onFailure { error ->
                    _createRecipeUIState.update {
                        CreateRecipeUIState.Error(error.message ?: "An error occurred")
                    }
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