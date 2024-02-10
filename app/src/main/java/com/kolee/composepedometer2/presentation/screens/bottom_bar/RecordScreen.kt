package com.kolee.composepedometer2.presentation.screens.bottom_bar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kolee.composepedometer2.common.DateUtil
import com.kolee.composepedometer2.domain.viewmodel.ShareViewModel
import com.kolee.composepedometer2.presentation.screens.components.BarChart
import com.kolee.composepedometer2.room_db.DailyStepsEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RecordScreen(
    shareViewModel: ShareViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val shareUiState by shareViewModel.uiState.collectAsState()

    val scaffoldState = rememberScaffoldState()

    var pageShowed by remember { mutableStateOf(0) }
    var listShowed by remember { mutableStateOf(emptyList<BarChartInput>()) }

    var demoList by remember { mutableStateOf(emptyList<DailyStepsEntity>()) }

    LaunchedEffect(key1 = true) {
        getDemoList(shareUiState.stepsAllDays.toMutableList()) {
            demoList = it
        }

        val size = demoList.size
        val fromIndex: Int = if (size > 7) size - 7 else 0

        val tempList = mutableListOf<BarChartInput>()
        for (i in (fromIndex until size)) {
            val date = LocalDate.ofEpochDay(demoList[i].epochDay)
            val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.US)
            tempList.add(
                BarChartInput(
                    date.format(formatter),
                    demoList[i].steps.toInt()
                )
            )
        }

        listShowed = tempList
    }

    Scaffold(
        topBar = {},
        content = { innerPadding ->
            MainContent(
                innerPadding = innerPadding,
                allDaysSteps = demoList,
                listShowed = listShowed,
                onLeftButton = {

                },
                onRightButton = {

                }
            )
        },
        floatingActionButton = {},
        scaffoldState = scaffoldState
    )
}

@Composable
fun MainContent(
    innerPadding: PaddingValues,
    allDaysSteps: List<DailyStepsEntity>,
    listShowed: List<BarChartInput>,
    onLeftButton: () -> Unit,
    onRightButton: () -> Unit
) {
    if (listShowed.isNotEmpty()) {
        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            BarChart(
                data = listShowed,
                height = 300.dp
            )
        }
    }
}

data class BarChartInput(
    val timeStamp: String,
    val steps: Int
)

private fun getDemoList(
    list: MutableList<DailyStepsEntity>,
    callBack: (List<DailyStepsEntity>) -> Unit
) {
    val tempList = mutableListOf(
        DailyStepsEntity(DateUtil.getToday()-19, 15500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-18, 4500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-17, 2500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-15, 13500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-14, 17500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-13, 4500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-12, 15500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-11, 4500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-10, 2500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-9, 13500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-7, 17500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-6, 4500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-4, 10500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-3, 3500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-2, 6500, DateUtil.formattedCurrentTime()),
        DailyStepsEntity(DateUtil.getToday()-1, 13500, DateUtil.formattedCurrentTime())
    )

    list.forEach { tempList.add(it) }
    callBack.invoke(tempList)
}