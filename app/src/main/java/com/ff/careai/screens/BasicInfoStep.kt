package com.ff.careai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ff.careai.navigation.ProfileSetupState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoStep(
    profileState: ProfileSetupState,
    onStateUpdate: (ProfileSetupState) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Basic Information",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        var expanded by remember { mutableStateOf(false) }
        val genders = listOf("Male", "Female", "Other")

        OutlinedTextField(
            value = profileState.name,
            onValueChange = { onStateUpdate(profileState.copy(name = it)) },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = profileState.age,
            onValueChange = { if (it.all { char -> char.isDigit() || it.isEmpty() })
                onStateUpdate(profileState.copy(age = it))
            },
            label = { Text("Age") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = profileState.gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genders.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            onStateUpdate(profileState.copy(gender = gender))
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            enabled = profileState.name.isNotBlank() &&
                    profileState.age.isNotBlank() &&
                    profileState.gender.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Next")
        }
    }
}
