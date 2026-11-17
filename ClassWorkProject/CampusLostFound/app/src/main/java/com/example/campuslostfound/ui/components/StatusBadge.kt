package com.example.campuslostfound.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuslostfound.data.entity.ItemStatus
import com.example.campuslostfound.data.entity.ItemType
import com.example.campuslostfound.ui.theme.FoundGreen
import com.example.campuslostfound.ui.theme.FoundGreenContainer
import com.example.campuslostfound.ui.theme.LostRed
import com.example.campuslostfound.ui.theme.LostRedContainer
import com.example.campuslostfound.ui.theme.PrimaryPurple
import com.example.campuslostfound.ui.theme.TextMuted

@Composable
fun ItemTypeBadge(type: ItemType, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (type) {
        ItemType.LOST -> Triple(LostRedContainer, LostRed, "LOST")
        ItemType.FOUND -> Triple(FoundGreenContainer, FoundGreen, "FOUND")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ItemStatusBadge(status: ItemStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (status) {
        ItemStatus.ACTIVE -> Triple(PrimaryPurple.copy(alpha = 0.12f), PrimaryPurple, "ACTIVE")
        ItemStatus.RESOLVED -> Triple(Color(0xFFE0E0E0), TextMuted, "RESOLVED")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
