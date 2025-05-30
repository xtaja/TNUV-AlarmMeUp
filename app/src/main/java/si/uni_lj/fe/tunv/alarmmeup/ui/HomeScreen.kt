package si.uni_lj.fe.tunv.alarmmeup.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.ui.components.RadialTimePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var hour by remember { mutableStateOf(12) }
    var minute by remember { mutableStateOf(0) }
    var isAm by remember { mutableStateOf(true) }
    var isSelectingHour by remember { mutableStateOf(true) }
    var wakeUpTime by remember { mutableStateOf("Not set") }

    fun formatTime(hour: Int, minute: Int, isAm: Boolean): String {
        val h = hour.toString().padStart(2, '0')
        val m = minute.toString().padStart(2, '0')
        val ampm = if (isAm) "AM" else "PM"
        return "$h:$m $ampm"
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                Text(
                    text = "The current wakeup time is set to: $wakeUpTime",
                    color = Color.Black,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(60.dp)
                        .width(100.dp)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(55)
                        )
                ) {
                    Button(
                        onClick = {
                            wakeUpTime = formatTime(hour, minute, isAm)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),


                    ) {
                        Text(
                            text = if (wakeUpTime == "Not set") "SET" else "RESET",
                            color = Color.Black,
                            fontSize = 18.sp)
                    }
                }

            }
        }
    }
}