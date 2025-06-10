package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

@Composable
fun SnoozeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text : String = "SNOOZE"
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = WhiteColor,
            contentColor = BlackColor
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(16.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sleep),
            contentDescription = "Snooze",
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            tint = BlackColor
        )
        Text("SNOOZE", fontSize = 22.sp)
    }
}