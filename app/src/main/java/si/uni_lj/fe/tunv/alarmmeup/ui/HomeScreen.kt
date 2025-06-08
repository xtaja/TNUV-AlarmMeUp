package si.uni_lj.fe.tunv.alarmmeup.ui


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import si.uni_lj.fe.tunv.alarmmeup.AlarmReceiver
import si.uni_lj.fe.tunv.alarmmeup.ui.components.RadialTimePicker
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import java.util.Calendar
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repo: SessionRepo,
    isTimeEdible: Boolean,
    onEditableChange: (Boolean) -> Unit
) {
    var ctx = LocalContext.current
    var scope = rememberCoroutineScope()
    var hour by remember { mutableStateOf(12) }
    var minute by remember { mutableStateOf(0) }
    var isAm by remember { mutableStateOf(true) }
    var isSelectingHour by remember { mutableStateOf(true) }
    var wakeUpTime by remember { mutableStateOf("Not set") }
    val context = LocalContext.current
    fun formatTime(hour: Int, minute: Int, isAm: Boolean): String {
        val h = hour.toString().padStart(2, '0')
        val m = minute.toString().padStart(2, '0')
        val ampm = if (isAm) "AM" else "PM"
        return "$h:$m $ampm"
    }


    val user by repo.currentUser.collectAsState(initial = null)

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Images")
        }
        return
    }


    val alarmEntity by repo.getClock(user!!.id).collectAsState(initial = null)

    var tempHour    by remember { mutableStateOf(12) }
    var tempMinute  by remember { mutableStateOf(0) }
    var tempIsAm    by remember { mutableStateOf(true) }

    LaunchedEffect(alarmEntity) {
        alarmEntity?.let {
            tempHour   = if (it.hour % 12 == 0) 12 else it.hour % 12
            tempMinute = it.minute
            tempIsAm   = it.hour < 12
            wakeUpTime = formatTime(tempHour, tempMinute, tempIsAm)
        }
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
                    selectedHour        = tempHour,
                    selectedMinute      = tempMinute,
                    isAm                = tempIsAm,
                    isSelectingHour     = isSelectingHour,
                    onTimeChange        = { h, m, am ->
                        tempHour   = h
                        tempMinute = m
                        tempIsAm   = am
                    },
                    onSelectionChange   = { selectingHour ->
                        isSelectingHour = selectingHour
                    }
                )

                Text(
                    text = "The current wakeup time is set to: $wakeUpTime",
                    color = Color.Black,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (isTimeEdible) {
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
                                scope.launch {
                                    repo.updateClock(to24Hour(tempHour, tempIsAm), tempMinute, user!!.id)
                                }
                                scheduleAlarm(ctx, to24Hour(tempHour, tempIsAm), tempMinute)

                                wakeUpTime = formatTime(tempHour, tempMinute, tempIsAm)

                                onEditableChange(false)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),


                            ) {
                            Text(
                                text = "SET",
                                color = Color.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

}
fun scheduleAlarm(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(Calendar.getInstance())) {
            add(Calendar.DATE, 1)
        }
    }

    val canSchedule = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }

    if (canSchedule) {
        try {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    } }
fun to24Hour(hour: Int, isAm: Boolean): Int {
    return when {
        isAm && hour == 12 -> 0
        !isAm && hour != 12 -> hour + 12
        else -> hour
    }
}