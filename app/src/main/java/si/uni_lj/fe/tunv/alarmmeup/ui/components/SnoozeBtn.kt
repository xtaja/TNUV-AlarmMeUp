package si.uni_lj.fe.tunv.alarmmeup.ui.components

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.scheduleAlarm
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor
import java.util.Calendar

@Composable
fun SnoozeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "SNOOZE",
    snoozed: Boolean = false
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = WhiteColor,
            contentColor = BlackColor
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sleep),
            contentDescription = "Snooze",
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            tint = BlackColor
        )
        Text(
            if (snoozed) "SNOOZED" else text,
            fontSize = 22.sp
        )
    }
}

fun snoozeAlarm(context: Context) {
    val calendar = Calendar.getInstance().apply {
        add(Calendar.MINUTE, 1)
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    scheduleAlarm(context, hour, minute)
}

