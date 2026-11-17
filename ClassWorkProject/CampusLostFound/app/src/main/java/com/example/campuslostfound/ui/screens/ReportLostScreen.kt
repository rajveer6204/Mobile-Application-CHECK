package com.example.campuslostfound.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuslostfound.data.entity.ItemType
import com.example.campuslostfound.ui.components.CustomTextField
import com.example.campuslostfound.ui.components.TopHeader
import com.example.campuslostfound.ui.theme.BackgroundLight
import com.example.campuslostfound.ui.theme.BorderLight
import com.example.campuslostfound.ui.theme.LostRed
import com.example.campuslostfound.ui.theme.LostRedContainer
import com.example.campuslostfound.ui.theme.PrimaryPurple
import com.example.campuslostfound.ui.theme.SurfaceWhite
import com.example.campuslostfound.ui.theme.TextMuted
import com.example.campuslostfound.ui.theme.TextPrimary
import com.example.campuslostfound.ui.theme.TextSecondary
import com.example.campuslostfound.viewmodel.AuthViewModel
import com.example.campuslostfound.viewmodel.ItemViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportLostScreen(
    authViewModel: AuthViewModel,
    itemViewModel: ItemViewModel,
    onBackClick: () -> Unit,
    onSubmitSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    var itemName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Electronics") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf(currentUser?.email ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isCategoryExpanded by remember { mutableStateOf(false) }

    val categories = listOf("Electronics", "Documents", "Keys", "Bags", "Books", "Clothing", "Others")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
    }

    val todayDate = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        TopHeader(
            title = "Report Lost Item",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(LostRedContainer)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(LostRed)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Fill in the item details to alert students nearby",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LostRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Item Name
            CustomTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = "Item Name *",
                placeholder = "e.g., Apple Watch, Black Wallet, Hostel Keys",
                leadingIcon = Icons.Default.Label
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selector Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Category *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = isCategoryExpanded,
                    onExpandedChange = { isCategoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                        leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = TextSecondary) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceWhite,
                            unfocusedContainerColor = SurfaceWhite,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = BorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    isCategoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Last Seen Location
            CustomTextField(
                value = location,
                onValueChange = { location = it },
                label = "Last Seen Location *",
                placeholder = "e.g., Central Library 2nd Floor, Lab 3",
                leadingIcon = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            CustomTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description *",
                placeholder = "Include details like color, scratches, stickers, or brand...",
                leadingIcon = Icons.Default.Description,
                singleLine = false,
                modifier = Modifier.height(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Info
            CustomTextField(
                value = contactInfo,
                onValueChange = { contactInfo = it },
                label = "Contact Info / Email",
                placeholder = "student@campus.edu or +1 555-0199",
                leadingIcon = Icons.Default.Phone
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Photo Upload Picker Box
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Upload Item Image",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Upload",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (selectedImageUri != null) "Image Selected! Tap to change" else "Tap to select photo from gallery",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedImageUri != null) PrimaryPurple else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    if (itemName.isNotBlank() && description.isNotBlank() && location.isNotBlank()) {
                        itemViewModel.reportItem(
                            itemName = itemName,
                            category = selectedCategory,
                            description = description,
                            location = location,
                            imageUri = selectedImageUri?.toString() ?: "",
                            itemType = ItemType.LOST,
                            userId = currentUser?.id ?: 1,
                            uploaderName = currentUser?.fullName ?: "Campus Student",
                            date = todayDate,
                            contactInfo = contactInfo,
                            onSuccess = onSubmitSuccess
                        )
                    }
                },
                enabled = itemName.isNotBlank() && description.isNotBlank() && location.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LostRed)
            ) {
                Text(
                    text = "Submit Lost Item Report",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SurfaceWhite
                )
            }
        }
    }
}
