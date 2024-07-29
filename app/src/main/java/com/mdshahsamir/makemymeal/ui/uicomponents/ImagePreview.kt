package com.mdshahsamir.makemymeal.ui.uicomponents

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.mdshahsamir.makemymeal.unil.fixOrientation

@Composable
fun ImagePreview(
    image: Bitmap,
    onClosePreview: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(400.dp)
            .clip(RoundedCornerShape(32.dp)),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            bitmap = image.fixOrientation().asImageBitmap(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Button(
            modifier = Modifier.padding(18.dp),
            onClick = onClosePreview,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Try Another")
        }
    }
}