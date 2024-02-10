package com.kolee.composepedometer2.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.kolee.composepedometer2.R

sealed class Screen(
    @StringRes val title: Int?,
    val icon: ImageVector?,
    val route: String
) {
    object PedometerScreen: Screen(R.string.Pedometer, Icons.Default.DirectionsWalk, "pedometer_screen")
    object RecordScreen: Screen(R.string.Record, Icons.Default.BarChart, "record_screen")
    object ProfileScreen: Screen(R.string.Profile, Icons.Default.Person, "profile_screen")
    object HomeScreen: Screen(null, null, "home_screen")
}
