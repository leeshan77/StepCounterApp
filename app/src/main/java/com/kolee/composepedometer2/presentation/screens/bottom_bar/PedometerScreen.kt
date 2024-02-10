package com.kolee.composepedometer2.presentation.screens.bottom_bar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kolee.composepedometer2.R
import com.kolee.composepedometer2.common.DateUtil
import com.kolee.composepedometer2.domain.viewmodel.ShareViewModel
import com.kolee.composepedometer2.presentation.screens.components.AnimatedCircularIndicator
import com.kolee.composepedometer2.presentation.screens.components.CustomDialog
import com.kolee.composepedometer2.presentation.screens.components.CustomText
import com.kolee.composepedometer2.service.RealtimeStepCounterService
import com.kolee.composepedometer2.ui.theme.Dimens
import com.kolee.composepedometer2.ui.theme.TextDefaultColor
import kotlinx.coroutines.delay

private const val TAG = "PedometerScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PedometerScreen(
    shareViewModel: ShareViewModel = hiltViewModel()
) {
//    Text(text = "Pedometer Screen")

    val context = LocalContext.current

    IgnoreBatteryOptimizations()

    val shareUiState by shareViewModel.uiState.collectAsState()
    val stepsToday = shareUiState.stepsToday

    var timeOfClock by remember { mutableStateOf("") }
    var dateOfClock by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        while (true) {
            timeOfClock = DateUtil.timeOfClock()
            dateOfClock = DateUtil.dateOfClock()
            delay(500L)
        }
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {},
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Dimens.Seven))
                Text(
                    text = timeOfClock,
                    style = TextStyle(fontSize = 28.sp, color = MaterialTheme.colors.TextDefaultColor)
                )
                Text(
                    text = dateOfClock,
                    style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.TextDefaultColor)
                )
                Spacer(modifier = Modifier.height(Dimens.Four))
                AnimatedCircularIndicator(stepsToday.toInt(), 10000)
                Spacer(modifier = Modifier.height(Dimens.Seven))
                Row {
                    val stride = 65
                    val weight = 73
                    val distance = (stepsToday * stride / 100) / 1000f

                    CustomText(
                        text = "%.1f".format(distance * weight) + " kcal",
                        leadingIcon = painterResource(id = R.drawable.baseline_local_fire_department_24)
                    )
                    Spacer(modifier = Modifier.width(Dimens.Two))
                    CustomText(
                        text = "%.2f".format(distance) + " km",
                        leadingIcon = painterResource(id = R.drawable.baseline_moving_24)
                    )
                }
            }
        },
        floatingActionButton = {},
        scaffoldState = scaffoldState
    )

    ComposableLifeCycle { _, event ->
        val intent = Intent(context, RealtimeStepCounterService::class.java)

        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "PedometerScreen: ON_RESUME")
                context.startForegroundService(intent)
            }
            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "PedometerScreen: ON_PAUSE")
                context.stopService(intent)
            }
            else -> {}
        }
    }
}

@Composable
fun ComposableLifeCycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@SuppressLint("ObsoleteSdkInt", "BatteryLife")
@Composable
fun IgnoreBatteryOptimizations() {
    val activity = LocalContext.current as Activity

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

    val pm = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
    val packName = activity.packageName
    val isWhiteListing = pm.isIgnoringBatteryOptimizations(packName)

    if (isWhiteListing.not()) {
        CustomDialog(
            title = stringResource(id = R.string.ignore_dialog_title),
            text = stringResource(id = R.string.ignore_dialog_text),
            confirm = stringResource(id = R.string.ignore_dialog_confirm)
        ) {
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
                it.data = Uri.parse("package:$packName")
                activity.startActivity(it)
            }
        }
    }
}

