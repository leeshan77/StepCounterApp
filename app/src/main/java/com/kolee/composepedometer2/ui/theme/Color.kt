package com.kolee.composepedometer2.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val RoyalBlue = Color(0xFF4169E1)
val LightBlack = Color(0xFF272727)

val LightPastel = Color(0xFFFBE9E7)
val DarkPastel = Color(0xFFFF8A65)

val Colors.TextDefaultColor: Color
    @Composable get() = if (isLight) Color(0xFF2B2B2B) else Color(0xFFCECECE)
val Colors.TextHintColor: Color
    @Composable get() = if (isLight) Color(0xFF848484) else Color(0xFF9B9B9B)
val Colors.BottomBarColor: Color
    @Composable get() = if (isLight) Color(0xFF4169E1) else Color(0xFF2B2B2B)