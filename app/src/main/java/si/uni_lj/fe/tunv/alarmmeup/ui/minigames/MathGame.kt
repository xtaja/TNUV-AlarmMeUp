package si.uni_lj.fe.tunv.alarmmeup.ui.minigames

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.ui.WinScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ExitButton

@Composable
fun MathGame(onExit: () -> Unit) {
    var isFinished by remember { mutableStateOf(false) }

    if (isFinished) {
        WinScreen(
            currentStreak = 5,
            numOfXP = 100,
            numOfSunCoins = 10,
            onCollect = onExit
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            ExitButton(onExit = onExit)
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Math Game", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { isFinished = true }) {
                    Text("Finish Game")
                }
            }
        }
    }
}