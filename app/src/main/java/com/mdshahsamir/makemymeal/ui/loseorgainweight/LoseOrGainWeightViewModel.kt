package com.mdshahsamir.makemymeal.ui.loseorgainweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.makemymeal.data.ai.GenerativeModelService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoseOrGainWeightViewModel @Inject constructor(
    private val generativeModelService: GenerativeModelService
) : ViewModel() {

    private val _weightUIState = MutableStateFlow<WeightUIState>(WeightUIState.Idle)
    val weightUIState: StateFlow<WeightUIState> = _weightUIState

    fun validateInput(gender: String, age: String, weight: String, loseOrGainWeight: String): Boolean {
        return !(gender.isEmpty() or age.isEmpty() or weight.isEmpty() or loseOrGainWeight.isEmpty())
    }

    fun generateContent(gender: String, age: String, weight: String, loseOrGainWeight: String) {
        _weightUIState.update { WeightUIState.Loading }
        val prompt = "My gender is $gender, age is $age and weight is $weight. I want to $loseOrGainWeight. With your analysis suggest me some recipes. Do no ask any information."

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModelService.generateResponse(prompt, null)
                _weightUIState.update { WeightUIState.Success(response) }
            } catch (e: Exception) {
                e.printStackTrace()
                _weightUIState.update { WeightUIState.Error("Something went wrong! Try again") }
            }
        }
    }
}

sealed interface WeightUIState {
    object Idle : WeightUIState
    object Loading : WeightUIState
    data class Success(val content: String) : WeightUIState
    data class Error(val errorMessage: String) : WeightUIState
}