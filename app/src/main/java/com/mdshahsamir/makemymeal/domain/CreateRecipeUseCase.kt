package com.mdshahsamir.makemymeal.domain

import android.graphics.Bitmap
import com.mdshahsamir.makemymeal.data.ai.GenerativeModelRepository
import javax.inject.Inject

class CreateRecipeUseCase @Inject constructor(
    private val generativeModelRepository: GenerativeModelRepository
) {
    suspend operator fun invoke(image: Bitmap): Result<String> =
        generativeModelRepository.generateResponse(CREATE_RECIPE_PROMPT, image)


    companion object {
        const val CREATE_RECIPE_PROMPT = "Accurately identify the food in the image and provide an appropriate and recipe consistent with your analysis. Do not ask any question"
    }
}