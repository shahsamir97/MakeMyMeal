package com.mdshahsamir.makemymeal.ui.makemeal

import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
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
class MakeMealViewModel @Inject constructor(
    private val generativeModelService: GenerativeModelService
): ViewModel() {

    private val _makeMealUIState = MutableStateFlow<MakeMealUIState>(MakeMealUIState.Idle)
    val makeMealUIState: StateFlow<MakeMealUIState> = _makeMealUIState

    private val _imageCaptureUIState = MutableStateFlow<ImageCaptureUIState>(ImageCaptureUIState.CameraPreview)
    val imageCaptureUIState: StateFlow<ImageCaptureUIState> = _imageCaptureUIState

    private lateinit var _imageContent: Bitmap
    private var _mealType: String = ""
    private var _cuisineType: String = ""

    private val imageCaptureObserver = object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            super.onCaptureSuccess(image)

            image.toBitmap().let { imageBitmap ->
                _imageCaptureUIState.update { ImageCaptureUIState.ImagePreview(imageBitmap) }
                _imageContent = imageBitmap
                generateContent(imageBitmap)
            }
        }
    }

    private fun generateContent(image: Bitmap) {
        _makeMealUIState.update { MakeMealUIState.Loading }

        val prompt = "I have the ingredients above. Not sure what to cook $_mealType. Show me a list of $_cuisineType foods with the recipes. Do not ask for any information."

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModelService.generateResponse(prompt, image)
                _makeMealUIState.update { MakeMealUIState.ContentGenerated(response) }
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

    fun onPhotoPickedFromGallery(bitmap: Bitmap) {
        generateContent(bitmap)
        _imageContent = bitmap
        _imageCaptureUIState.update { ImageCaptureUIState.ImagePreview(bitmap) }
    }

    fun updateMealType(mealType: String) {
        _mealType = if (mealType.isNotEmpty()) "for $mealType" else ""

        if (_makeMealUIState.value is MakeMealUIState.ContentGenerated) {
            generateContent(_imageContent)
        }
    }

    fun updateCuisineType(cuisineType: String) {
        _cuisineType =if (cuisineType.isNotEmpty()) "$cuisineType cuisine" else ""

        if (_makeMealUIState.value is MakeMealUIState.ContentGenerated) {
            generateContent(_imageContent)
        }
    }
}

sealed interface MakeMealUIState {
    data object Idle: MakeMealUIState
    data object Loading: MakeMealUIState
    data class ContentGenerated(val content: String): MakeMealUIState
    data class Error(val errorMessage:String): MakeMealUIState
}
