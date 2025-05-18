package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ProfilePicture(resourceId: Int, size: Dp = 64.dp) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = "Profile picture",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.LightGray)
    )
}