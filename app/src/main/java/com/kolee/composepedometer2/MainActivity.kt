package com.kolee.composepedometer2

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.kolee.composepedometer2.presentation.permission.CheckAndRequestPermissions
import com.kolee.composepedometer2.presentation.screens.HomeScreen
import com.kolee.composepedometer2.ui.theme.ComposePedometer2Theme
import com.kolee.composepedometer2.worker.StepCounterWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val listOfPermissions = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) add(Manifest.permission.ACTIVITY_RECOGNITION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) add(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePedometer2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CheckAndRequestPermissions(
                        permissions = listOfPermissions
                    ) {
                        HomeScreen()
                    }

                    StepCounterWorker.periodicWork(this)
                }
            }
        }
    }
}
