package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    resourceId: Int,
    size: Dp = 64.dp,
    ) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = "Profile picture",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.LightGray)
    )
}