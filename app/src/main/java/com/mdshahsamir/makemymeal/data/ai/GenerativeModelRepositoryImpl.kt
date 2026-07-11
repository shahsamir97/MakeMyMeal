package com.mdshahsamir.makemymeal.data.ai

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GenerativeModelServiceImpl @Inject constructor(
    private val generativeModel: GenerativeModel
): GenerativeModelService {

    override suspend fun generateResponse(prompt: String, image: Bitmap?): Result<String> {
        return runCatching {
            val response = generativeModel.generateContent(
                content {
                    if (image != null) {
                        image(image)
                    }

                    text(prompt)
                }
            )

            response.text
                ?.takeIf { it.isNotEmpty() }
                ?: throw Exception("Empty response from the generative model")
        }
    }
}