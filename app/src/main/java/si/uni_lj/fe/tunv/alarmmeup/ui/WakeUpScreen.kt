package si.uni_lj.fe.tunv.alarmmeup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SnoozeButton

@Composable
fun WakeUpScreen(
    onAwakeClick: () -> Unit,
    onSnoozeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("GoodMorning!", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_alarmclock),
            contentDescription = "Alarm Clock",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAwakeClick,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("I'm Awake", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        SnoozeButton(
            onClick = onSnoozeClick,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}