package com.mdshahsamir.makemymeal.domain

import android.graphics.Bitmap
import com.mdshahsamir.makemymeal.data.ai.GenerativeModelRepository
import javax.inject.Inject

class MakeMealUseCase @Inject constructor(
    private val generativeModelRepository: GenerativeModelRepository
) {
    suspend operator fun invoke(
        mealType: String,
        cuisineType: String,
        image: Bitmap
    ): Result<String> {
        val prompt =
            "I want to make a $mealType meal of $cuisineType cuisine type with the ingredients in the attached image." +
                    " Create a recipe according to the instruction. In case not possible suggest alternative."

        return generativeModelRepository.generateResponse(prompt, image)
    }
}