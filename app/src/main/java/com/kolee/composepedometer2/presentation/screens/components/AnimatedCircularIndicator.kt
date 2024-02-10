package com.kolee.composepedometer2.presentation.screens.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolee.composepedometer2.ui.theme.*

@Composable
fun AnimatedCircularIndicator(
    currentValue: Int,
    maxValue: Int,
    backgroundColor: Color = LightPastel,
    indicatorColor: Color = DarkPastel
) {
    val strokeCircle = with(LocalDensity.current) {
        Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
    }

    val strokeArc = with(LocalDensity.current) {
        Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
    }

    Box(contentAlignment = Alignment.Center) {
        ShowText(
            currentValue = currentValue,
            maxValue = maxValue
        )

        val targetValue = currentValue / maxValue.toFloat()

        Canvas(
            Modifier
                .progressSemantics(currentValue / maxValue.toFloat())
                .size(240.dp)
        ) {
            val startAngle = 270f    // Start at 12 O'clock
            val sweep: Float = targetValue * 360f
            val diameterOffset = strokeCircle.width / 2 - 6  //-6 : Set Gap between 2 Circles

            drawCircle(
                color = backgroundColor,
                style = strokeCircle,
                radius = size.minDimension / 2.0f - diameterOffset
            )

            drawCircularIndicator(
                startAngle, sweep, strokeArc
            )
        }
    }
}

@Composable
private fun ShowText(
    currentValue: Int,
    maxValue: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = DecimalFormat("#,###").format(currentValue),
            style = TextStyle(
                color = MaterialTheme.colors.TextDefaultColor,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "/" + DecimalFormat("#,###").format(maxValue),
            style = TextStyle(
                color = MaterialTheme.colors.TextHintColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset

    drawArc(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFFDD835),
                Color(0xFFFF8A65),
                Color(0xFFBB86FC),
                Color(0xFF1D41C5)
            )
        ),

        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

@Preview(showBackground = true)
@Composable
fun CircularIndicatorPreview() {
    ComposePedometer2Theme {
        AnimatedCircularIndicator(currentValue = 2000, maxValue = 3000)
    }
}

