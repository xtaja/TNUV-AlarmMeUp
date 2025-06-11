package si.uni_lj.fe.tunv.alarmmeup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SnoozeButton
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.MainColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

@Composable
fun WakeUpScreen(
    onAwakeClick: () -> Unit,
    onSnoozeClick: () -> Unit,
    snoozed: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("GoodMorning!", fontSize = 32.sp, color = BlackColor)
        Spacer(modifier = Modifier.height(22.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_alarmclock),
            contentDescription = "Alarm Clock",
            modifier = Modifier.fillMaxSize(0.7f),
            tint = BlackColor
        )
        Spacer(modifier = Modifier.height(22.dp))
        Button(
            onClick = onAwakeClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainColor,
                contentColor = WhiteColor
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .shadow(10.dp, RoundedCornerShape(16.dp))
                .fillMaxWidth(0.5f)

        ) {
            Text("I'm Awake", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        SnoozeButton(
            onClick = onSnoozeClick,
            modifier = Modifier.fillMaxWidth(0.5f),
            snoozed = snoozed
        )
    }
}
