package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

data class DateRangeHighlight(
    val start: LocalDate,
    val end: LocalDate,
    val color: Color,
    val borderColor: Color = color.copy(alpha = 0.8f),
    val icon: ImageVector? = null, // Optional icon for the range
    val iconColor: Color = Color.Black // Default icon color
)