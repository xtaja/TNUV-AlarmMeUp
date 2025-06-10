package si.uni_lj.fe.tunv.alarmmeup.ui


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import si.uni_lj.fe.tunv.alarmmeup.AlarmReceiver
import si.uni_lj.fe.tunv.alarmmeup.ui.components.RadialTimePicker
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AccentColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repo: SessionRepo,
    isTimeEdible: Boolean,
    iconResId: Int
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
    var showAlert by remember { mutableStateOf(false) }
    var showPurchase by remember { mutableStateOf(false) }
    var changeButton by remember { mutableStateOf(false) }

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

    val scrollState = rememberScrollState()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Alarm is set to: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(wakeUpTime)
                        }
                    },
                    color = BlackColor,
                    fontSize = 25.sp,
                )

                Spacer(modifier = Modifier.height(20.dp))
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
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (!changeButton) {
                            showAlert = true
                        } else {
                            showPurchase = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhiteColor,
                        contentColor = BlackColor
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.shadow(10.dp, RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = if (!changeButton) "SET" else "RESET for 20",
                        fontSize = 22.sp,
                        color = BlackColor
                    )
                    if (changeButton) {
                        Icon(
                            painter = painterResource(iconResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                                .padding(end = 8.dp),
                            tint = AccentColor
                        )
                    }
                }

                if (showPurchase){
                    AlertDialog(
                        onDismissRequest = { showAlert = false },
                        title = { Text("Confirm Action")},
                        text = { Text("Are you sure you want to change the time?")},
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showPurchase = false
                                    scope.launch {
                                        repo.updateClock(
                                            to24Hour(tempHour, tempIsAm),
                                            tempMinute,
                                            user!!.id
                                        )
                                    }
                                    scheduleAlarm(ctx, to24Hour(tempHour, tempIsAm), tempMinute)

                                    wakeUpTime = formatTime(tempHour, tempMinute, tempIsAm)
                                })
                            {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showPurchase = false })
                            {
                                Text("No")
                            }
                        }
                    )
                }

                if (showAlert){
                    AlertDialog(
                        onDismissRequest = { showAlert = false },
                        title = { Text("Confirm Action")},
                        text = { Text("Are you sure you want to change the time?")},
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showAlert = false
                                    changeButton = true
                                    scope.launch {
                                        repo.updateClock(
                                            to24Hour(tempHour, tempIsAm),
                                            tempMinute,
                                            user!!.id
                                        )
                                    }
                                    scheduleAlarm(ctx, to24Hour(tempHour, tempIsAm), tempMinute)

                                    wakeUpTime = formatTime(tempHour, tempMinute, tempIsAm)
                                })
                            {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showAlert = false })
                            {
                                Text("No")
                            }
                        }
                    )
                }

            }
        }
    }

}
fun scheduleAlarm(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            // Prompt user to grant permission or show a message
            // You can direct them to Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            return
        }
    }

    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        if (before(Calendar.getInstance())) {
            add(Calendar.DATE, 1)
        }
    }

    try {
        val alarmClockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    } catch (e: SecurityException) {
        // Handle the exception (e.g., show a message to the user)
    }
}
fun to24Hour(hour: Int, isAm: Boolean): Int {
    return when {
        isAm && hour == 12 -> 0
        !isAm && hour != 12 -> hour + 12
        else -> hour
    }
}

fun snoozeAlarm(context: Context) {
    val calendar = Calendar.getInstance().apply {
        add(Calendar.MINUTE, 5)
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    scheduleAlarm(context, hour, minute)
}