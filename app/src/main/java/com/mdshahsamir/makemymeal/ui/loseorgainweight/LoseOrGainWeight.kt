package com.mdshahsamir.makemymeal.ui.loseorgainweight

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mdshahsamir.makemymeal.R
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealAppTheme
import com.mdshahsamir.makemymeal.ui.uicomponents.MyLoader
import com.mdshahsamir.makemymeal.ui.uicomponents.TypeWriterText

@Composable
fun LoseOrGainWeightScreen(loseOrGainWeightViewModel: LoseOrGainWeightViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val weightUIState by loseOrGainWeightViewModel.weightUIState.collectAsStateWithLifecycle()

    LoseOrGainWeightContent(onClickCreateRecipe = { gender, age, weight, loseOrGainWeight ->
        if (loseOrGainWeightViewModel.validateInput(gender, age, weight, loseOrGainWeight)) {
            loseOrGainWeightViewModel.generateContent(gender, age, weight, loseOrGainWeight)
        } else {
            Toast.makeText(context, "All fields required! Please Select all required data.", Toast.LENGTH_SHORT).show()
        }
    },
        weightUIState)

    when(weightUIState) {
        WeightUIState.Loading -> { MyLoader() }
        else -> {}
    }
}

@Composable
fun LoseOrGainWeightContent(
    onClickCreateRecipe: (gender: String, age: String, weight: String, loseOrGainWeight: String) -> Unit,
    weightUIState: WeightUIState,
) {
    val mealPlans = stringArrayResource(id = R.array.type_of_meal_plan)
    val genderTypes = stringArrayResource(id = R.array.gender_type)

    var ageSliderPosition by remember { mutableFloatStateOf(22f) }
    var weightSliderPosition by remember { mutableFloatStateOf(16f) }
    var selectedPlanType by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.select_gender),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                genderTypes.forEach {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedGender == it) {
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        ),
                        onClick = { selectedGender = it }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(100.dp),
                                painter = painterResource(
                                    id = if (it == stringResource(id = R.string.male)) {
                                        R.drawable.ic_male
                                    } else {
                                        R.drawable.ic_female
                                    }
                                ),
                                contentDescription = it,
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                            Text(text = it)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Your Age: ${ageSliderPosition.toInt()}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Slider(
                value = ageSliderPosition,
                steps = 120,
                valueRange = 0f..120f,
                onValueChange = {
                    ageSliderPosition = it
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Your Weight: $weightSliderPosition",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.width(6.dp))
            Slider(
                value = weightSliderPosition,
                steps = 200,
                valueRange = 16f..200f,
                onValueChange = {
                    weightSliderPosition = it
                }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                item { Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.i_want_to),
                    style = MaterialTheme.typography.titleMedium
                ) }

                items(mealPlans) {
                    FilterChip(
                        selected = selectedPlanType == it,
                        onClick = {
                            selectedPlanType = if (selectedPlanType == it) "" else it
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
                            if (selectedPlanType == it) {
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

            Spacer(modifier = Modifier.height(36.dp))
            Button(onClick = {
                onClickCreateRecipe(selectedGender, ageSliderPosition.toString(), weightSliderPosition.toString(), selectedPlanType)
            }) {
                Text(text = "Create Recipes")
            }

            when(weightUIState) {
                is WeightUIState.Success -> {
                    Spacer(modifier = Modifier.height(22.dp))
                    TypeWriterText(
                        modifier = Modifier.fillMaxWidth(),
                        text = weightUIState.content
                    )
                }

                is WeightUIState.Error -> {
                    Spacer(modifier = Modifier.height(22.dp))
                    TypeWriterText(
                        modifier = Modifier.fillMaxWidth(),
                        text = weightUIState.errorMessage,
                        textColor = Color.Red
                    )
                }

                else -> {}
            }
        }
    }
}

@Preview
@Composable
fun LoseOrGainWeightContentPreview() {
    MakeMyMealAppTheme {
        LoseOrGainWeightContent(
            onClickCreateRecipe = { _, _, _, _ -> },
            weightUIState = WeightUIState.Success("Content is here")
        )
    }
}
