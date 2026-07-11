package com.mdshahsamir.makemymeal.data.ai

import android.graphics.Bitmap

interface GenerativeModelRepository {

    suspend fun generateResponse(prompt: String, image: Bitmap?): Result<String>
}