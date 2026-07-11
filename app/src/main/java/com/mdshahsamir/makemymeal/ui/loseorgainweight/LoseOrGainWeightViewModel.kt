package com.mdshahsamir.makemymeal.ui.loseorgainweight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdshahsamir.makemymeal.domain.CreateDietMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoseOrGainWeightViewModel @Inject constructor(
    private val createDietMealUseCase: CreateDietMealUseCase

) : ViewModel() {

    private val _weightUIState = MutableStateFlow<WeightUIState>(WeightUIState.Idle)
    val weightUIState: StateFlow<WeightUIState> = _weightUIState

    fun validateInput(gender: String, age: String, weight: String, loseOrGainWeight: String): Boolean {
        return !(gender.isEmpty() or age.isEmpty() or weight.isEmpty() or loseOrGainWeight.isEmpty())
    }

    fun generateContent(gender: String, age: String, weight: String, loseOrGainWeight: String) {
        _weightUIState.update { WeightUIState.Loading }

        viewModelScope.launch {
            createDietMealUseCase(age, gender, weight, loseOrGainWeight)
                .onSuccess { content ->
                    _weightUIState.update { WeightUIState.Success(content) }
                }
                .onFailure { error ->
                    _weightUIState.update { WeightUIState.Error(error.message ?: "An error occurred") }
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