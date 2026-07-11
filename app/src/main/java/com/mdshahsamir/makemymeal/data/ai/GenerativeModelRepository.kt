package com.mdshahsamir.makemymeal.data.ai

import android.graphics.Bitmap

interface GenerativeModelService {

    suspend fun generateResponse(prompt: String, image: Bitmap?): Result<String>
}