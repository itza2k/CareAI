package com.ff.careai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ff.careai.data.UserProfileManager

@Composable
fun ProfileScreen(userProfileManager: UserProfileManager) {
    val userProfile by userProfileManager.userProfile.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        userProfile?.let { profile ->
            ProfileItem("Name", profile.name)
            ProfileItem("Age", profile.age.toString())
            ProfileItem("Gender", profile.gender)
            ProfileItem("Medical Conditions", profile.medicalConditions)
            ProfileItem("Allergies", profile.allergies)
        }
    }
}

@Composable
private fun ProfileItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value.ifEmpty { "Not specified" },
            style = MaterialTheme.typography.bodyLarge
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
