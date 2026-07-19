package com.mdshahsamir.makemymeal.ui.uicomponents

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mdshahsamir.makemymeal.R
import com.mdshahsamir.makemymeal.unil.convertUriToBitmap
import com.mdshahsamir.makemymeal.unil.getCameraProvider

@Composable
fun CameraView(
    onClickCapture: (imageCapture: ImageCapture) -> Unit,
    onPhotoPicked: (image: Bitmap) -> Unit,
) {
    val contentResolver = LocalContext.current.contentResolver
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()

    val previewView = remember { PreviewView(context) }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember { ImageCapture.Builder().build() }
    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onPhotoPicked(
                    convertUriToBitmap(
                        uri = it,
                        contentResolver = contentResolver
                    )
                )
            }
        }
    )

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(400.dp)
            .clip(RoundedCornerShape(32.dp)),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        Row {
            Button(
                modifier = Modifier.padding(12.dp),
                onClick = {
                    singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_photo_library),
                    contentDescription = stringResource(R.string.capture_image),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Button(
                modifier = Modifier.padding(12.dp),
                onClick = {
                    onClickCapture(imageCapture)
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = stringResource(R.string.capture_image),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}