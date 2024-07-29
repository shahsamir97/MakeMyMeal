package com.mdshahsamir.makemymeal.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.mdshahsamir.makemymeal.BuildConfig

/**Access API key from BuildConfig file
 * You can create a apiKey variable in local.properties file and enable buildConfig in you app level build.gradle file*/
val generativeModel = GenerativeModel(
    modelName = "gemini-1.5-flash",
    apiKey = BuildConfig.apiKey,
    generationConfig =  GenerationConfig.builder().apply { temperature = 1F }.build()
)
