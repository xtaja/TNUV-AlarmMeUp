package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScanQRCodeToggle(
    isScan: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val toggleWidth = 160.dp
    val toggleHeight = 36.dp
    val thumbWidth = toggleWidth / 1.8f
    val offsetX by animateDpAsState(targetValue = if (isScan) thumbWidth else 0.dp, label = "")

    Box(
        modifier = modifier
            .width(toggleWidth)
            .height(toggleHeight)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.LightGray)
            .clickable { onToggle(!isScan) }
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(thumbWidth)
                .height(toggleHeight)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xF2F2F2F2))
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My code",
                    color = if (!isScan) Color.Black else Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Scan",
                    color = if (isScan) Color.Black else Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}