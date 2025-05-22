package si.uni_lj.fe.tunv.alarmmeup.ui


import si.uni_lj.fe.tunv.alarmmeup.ui.components.RadialTimePicker

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var hour by remember { mutableStateOf(12) }
    var minute by remember { mutableStateOf(0) }
    var isAm by remember { mutableStateOf(true) }
    var isSelectingHour by remember { mutableStateOf(true) }
    var wakeUpTime by remember { mutableStateOf("Not set") }

    // Helper to format time as a string
    fun formatTime(hour: Int, minute: Int, isAm: Boolean): String {
        val h = hour.toString().padStart(2, '0')
        val m = minute.toString().padStart(2, '0')
        val ampm = if (isAm) "AM" else "PM"
        return "$h:$m $ampm"
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "The current wakeup time is set to: $wakeUpTime",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                RadialTimePicker(
                    selectedHour = hour,
                    selectedMinute = minute,
                    isAm = isAm,
                    isSelectingHour = isSelectingHour,
                    onTimeChange = { newHour, newMinute, newIsAm ->
                        hour = newHour
                        minute = newMinute
                        isAm = newIsAm
                    },
                    onSelectionChange = { selectingHour ->
                        isSelectingHour = selectingHour
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        wakeUpTime = formatTime(hour, minute, isAm)
                    }
                ) {
                    Text("SET")
                }
            }
        }
    }
}