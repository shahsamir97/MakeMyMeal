package com.mdshahsamir.makemymeal.ui.uicomponents

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mdshahsamir.makemymeal.R
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.dashboard), color = MaterialTheme.colorScheme.onPrimary) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Preview
@Composable
fun MyTopAppBarPreview() {
    MakeMyMealAppTheme {
        MyTopAppBar()
    }
}