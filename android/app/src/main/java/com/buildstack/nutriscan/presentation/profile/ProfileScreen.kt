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

                var localImageBytes by remember { mutableStateOf<ByteArray?>(null) }
                var localMimeType by remember { mutableStateOf<String?>(null) }
                var localBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
                var localUri by remember { mutableStateOf<android.net.Uri?>(null) }
                val context = androidx.compose.ui.platform.LocalContext.current

                val galleryLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
                ) { uri ->
                    uri?.let {
                        localUri = it
                        localBitmap = null
                        val file = com.buildstack.nutriscan.util.ImageUtils.uriToFile(context, it)
                        if (file != null) {
                            localImageBytes = file.readBytes()
                            localMimeType = context.contentResolver.getType(it) ?: "image/jpeg"
                        }
                    }
                }

                val cameraLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview()
                ) { bitmap ->
                    bitmap?.let {
                        localBitmap = it
                        localUri = null
                        val bos = java.io.ByteArrayOutputStream()
                        it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, bos)
                        localImageBytes = bos.toByteArray()
                        localMimeType = "image/jpeg"
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val model: Any? = localBitmap ?: localUri ?: state.profile?.profilePic
                    
                    if (model != null) {
                        coil.compose.AsyncImage(
                            model = model,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .androidx.compose.foundation.shape.CircleShape
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .androidx.compose.ui.draw.clip(androidx.compose.foundation.shape.CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(androidx.compose.material.icons.Icons.Default.Person, contentDescription = null, modifier = Modifier.size(50.dp))
                        }
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        TextButton(onClick = { cameraLauncher.launch(null) }) {
                            Text("Take Photo")
                        }
                        TextButton(onClick = { galleryLauncher.launch("image/*") }) {
                            Text("Choose Gallery")
                        }
                    }
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
                            dietaryPreferences = selectedDiets.toList(),
                            imageBytes = localImageBytes,
                            mimeType = localMimeType
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
