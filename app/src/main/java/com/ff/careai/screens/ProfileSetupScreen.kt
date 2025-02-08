package com.ff.careai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ff.careai.data.UserProfile
import com.ff.careai.data.UserProfileManager
import com.ff.careai.navigation.ProfileSetupNavigation
import com.ff.careai.navigation.ProfileSetupState
import kotlinx.coroutines.launch

@Composable
fun ProfileSetupScreen(
    navController: NavHostController,
    userProfileManager: UserProfileManager,
    onProfileSetupComplete: () -> Unit
) {
    var profileSetupState by remember { mutableStateOf(ProfileSetupState()) }
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = ProfileSetupNavigation.BasicInfo.route
    ) {
        composable(ProfileSetupNavigation.BasicInfo.route) {
            BasicInfoScreen(
                profileSetupState = profileSetupState,
                onNext = { updatedState ->
                    profileSetupState = updatedState
                    navController.navigate(ProfileSetupNavigation.MedicalInfo.route)
                }
            )
        }
        composable(ProfileSetupNavigation.MedicalInfo.route) {
            MedicalInfoScreen(
                profileSetupState = profileSetupState,
                onSave = { updatedState ->
                    profileSetupState = updatedState
                    scope.launch {
                        userProfileManager.saveProfile(
                            UserProfile(
                                name = profileSetupState.name,
                                age = profileSetupState.age.toIntOrNull() ?: 0,
                                gender = profileSetupState.gender,
                                medicalConditions = profileSetupState.medicalConditions,
                                allergies = profileSetupState.allergies
                            )
                        )
                        onProfileSetupComplete()
                    }
                }
            )
        }
    }
}

@Composable
fun BasicInfoScreen(
    profileSetupState: ProfileSetupState,
    onNext: (ProfileSetupState) -> Unit
) {
    var name by remember { mutableStateOf(profileSetupState.name) }
    var age by remember { mutableStateOf(profileSetupState.age) }
    var gender by remember { mutableStateOf(profileSetupState.gender) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Basic Information",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onNext(ProfileSetupState(name, age, gender))
            },
            enabled = name.isNotBlank() && age.isNotBlank() && gender.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

@Composable
fun MedicalInfoScreen(
    profileSetupState: ProfileSetupState,
    onSave: (ProfileSetupState) -> Unit
) {
    var medicalConditions by remember { mutableStateOf(profileSetupState.medicalConditions) }
    var allergies by remember { mutableStateOf(profileSetupState.allergies) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Medical Information",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = medicalConditions,
            onValueChange = { medicalConditions = it },
            label = { Text("Medical Conditions (if any)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = allergies,
            onValueChange = { allergies = it },
            label = { Text("Allergies (if any)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onSave(profileSetupState.copy(medicalConditions = medicalConditions, allergies = allergies))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }
    }
}
