package si.uni_lj.fe.tunv.alarmmeup.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

@Composable
fun NavBarStats(
    numOfXP: Int = 0,
    numOfSunCoins: Int = 0
) {
    val textColor = BlackColor
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .background(
                color = WhiteColor,
                shape = RoundedCornerShape(50)
            )
    ) {
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 85.dp)
                .graphicsLayer(rotationZ = -45f)
                .offset(20.dp, 22.dp)
                .background(
                    color = WhiteColor,
                    shape = RoundedCornerShape(25)
                )
        )
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 85.dp)
                .graphicsLayer(rotationZ = 45f)
                .offset(-20.dp, 21.dp)
                .background(
                    color = WhiteColor,
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
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = numOfXP.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "XP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
// Suncoin row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = numOfSunCoins.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_suncoin),
                    contentDescription = "suncoin",
                    modifier = Modifier.size(27.dp),
                    tint = textColor
                )

            }
        }

    }

}