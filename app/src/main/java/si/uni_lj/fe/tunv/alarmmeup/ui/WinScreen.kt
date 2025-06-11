package si.uni_lj.fe.tunv.alarmmeup.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ChallengeEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AccentColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WinScreen(
    repo: SessionRepo,
    numOfXP: Int,
    numOfSunCoins: Int,
    onCollect: (ChallengeEnum, Boolean, Boolean) -> Unit,
    gameEnum: ChallengeEnum
) {
    val user by repo.currentUser.collectAsState(initial = null)

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.loading))
        }
        return
    }

    var streak = remember { mutableStateOf<Int>(0) }

    LaunchedEffect(user) {
        val newStreak = repo.calculateCurrentStreak(userId = user!!.id) + 1
        streak.value = newStreak
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Congratulations!", fontSize = 38.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(60.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_streak),
                contentDescription = "Streak",
                modifier = Modifier.size(120.dp),
                tint = AccentColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("${streak.value}", fontSize = 90.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            "You’ve kept your streak and earned:",
            fontSize = 28.sp,
            modifier = Modifier.fillMaxWidth(0.8f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = buildAnnotatedString {
                    append("$numOfXP ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("XP")
                    }
                },
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text("$numOfSunCoins", fontSize = 35.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_suncoin),
                contentDescription = "SunCoin",
                modifier = Modifier.size(40.dp),
                tint = AccentColor
            )
        }
        Spacer(modifier = Modifier.height(120.dp))
        Button(
            onClick = { onCollect(gameEnum, true, true)},
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WhiteColor,
                contentColor = BlackColor
            ),
            modifier = Modifier.shadow(10.dp, RoundedCornerShape(16.dp))
        ) {
            Text("CONTINUE", fontSize = 22.sp)
        }
    }
}