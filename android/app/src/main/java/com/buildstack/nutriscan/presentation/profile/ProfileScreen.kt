package com.buildstack.nutriscan.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.buildstack.nutriscan.presentation.components.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToHistory: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var selectedAllergies by remember { mutableStateOf(setOf<String>()) }
    var selectedDiets by remember { mutableStateOf(setOf<String>()) }

    val commonAllergies = listOf("Peanuts", "Dairy", "Gluten", "Soy", "Nuts", "Shellfish", "Eggs")
    val commonDiets = listOf("Vegan", "Vegetarian", "Keto", "Paleo", "Halal", "Kosher")

    LaunchedEffect(state.profile) {
        state.profile?.let { profile ->
            age = profile.age?.toString() ?: ""
            weight = profile.weight?.toString() ?: ""
            height = profile.height?.toString() ?: ""
            selectedAllergies = profile.allergies.toSet()
            selectedDiets = profile.dietaryPreferences.toSet()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile & Settings") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onNavigateToLogin()
                    }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "profile",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "scan" -> onNavigateToScan()
                        "history" -> onNavigateToHistory()
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
                if (state.saveSuccess) {
                    Text(text = "Profile updated successfully!", color = MaterialTheme.colorScheme.primary)
                }

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text("Allergies", style = MaterialTheme.typography.titleMedium)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonAllergies.forEach { allergy ->
                        FilterChip(
                            selected = selectedAllergies.contains(allergy),
                            onClick = {
                                selectedAllergies = if (selectedAllergies.contains(allergy)) {
                                    selectedAllergies - allergy
                                } else {
                                    selectedAllergies + allergy
                                }
                            },
                            label = { Text(allergy) }
                        )
                    }
                }

                Text("Dietary Preferences", style = MaterialTheme.typography.titleMedium)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    commonDiets.forEach { diet ->
                        FilterChip(
                            selected = selectedDiets.contains(diet),
                            onClick = {
                                selectedDiets = if (selectedDiets.contains(diet)) {
                                    selectedDiets - diet
                                } else {
                                    selectedDiets + diet
                                }
                            },
                            label = { Text(diet) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.updateProfile(
                            age = age.toIntOrNull(),
                            weight = weight.toFloatOrNull(),
                            height = height.toFloatOrNull(),
                            allergies = selectedAllergies.toList(),
                            dietaryPreferences = selectedDiets.toList()
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Save Profile")
                    }
                }
            }
        }
    }
}
