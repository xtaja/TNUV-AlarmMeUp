package si.uni_lj.fe.tunv.alarmmeup.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.MainColor


@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    backgroundAlignment: Alignment = Alignment.TopStart,
    content: @Composable RowScope.() -> Unit
) {
    val mainColor = MainColor
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(146.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(73.dp)
                .align(backgroundAlignment)
                .background(
                    color = mainColor,
                )
        )
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}