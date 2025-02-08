package com.ff.careai.navigation

sealed class ProfileSetupNavigation(val route: String) {
    object BasicInfo : ProfileSetupNavigation("basic_info")
    object MedicalInfo : ProfileSetupNavigation("medical_info")
}

data class ProfileSetupState(
    val name: String = "",
    val age: String = "",
    val gender: String = "",
    val medicalConditions: String = "",
    val allergies: String = ""
)
