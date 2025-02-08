package com.ff.careai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

// Add these imports at the top with other imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.SENT
)

enum class MessageStatus {
    SENT, DELIVERED, ERROR
}

@Composable
fun ChatScreen(
    viewModel: HealthcareViewModel = viewModel()
) {
    var messageText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isTyping by remember { mutableStateOf(false) }
    val maxCharLength = 500

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Healthcare Chat", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { messages = listOf() }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear chat"
                )
            }
        }

        // Chat messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                ChatBubble(message)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Enhanced input area
        Column {
            Text(
                "${messageText.length}/$maxCharLength",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = {
                        if (it.length <= maxCharLength) messageText = it
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your health question...") },
                    isError = messageText.length >= maxCharLength
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages = messages + ChatMessage(messageText, true)
                            isTyping = true
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank() && messageText.length < maxCharLength
                ) {
                    Text("Send")
                }
            }
        }

        // Handle UI states
        LaunchedEffect(uiState) {
            when (uiState) {
                is UiState.Success -> {
                    isTyping = false
                    messages = messages + ChatMessage(
                        (uiState as UiState.Success).outputText,
                        false
                    )
                }
                is UiState.Error -> {
                    isTyping = false
                    messages = messages + ChatMessage(
                        (uiState as UiState.Error).errorMessage,
                        false,
                        status = MessageStatus.ERROR
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (message.isUser)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(4.dp)
        ) {
            Column {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(8.dp),
                    color = if (message.isUser)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = formatTime(message.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
}
