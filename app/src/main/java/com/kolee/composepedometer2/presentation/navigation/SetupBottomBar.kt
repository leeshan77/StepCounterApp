package com.kolee.composepedometer2.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kolee.composepedometer2.ui.theme.BottomBarColor

@Composable
fun SetupBottomBar(
    navController: NavController
) {
    val listBottomItem = listOf(
        Screen.PedometerScreen,
        Screen.RecordScreen,
        Screen.ProfileScreen
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        containerColor = MaterialTheme.colors.BottomBarColor
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        listBottomItem.forEach { bottomItem ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = bottomItem.icon!!,
                        contentDescription = null,
                        tint = if (currentRoute == bottomItem.route) Color.DarkGray else Color.LightGray
                    )
                },
                label = {
                    Text(
                        text = stringResource(bottomItem.title!!),
                        style = TextStyle(fontSize = 12.sp, color = Color.LightGray)
                    )
                },
                selected = (currentRoute == bottomItem.route),
                onClick = {
                    navController.navigate(bottomItem.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                }
            )
        }
    }
}