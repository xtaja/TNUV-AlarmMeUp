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
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.MainColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.SecondaryColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor


@Composable
fun SettingsBtn(
    currentScreen: SettingsEnum,
    onSettingsClick: () -> Unit
) {
    val isActive = currentScreen == SettingsEnum.Settings
    val outerColor = if (isActive) WhiteColor else WhiteColor
    val innerColor = if (isActive) MainColor else SecondaryColor

    IconButton(
        onClick = { onSettingsClick() },
        modifier = Modifier
            .offset(x = 150.dp, y =-300.dp)
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
                painter = painterResource(id = R.drawable.ic_edit_alarm),
                contentDescription = "Settings",
                modifier = Modifier.size(25.dp),
                tint = Color.Black
            )
        }
    }
}