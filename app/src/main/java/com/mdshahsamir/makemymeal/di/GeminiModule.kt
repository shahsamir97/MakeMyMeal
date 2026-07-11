package com.mdshahsamir.makemymeal.di

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.mdshahsamir.makemymeal.BuildConfig
import com.mdshahsamir.makemymeal.data.ai.GenerativeModelRepository
import com.mdshahsamir.makemymeal.data.ai.GenerativeModelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GeminiModule {


    @Binds
    abstract fun bindGenerativeModelService(
        generativeModelServiceImpl: GenerativeModelRepositoryImpl
    ): GenerativeModelRepository

    companion object {
        @Provides
        fun provideGeminiService(): GenerativeModel {
            return GenerativeModel(
                modelName = "gemini-3.5-flash",
                apiKey = BuildConfig.apiKey,
                generationConfig = GenerationConfig.builder().apply { temperature = 1F }.build()
            )
        }
    }
}