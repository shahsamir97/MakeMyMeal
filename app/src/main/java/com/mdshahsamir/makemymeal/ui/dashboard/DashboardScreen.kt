package com.mdshahsamir.makemymeal.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mdshahsamir.makemymeal.R
import com.mdshahsamir.makemymeal.navigation.NavigationScreen
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealAppTheme
import com.mdshahsamir.makemymeal.ui.uicomponents.MyTopAppBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun DashboardScreen(navController: NavController) {

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {
        Text("Camera permission Granted")
    } else {
        PermissionDialog(onClickRequestPermission = {
            cameraPermissionState.launchPermissionRequest()
        })
    }

    DashboardContent(
        onClickCreateRecipe = {
            navController.navigate(NavigationScreen.CreateRecipe.route)
        },
        onClickMakeMeal = {
            navController.navigate(NavigationScreen.MakeMeal.route)
        },
        onClickLoseOrGainWeight = {
            navController.navigate(NavigationScreen.LoseORGainWeight.route)
        }
    )
}

@Composable
internal fun DashboardContent(
    onClickCreateRecipe: () -> Unit,
    onClickMakeMeal: () -> Unit,
    onClickLoseOrGainWeight: () -> Unit,
) {
    val createRecipeLottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.create_recip))
    val makeMealLottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.making_meal))
    val workoutLottieComp by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.workout))


    Scaffold(
        topBar = { MyTopAppBar() }
    ) { contentPadding ->
        Column( modifier = Modifier
            .padding(contentPadding)
            .padding(16.dp)
        ) {
            Row {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    onClick = onClickCreateRecipe,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.create_recipe),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LottieAnimation(
                            composition = createRecipeLottieComp,
                            iterations = LottieConstants.IterateForever,
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    onClick = onClickMakeMeal,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.make_meal),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LottieAnimation(
                            composition = makeMealLottieComp,
                            iterations = LottieConstants.IterateForever,
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                onClick = onClickLoseOrGainWeight,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Lose or Gain Weight",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LottieAnimation(
                        composition = workoutLottieComp,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Fit,
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionDialog(onClickRequestPermission: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "This app needs camera permission to take picture of your ingredients or food to create a recipe.",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(onClick = onClickRequestPermission) {
                        Text(text = "Request Permission")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DashboardContentPreview() {
    MakeMyMealAppTheme {
        DashboardContent(
            onClickCreateRecipe = {},
            onClickMakeMeal = {},
            onClickLoseOrGainWeight = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    MakeMyMealAppTheme {
        PermissionDialog(onClickRequestPermission = {})
    }
}
