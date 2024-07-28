package com.mdshahsamir.makemymeal.ui.uicomponents

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun TypeWriterText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    var displayText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(text) {
        withContext(Dispatchers.Default) {
            for(i in 0..text.length) {
                displayText = text.substring(0, i)
                delay(10)
            }
        }
    }

    Text(
        modifier = modifier,
        text = displayText,
        style = style,
        textAlign = textAlign,
        color = textColor
    )
}