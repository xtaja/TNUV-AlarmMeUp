package si.uni_lj.fe.tunv.alarmmeup.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.components.MiniGameCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MorningScreen(
    onMathClick: () -> Unit,
    onTypingClick: () -> Unit,
    onMemoryClick: () -> Unit,
    onWordleClick: () -> Unit,
    onSnoozeClick: () -> Unit
) {
    val currentTime = remember {
        SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Time to wake up!", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(25.dp))
        Text(currentTime, fontSize = 70.sp)
        Spacer(modifier = Modifier.height(45.dp))
        Text("Choose today's challenge:", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MiniGameCard(
                iconRes = R.drawable.ic_math,
                name = "Math",
                xp = 50,
                onClick = onMathClick,
                modifier = Modifier.weight(1f)
            )
            MiniGameCard(
                iconRes = R.drawable.ic_typing,
                name = "Typing",
                xp = 40,
                onClick = onTypingClick,
                modifier = Modifier.weight(1f)
            )
            MiniGameCard(
                iconRes = R.drawable.ic_memory,
                name = "Memory",
                xp = 30,
                onClick = onMemoryClick,
                modifier = Modifier.weight(1f)
            )
            MiniGameCard(
                iconRes = R.drawable.ic_wordle,
                name = "Wordle",
                xp = 60,
                onClick = onWordleClick,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Text("...or snooze and loose your streak...", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onSnoozeClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )) {
            Image(
                painter = painterResource(id = R.drawable.ic_snooze),
                contentDescription = "Snooze",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text("SNOOZE", fontSize = 22.sp)
        }
    }
}
