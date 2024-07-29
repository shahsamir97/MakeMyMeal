package com.mdshahsamir.makemymeal.ui.makemeal


import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdshahsamir.makemymeal.R
import com.mdshahsamir.makemymeal.common.ImageCaptureUIState
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealAppTheme
import com.mdshahsamir.makemymeal.ui.uicomponents.CameraView
import com.mdshahsamir.makemymeal.ui.uicomponents.ImagePreview
import com.mdshahsamir.makemymeal.ui.uicomponents.MyLoader
import com.mdshahsamir.makemymeal.ui.uicomponents.TypeWriterText
import com.mdshahsamir.makemymeal.unil.fixOrientation

@Composable
fun MakeMealScreen(
    makeMealViewModel: MakeMealViewModel = viewModel()
) {
    val makeMealUIState by makeMealViewModel.makeMealUIState.collectAsStateWithLifecycle()
    val imageCaptureUIState by makeMealViewModel.imageCaptureUIState.collectAsStateWithLifecycle()

    MakeMealContent(
        makeMealUIState = makeMealUIState,
        imageCaptureUIState = imageCaptureUIState,
        onClickCapture = {
            makeMealViewModel.captureImage(it)
        },
        onClosePreview = {
            makeMealViewModel.activateCameraPreviewMode()
        }
    )

    when(makeMealUIState) {
        MakeMealUIState.Loading -> MyLoader()
        else -> {}
    }
}

@Composable
fun MakeMealContent(
    makeMealUIState: MakeMealUIState,
    imageCaptureUIState: ImageCaptureUIState,
    onClickCapture: (imageCapture: ImageCapture) -> Unit,
    onClosePreview: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var showImagePreview by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(imageCaptureUIState) {
        showImagePreview = imageCaptureUIState is ImageCaptureUIState.ImagePreview
    }

    Scaffold {contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            when (imageCaptureUIState) {
                is ImageCaptureUIState.CameraPreview -> {
                    CameraView(onClickCapture = onClickCapture)
                }

                is ImageCaptureUIState.ImagePreview -> {
                    ImagePreview(imageCaptureUIState.imageBitmap, onClosePreview)
                }
            }

            Column(modifier = Modifier.padding(8.dp)){
                if (imageCaptureUIState is ImageCaptureUIState.CameraPreview) {
                    Text(
                        text = stringResource(R.string.make_meal_helper_text),
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                }

                when(makeMealUIState) {
                    is MakeMealUIState.ContentGenerated -> {
                        Spacer(modifier = Modifier.height(22.dp))
                        TypeWriterText(
                            modifier = Modifier.fillMaxWidth(),
                            text = makeMealUIState.content
                        )
                    }

                    is MakeMealUIState.Error -> {
                        Spacer(modifier = Modifier.height(22.dp))
                        TypeWriterText(
                            modifier = Modifier.fillMaxWidth(),
                            text = makeMealUIState.errorMessage,
                            textColor = Color.Red
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Preview
@Composable
fun CreateRecipeContentPreview() {
    MakeMyMealAppTheme {
        MakeMealContent(
            makeMealUIState = MakeMealUIState.Idle,
            onClickCapture = {},
            onClosePreview = {},
            imageCaptureUIState = ImageCaptureUIState.CameraPreview,
        )
    }
}