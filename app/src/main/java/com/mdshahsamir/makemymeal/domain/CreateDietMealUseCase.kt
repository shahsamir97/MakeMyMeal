package com.mdshahsamir.makemymeal.domain

import com.mdshahsamir.makemymeal.data.ai.GenerativeModelRepository
import javax.inject.Inject

class CreateDietMealUseCase @Inject constructor(
    private val generativeModelRepository: GenerativeModelRepository
) {
    suspend operator fun invoke(age: String, gender: String, weight: String, dietType: String): Result<String> {
        val prompt =
            "I am a $age year old $gender weighing $weight kg. I want to follow a $dietType diet. Please provide me with a meal plan that is suitable."

        return generativeModelRepository.generateResponse(prompt, null)
    }
}