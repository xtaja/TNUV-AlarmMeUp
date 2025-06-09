package si.uni_lj.fe.tunv.alarmmeup.ui.minigames

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo

@Composable
fun TypingGame(
    onExit: () -> Unit,
    sessionRepo: SessionRepo
) {
    var numOfXP=50
    var numOfSunCoins=10
    var isFinished by remember { mutableStateOf(false) }
    LaunchedEffect(isFinished) {
        if (isFinished) {
            sessionRepo.addXPAndCoins(numOfXP, numOfSunCoins)
            sessionRepo.setGameCompletedToday()
        }
    }
    if (isFinished) {
        WinScreen(
            currentStreak = 5,
            numOfXP = numOfXP,
            numOfSunCoins = numOfSunCoins,
            onCollect = onExit
        )
    }else {
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
                Text("Typing Game", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { isFinished = true }) {
                    Text("Finish Game")
                }
            }
        }
    }
}