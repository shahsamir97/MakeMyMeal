package com.mdshahsamir.makemymeal.data.ai

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.FinishReason
import com.google.ai.client.generativeai.type.content
import com.mdshahsamir.makemymeal.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenerativeModelRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): GenerativeModelRepository {

    override suspend fun generateResponse(prompt: String, image: Bitmap?): Result<String> {
        return runCatching {
            withContext(dispatcher) {
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
            }
                ?: throw Exception("Empty response from the generative model")
        }
    }
}