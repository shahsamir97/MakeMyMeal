package com.mdshahsamir.makemymeal.repo

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.mdshahsamir.makemymeal.data.ai.GenerativeModelRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GenerativeModelRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: GenerativeModelRepositoryImpl
    private lateinit var generativeModel: GenerativeModel

    @Before
    fun setUp() {
        // Initialize the mock GenerativeModel and the repository
        generativeModel = mockk()
        repository = GenerativeModelRepositoryImpl(generativeModel, dispatcher)
    }

    @Test
    fun `generateResponse returns text when model responds successfully`() = runTest(dispatcher) {
        //Given
        val expectedText = "Generated response"
        val response = mockk<GenerateContentResponse> {
            coEvery { text } returns expectedText
        }
        coEvery { generativeModel.generateContent(any<Content>()) } returns response

        //When
        val result = repository.generateResponse("Test prompt", null)

        //Then
        assert(result.isSuccess)
        assert(result.getOrNull() == expectedText)
    }

    @Test
    fun `generateResponse fails when model returns empty text`() = runTest(dispatcher) {
        val response = mockk<GenerateContentResponse> {
            coEvery { text } returns ""
        }
        coEvery { generativeModel.generateContent(any<Content>()) } returns response

        val result = repository.generateResponse("Test prompt", null)

        assert(result.isFailure)
    }
}