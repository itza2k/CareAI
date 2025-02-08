package com.ff.careai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ff.careai.navigation.ProfileSetupState

@Composable
fun MedicalInfoStep(
    profileState: ProfileSetupState,
    onStateUpdate: (ProfileSetupState) -> Unit,
    onBack: () -> Unit,
    onComplete: (ProfileSetupState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Medical Information",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = profileState.medicalConditions,
            onValueChange = { onStateUpdate(profileState.copy(medicalConditions = it)) },
            label = { Text("Medical Conditions (if any)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            minLines = 3
        )

        OutlinedTextField(
            value = profileState.allergies,
            onValueChange = { onStateUpdate(profileState.copy(allergies = it)) },
            label = { Text("Allergies (if any)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            minLines = 3
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }
            Button(
                onClick = { onComplete(profileState) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Complete")
            }
        }
    }
}
