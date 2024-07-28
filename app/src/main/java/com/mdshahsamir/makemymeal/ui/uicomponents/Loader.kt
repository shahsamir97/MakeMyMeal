package com.mdshahsamir.makemymeal.ui.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mdshahsamir.makemymeal.R
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealAppTheme

@Composable
fun MyLoader() {
    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_anim))

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        Card(modifier = Modifier.padding(16.dp).wrapContentSize().aspectRatio(1f)) {
            LottieAnimation(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Preview
@Composable
fun MyLoaderPreview() {
    MakeMyMealAppTheme {
        MyLoader()
    }
}