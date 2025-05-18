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

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            RadialTimePicker(
                selectedHour = hour,
                selectedMinute = minute,
                isAm = isAm,
                isSelectingHour = isSelectingHour,
                //onSelectionChange = { isSelectingHour = it },
                onTimeChange = { newHour, newMinute, newIsAm ->
                    Log.d("TimePicker", "Hour: $newHour, Minute: $newMinute, isAm: $newIsAm")
                    hour = newHour
                    minute = newMinute
                    isAm = newIsAm
                }, onSelectionChange = { selectingHour ->
                    Log.d("TimePicker", "isSelectingHour: $selectingHour")
                    isSelectingHour = selectingHour
                }
            )
        }
    }
}