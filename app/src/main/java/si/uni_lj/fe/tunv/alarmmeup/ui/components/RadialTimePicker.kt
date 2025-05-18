package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun RadialTimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    isAm: Boolean,
    isSelectingHour: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onTimeChange: (Int, Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var hourInput by remember { mutableStateOf(selectedHour.toString().padStart(2, '0')) }
    var minuteInput by remember { mutableStateOf(selectedMinute.toString().padStart(2, '0')) }


    LaunchedEffect(selectedHour) { hourInput = selectedHour.toString().padStart(2, '0') }
    LaunchedEffect(selectedMinute) { minuteInput = selectedMinute.toString().padStart(2, '0') }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(80.dp)
                    .width(120.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = hourInput,
                        onValueChange = {
                            if (it.length <= 2 && it.all { c -> c.isDigit() }) {
                                hourInput = it
                                val hour = it.toIntOrNull()?.coerceIn(1, 12) ?: selectedHour
                                val minute = minuteInput.toIntOrNull()?.coerceIn(0, 59) ?: selectedMinute
                                onTimeChange(hour, minute, isAm)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.width(50.dp)
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    TextField(
                        value = minuteInput,
                        onValueChange = {
                            if (it.length <= 2 && it.all { c -> c.isDigit() }) {
                                minuteInput = it
                                val hour = hourInput.toIntOrNull()?.coerceIn(1, 12) ?: selectedHour
                                val minute = it.toIntOrNull()?.coerceIn(0, 59) ?: selectedMinute
                                onTimeChange(hour, minute, isAm)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.width(50.dp)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = { onTimeChange(selectedHour, selectedMinute, true) },
                    modifier = Modifier
                        .height(40.dp)
                        .width(60.dp)
                ) {
                    Text(
                        text = "AM",
                        color = if (isAm) Color.Black else Color.Gray
                    )
                }
                TextButton(
                    onClick = { onTimeChange(selectedHour, selectedMinute, false) },
                    modifier = Modifier
                        .height(40.dp)
                        .width(60.dp)
                ) {
                    Text(
                        text = "PM",
                        color = if (!isAm) Color.Black else Color.Gray
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(500.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val boxSize = min(this.size.width, this.size.height).toFloat();                        val center = Offset(boxSize / 2, boxSize / 2)
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        val distance = hypot(dx, dy)
                        val hourRadius = boxSize / 3
                        val minuteNumberRadius = hourRadius - 100
                        val hourDist = abs(distance - (hourRadius - 10))
                        val minuteDist = abs(distance - minuteNumberRadius)
                        if (hourDist < minuteDist) {
                            val angle = (atan2(dy, dx).toDouble() * 180 / Math.PI + 360 + 90) % 360
                            val hour = ((angle / 30).roundToInt() + 12) % 12
                            onTimeChange(if (hour == 0) 12 else hour, selectedMinute, isAm)
                            onSelectionChange(true)
                        } else {
                            val angle = (atan2(dy, dx).toDouble() * 180 / Math.PI + 360 + 90) % 360
                            val minute = ((angle / 6).roundToInt()) % 60
                            onTimeChange(selectedHour, minute, isAm)
                            onSelectionChange(false)
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val boxSize = size.minDimension
                val center = Offset(boxSize / 2, boxSize / 2)
                val hourRadius = boxSize / 3
                val minuteRadius = boxSize / 2.5f
                val minuteNumberRadius = hourRadius - 100

                drawCircle(Color.LightGray, center = center, radius = minuteRadius)

                // Draw hour hand and its end circle
                val hourAngle = Math.toRadians((selectedHour * 30 - 90).toDouble())
                val hourX = center.x + cos(hourAngle) * (hourRadius - 0)
                val hourY = center.y + sin(hourAngle) * (hourRadius - 0)
                drawLine(
                    Color.Gray,
                    start = center,
                    end = Offset(hourX.toFloat(), hourY.toFloat()),
                    strokeWidth = 8f
                )
                drawCircle(
                    color = Color.Gray,
                    radius = 45f,
                    center = Offset(hourX.toFloat(), hourY.toFloat())
                )

                // Draw minute hand and its end circle
                val minuteAngle = Math.toRadians((selectedMinute * 6 - 90).toDouble())
                val minuteX = center.x + cos(minuteAngle) * (minuteRadius - 158)
                val minuteY = center.y + sin(minuteAngle) * (minuteRadius - 158)
                drawLine(
                    Color.Gray,
                    start = center,
                    end = Offset(minuteX.toFloat(), minuteY.toFloat()),
                    strokeWidth = 8f
                )
                drawCircle(
                    color = Color.Gray,
                    radius = 30f,
                    center = Offset(minuteX.toFloat(), minuteY.toFloat())
                )

                // Draw hour numbers (outer circle)
                for (h in 1..12) {
                    val angle = Math.toRadians((h * 30 - 90).toDouble())
                    val x = center.x + cos(angle) * (hourRadius - 10)
                    val y = center.y + sin(angle) * (hourRadius - 10)
                    drawContext.canvas.nativeCanvas.apply {
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 60f
                            textAlign = android.graphics.Paint.Align.CENTER
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }
                        drawText(
                            h.toString(),
                            x.toFloat(),
                            y.toFloat() + 30,
                            paint
                        )
                    }
                }

                // Draw minute numbers (inner circle)
                for (m in 0 until 60 step 5) {
                    val angle = Math.toRadians((m * 6 - 90).toDouble())
                    val x = center.x + cos(angle) * minuteNumberRadius
                    val y = center.y + sin(angle) * minuteNumberRadius
                    val minuteText = m.toString().padStart(2, '0')
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            minuteText,
                            x.toFloat(),
                            y.toFloat() + 22,
                            android.graphics.Paint().apply {
                                color = Color.White.toArgb()
                                textSize = 45f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }
    }
}