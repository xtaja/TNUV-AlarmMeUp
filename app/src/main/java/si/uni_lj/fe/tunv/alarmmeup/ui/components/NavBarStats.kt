package si.uni_lj.fe.tunv.alarmmeup.ui.components


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AlarmMeUpTheme

@Composable
fun NavBarStats(
    numOfXP: Int = 120,
    numOfSunCoins: Int = 520
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(50)
            )
    ) {
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 85.dp)
                .graphicsLayer(rotationZ = -45f)
                .offset(20.dp, 22.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(25)
                )
        )
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 85.dp)
                .graphicsLayer(rotationZ = 45f)
                .offset(-20.dp, 21.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(25)
                )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // XP row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "XP",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = numOfXP.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Suncoin row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_suncoin),
                    contentDescription = "suncoin",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = numOfSunCoins.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }

}