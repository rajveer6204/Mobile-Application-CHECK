package com.example.digitallibary.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val NavyPrimary = Color(0xFF172554)
val NavyLight = Color(0xFF1E3A8A)
val NavyDark = Color(0xFF0F172A)

val PurpleAccent = Color(0xFF7C3AED)
val PurpleLight = Color(0xFF8B5CF6)
val PurpleDark = Color(0xFF5B21B6)

val BackgroundLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF1F5F9)

val BackgroundDark = Color(0xFF0B132B)
val SurfaceDark = Color(0xFF1E293B)
val SurfaceVariantDark = Color(0xFF334155)

val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF64748B)

val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)

val RatingStar = Color(0xFFF59E0B)
val SuccessGreen = Color(0xFF10B981)
val ErrorRed = Color(0xFFEF4444)

// Book Cover Gradient Palette
val BookCoverGradients = listOf(
    Brush.linearGradient(listOf(Color(0xFF1E1B4B), Color(0xFF4C1D95))), // Navy to Deep Purple
    Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF047857))), // Emerald
    Brush.linearGradient(listOf(Color(0xFF7C2D12), Color(0xFFC2410C))), // Warm Amber / Rust
    Brush.linearGradient(listOf(Color(0xFF831843), Color(0xFFBE185D))), // Rose Indigo
    Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0284C7))), // Royal Blue / Cyan
    Brush.linearGradient(listOf(Color(0xFF312E81), Color(0xFF6366F1)))  // Indigo / Violet
)
