package com.mdshahsamir.makemymeal.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.mdshahsamir.makemymeal.BuildConfig

val generativeModel = GenerativeModel(
    modelName = "gemini-1.5-flash",
    // Access your API key as a Build Configuration variable (see "Set up your API key" above)
    apiKey = BuildConfig.apiKey
)