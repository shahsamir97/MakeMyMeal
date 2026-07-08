package com.mdshahsamir.makemymeal.ui.makemeal


import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mdshahsamir.makemymeal.R
import com.mdshahsamir.makemymeal.common.ImageCaptureUIState
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealAppTheme
import com.mdshahsamir.makemymeal.ui.uicomponents.CameraView
import com.mdshahsamir.makemymeal.ui.uicomponents.ImagePreview
import com.mdshahsamir.makemymeal.ui.uicomponents.MyLoader
import com.mdshahsamir.makemymeal.ui.uicomponents.TypeWriterText

@Composable
fun MakeMealScreen(
    makeMealViewModel: MakeMealViewModel = hiltViewModel()
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
        },
        onPhotoPicked = { bitmap ->
            makeMealViewModel.onPhotoPickedFromGallery(bitmap)
        },
        onMealTypeSelected = {
            makeMealViewModel.updateMealType(it)
        },
        onCuisineTypeSelected = {
            makeMealViewModel.updateCuisineType(it)
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
    onPhotoPicked: (bitmap: Bitmap) -> Unit,
    onMealTypeSelected: (String) -> Unit,
    onCuisineTypeSelected: (String) -> Unit,
) {
    val mealTypes = stringArrayResource(id = R.array.meal_type)
    val cuisineType = stringArrayResource(id = R.array.cuisine_type)

    val scrollState = rememberScrollState()
    var showImagePreview by rememberSaveable { mutableStateOf(false) }

    var selectedMealType by rememberSaveable { mutableStateOf("") }
    var selectedCuisineType by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(imageCaptureUIState) {
        showImagePreview = imageCaptureUIState is ImageCaptureUIState.ImagePreview
    }

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.animateScrollTo(scrollState.maxValue)
    } 

    Scaffold {contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.select_meal_type),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(mealTypes) {
                    FilterChip(
                        selected = selectedMealType == it,
                        onClick = {
                            selectedMealType = if (selectedMealType == it) "" else it
                            onMealTypeSelected(selectedMealType)
                        },
                        label = { Text(text = it, color = MaterialTheme.colorScheme.onPrimary) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            selectedContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            if (selectedMealType == it) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check),
                                    contentDescription = stringResource(id = R.string.meal_type_selected, it),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Select Cuisine",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(cuisineType) {
                    FilterChip(
                        selected = selectedCuisineType == it,
                        onClick = {
                            selectedCuisineType = if (selectedCuisineType == it) "" else it
                            onCuisineTypeSelected(selectedCuisineType)
                        },
                        label = {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            selectedContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            if (selectedCuisineType == it) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check),
                                    contentDescription = stringResource(id = R.string.meal_type_selected, it),
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.padding(16.dp)) {
                when (imageCaptureUIState) {
                    is ImageCaptureUIState.CameraPreview -> {
                        CameraView(
                            onClickCapture = onClickCapture,
                            onPhotoPicked = { bitmap ->
                                onPhotoPicked(bitmap)
                            }
                        )
                    }

                    is ImageCaptureUIState.ImagePreview -> {
                        ImagePreview(imageCaptureUIState.imageBitmap, onClosePreview)
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)){
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
            imageCaptureUIState = ImageCaptureUIState.CameraPreview,
            onClickCapture = {},
            onClosePreview = {},
            onPhotoPicked = {},
            onMealTypeSelected = {},
            onCuisineTypeSelected = {},
        )
    }
}