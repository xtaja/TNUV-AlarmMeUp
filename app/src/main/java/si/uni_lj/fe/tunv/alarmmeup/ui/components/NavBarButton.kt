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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AlarmMeUpTheme

@Composable
fun NavBarButton(
    modifier: Modifier = Modifier,
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    isActive: Boolean = false,
    label: String? = null,
    iconSize: Dp = 40.dp
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.then(Modifier.size(90.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(50)
                )
        ) {
            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(50)
                        )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize),
                    tint = Color.Unspecified
                )
                if (label != null) {
                    Spacer(modifier = Modifier.width(0.dp))
                    Text(label, style = MaterialTheme.typography.bodyMedium, fontSize = 22.sp)
                }
            }
        }
    }
}