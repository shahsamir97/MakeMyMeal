package com.mdshahsamir.makemymeal.ui.createrecipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CreateRecipeScreen() {
    Scaffold {contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Click here")
            }
        }
    }
}