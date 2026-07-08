package com.mdshahsamir.makemymeal.data.ai

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GenerativeModelServiceImpl @Inject constructor(
    private val generativeModel: GenerativeModel
): GenerativeModelService {

    override suspend fun generateResponse(prompt: String, image: Bitmap?): String {
        val content = content {
            if (image != null) {
                image(image)
            }
            text(prompt)
        }

        val response = generativeModel.generateContent(content)

        return response.text!!
    }
}