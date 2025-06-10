package si.uni_lj.fe.tunv.alarmmeup.ui.components


import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.GrayColor

@Composable
fun ExitButton(onExit: () -> Unit) {
    Button(
        modifier = Modifier.size(80.dp,80.dp),
        onClick = onExit,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = GrayColor
        )
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "Exit",
            modifier = Modifier.size(40.dp) ,
            tint = GrayColor

        )

    }
}