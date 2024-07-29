package com.mdshahsamir.makemymeal.ui.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MySuggestionChip(onClick: (String) -> Unit, labelText: String) {
    SuggestionChip(
        onClick = { onClick(labelText) },
        label = { Text(text = labelText) },
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Preview
@Composable
fun MySuggestionChipPreview() {
    MySuggestionChip(onClick = {}, labelText = "Suggestion Chip")
}