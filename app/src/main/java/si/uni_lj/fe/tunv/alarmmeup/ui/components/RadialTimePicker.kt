package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AccentColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.GrayColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.MainColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.SecondaryColor
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun RadialTimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    isAm: Boolean,
    isSelectingHour: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onTimeChange: (Int, Int, Boolean) -> Unit,
    modifier: Modifier = Modifier.height(400.dp),
) {
    var hourInput by remember { mutableStateOf(TextFieldValue(selectedHour.toString().padStart(2, '0'))) }
    var minuteInput by remember { mutableStateOf(TextFieldValue(selectedMinute.toString().padStart(2, '0'))) }

    var hourFocused by remember { mutableStateOf(false) }
    var minuteFocused by remember { mutableStateOf(false) }

    val hourFocusRequester = remember { FocusRequester() }
    val minuteFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var prevHourLength by remember { mutableStateOf(hourInput.text.length) }

    var localHour by remember { mutableStateOf(selectedHour) }
    var localMinute by remember { mutableStateOf(selectedMinute) }


    LaunchedEffect(selectedHour, selectedMinute) {
        if (!hourFocused) hourInput = TextFieldValue(selectedHour.toString().padStart(2, '0'))
        if (!minuteFocused) minuteInput = TextFieldValue(selectedMinute.toString().padStart(2, '0'))
        localHour = selectedHour
        localMinute = selectedMinute
    }

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
                    .height(90.dp)
                    .width(175.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = hourInput,
                        onValueChange = {
                            val hourText = it.text
                            if (hourText.length <= 2 && hourText.all { c -> c.isDigit() }) {
                                val hourVal = hourText.toIntOrNull()
                                if (hourVal == null || (hourVal in 0..24)) {
                                    hourInput = it
                                    val hour = hourVal ?: selectedHour
                                    val minute = minuteInput.text.toIntOrNull()?.coerceIn(0, 59) ?: selectedMinute
                                    onTimeChange(hour, minute, isAm)
                                    // Only move focus if user just entered the 2nd digit
                                    if (prevHourLength < 2 && hourText.length == 2 && hourFocused) {
                                        hourFocused = false
                                        minuteFocusRequester.requestFocus()
                                    }
                                    prevHourLength = hourText.length
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .width(80.dp)
                            .focusRequester(hourFocusRequester)
                            .onFocusChanged { focusState ->
                                hourFocused = focusState.isFocused
                                if (focusState.isFocused) {
                                    minuteFocused = false
                                    hourInput = hourInput.copy(selection = TextRange(0, hourInput.text.length))
                                }
                            }
                            .alignByBaseline(),
                        colors =TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = SecondaryColor,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = BlackColor,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 50.sp,
                            color= BlackColor
                        )
                    )
                    Text(
                        text = ":",
                        fontSize = 50.sp,
                        color = BlackColor,
                        modifier = Modifier
                            .alignByBaseline()
                    )
                    TextField(
                        value = minuteInput,
                        onValueChange = {
                            val MinutesText = it.text
                            if (MinutesText.length <= 2 && MinutesText.all { c -> c.isDigit() }) {
                                val minuteVal = MinutesText.toIntOrNull()
                                if (minuteVal == null || (minuteVal in 0..59)) {
                                    minuteInput = it
                                    val hour = hourInput.text.toIntOrNull()?.coerceIn(0, 24) ?: selectedHour
                                    val minute = minuteVal ?: selectedMinute
                                    onTimeChange(hour, minute, isAm)
                                    if (MinutesText.length == 2) {
                                        minuteFocused = false
                                        focusManager.clearFocus()
                                    }
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .width(80.dp)
                            .focusRequester(minuteFocusRequester)
                            .onFocusChanged { focusState ->
                                minuteFocused = focusState.isFocused
                                if (focusState.isFocused) {
                                    hourFocused = false
                                    minuteInput = minuteInput.copy(selection = TextRange(0, minuteInput.text.length))
                                }
                            }
                            .alignByBaseline(),
                        colors =TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = SecondaryColor,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = BlackColor,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 50.sp,
                            color= BlackColor
                        )
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
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
                        color = if (isAm) AccentColor else GrayColor,
                        fontWeight = if (isAm) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 18.sp
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
                        color = if (!isAm) AccentColor else GrayColor,
                        fontWeight = if (!isAm) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 18.sp
                    )
                }
            }
        }

        //Spacer(Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp)
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
                                localHour = if (hour == 0) 12 else hour
                                onTimeChange(localHour, localMinute, isAm)
                                onSelectionChange(true)
                            } else {
                                val angle = (atan2(dy, dx).toDouble() * 180 / Math.PI + 360 + 90) % 360
                                val minute = ((angle / 6).roundToInt()) % 60
                                localMinute = minute
                                onTimeChange(localHour, localMinute, isAm)
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

                    drawCircle(SecondaryColor, center = center, radius = minuteRadius)

                    // Draw hour hand and its end circle
                    val hourAngle = Math.toRadians((selectedHour * 30 - 90).toDouble())
                    val hourX = center.x + cos(hourAngle) * (hourRadius - 0)
                    val hourY = center.y + sin(hourAngle) * (hourRadius - 0)
                    val handColor = MainColor
                    drawLine(
                        handColor,
                        start = center,
                        end = Offset(hourX.toFloat(), hourY.toFloat()),
                        strokeWidth = 8f
                    )
                    drawCircle(
                        color = handColor,
                        radius = 45f,
                        center = Offset(hourX.toFloat(), hourY.toFloat())
                    )

                    // Draw minute hand and its end circle
                    val minuteAngle = Math.toRadians((selectedMinute * 6 - 90).toDouble())
                    val minuteX = center.x + cos(minuteAngle) * (minuteRadius - 158)
                    val minuteY = center.y + sin(minuteAngle) * (minuteRadius - 158)
                    drawLine(
                        handColor,
                        start = center,
                        end = Offset(minuteX.toFloat(), minuteY.toFloat()),
                        strokeWidth = 8f
                    )
                    drawCircle(
                        color = handColor,
                        radius = 30f,
                        center = Offset(minuteX.toFloat(), minuteY.toFloat())
                    )

                    // Draw hour numbers (outer circle)
                    for (h in 1..12) {
                        val angle = Math.toRadians((h * 30 - 90).toDouble())
                        val x = center.x + cos(angle) * (hourRadius - 10)
                        val y = center.y + sin(angle) * (hourRadius - 10)
                        val displayHour = if (!isAm) {
                            val pmHour = h + 12
                            if (pmHour == 24) "00" else pmHour.toString()
                        } else {
                            h.toString()
                        }
                        drawContext.canvas.nativeCanvas.apply {
                            val paint = android.graphics.Paint().apply {
                                color = BlackColor.toArgb()
                                textSize = 60f
                                textAlign = android.graphics.Paint.Align.CENTER
                                typeface = android.graphics.Typeface.DEFAULT_BOLD
                            }
                            drawText(
                                displayHour,
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
                                    color = BlackColor.toArgb()
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
}