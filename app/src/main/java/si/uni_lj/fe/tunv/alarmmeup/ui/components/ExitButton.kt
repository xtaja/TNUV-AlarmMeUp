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

@Composable
fun ExitButton(onExit: () -> Unit) {
    Button(
        modifier = Modifier.size(48.dp,48.dp),
        onClick = onExit,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ){
        Icon(
            painter = painterResource(id = R.drawable.close),
            contentDescription = "Exit",
            modifier = Modifier.size(24.dp)
        )

    }
}