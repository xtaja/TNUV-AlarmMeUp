package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import si.uni_lj.fe.tunv.alarmmeup.R

@Composable
fun ProfileSettingsBtn(
    onSettingsClick: () -> Unit
) {
    val outerColor = Color.White
    val innerColor = Color(0xF2F2F2F2)

    IconButton(
        onClick = { onSettingsClick() },
        modifier = Modifier
            .offset(x = 0.dp, y = -30.dp)
            .size(60.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = outerColor,
                    shape = RoundedCornerShape(50)
                )
        ) {

            Box(

                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = innerColor,
                        shape = RoundedCornerShape(50)
                    )
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings",
                modifier = Modifier.size(25.dp),
                tint = Color.Black
            )
        }
    }
}