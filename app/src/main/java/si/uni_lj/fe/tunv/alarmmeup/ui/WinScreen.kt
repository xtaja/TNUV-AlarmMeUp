package si.uni_lj.fe.tunv.alarmmeup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R

@Composable
fun WinScreen(
    currentStreak: Int,
    numOfXP: Int,
    numOfSunCoins: Int,
    onCollect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Congratulations!", fontSize = 28.sp, fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_streak),
                contentDescription = "Streak",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("$currentStreak", fontSize = 80.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(50.dp))
        Text("You’ve kept your streak and earned:", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$numOfXP XP", fontSize = 35.sp, fontWeight = FontWeight.Medium,)
            Spacer(modifier = Modifier.width(30.dp))
            Text("$numOfSunCoins", fontSize = 35.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_suncoin),
                contentDescription = "SunCoin",
                modifier = Modifier.size(35.dp)
            )
        }
        Spacer(modifier = Modifier.height(90.dp))
        Button(onClick = onCollect,
            colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        )) {
            Text("CONTINUE", fontSize = 22.sp)
        }
    }
}