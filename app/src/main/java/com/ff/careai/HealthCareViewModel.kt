package com.ff.careai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthcareViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    fun sendMessage(userMessage: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val prompt = """
                    You are a helpful and knowledgeable healthcare assistant. Provide accurate, 
                    professional medical information while making it clear that you're not replacing 
                    professional medical advice. Always encourage users to consult healthcare 
                    professionals for serious concerns.
                    
                    User query: $userMessage
                """.trimIndent()

                val response = generativeModel.generateContent(content { text(prompt) })
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Error processing request")
            }
        }
    }
}
