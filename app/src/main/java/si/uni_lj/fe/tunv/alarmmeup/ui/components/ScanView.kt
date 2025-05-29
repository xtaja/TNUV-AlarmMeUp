package si.uni_lj.fe.tunv.alarmmeup.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScanView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(300.dp)
            .height(440.dp)
            .clip(RoundedCornerShape(24.dp)) // Set your desired corner radius
            .background(Color.Black), // Optional: background for better effect
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(modifier = Modifier.fillMaxSize())
    }
}