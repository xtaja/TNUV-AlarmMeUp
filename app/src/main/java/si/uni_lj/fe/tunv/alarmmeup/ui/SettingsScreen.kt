package si.uni_lj.fe.tunv.alarmmeup.ui

import android.util.Patterns
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.indication
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.unit.sp

data class SettingCategory(
    val id: Int,
    val name: String,
    val content: @Composable () -> Unit
)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    var selectedDays by remember { mutableStateOf(setOf("M", "T", "W", "T", "F")) }
    var volume by remember { mutableStateOf(0.6f) }
    var selectedSound by remember { mutableStateOf("Birds") }
    var selectedVibration by remember { mutableStateOf("Basic") }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DaysSelector(selectedDays) { selectedDays = it }

        Spacer(Modifier.height(24.dp))

        VolumeSlider(volume) { volume = it }

        Spacer(Modifier.height(24.dp))

        AlarmSoundSelector(selectedSound) { selectedSound = it }

        Spacer(Modifier.height(24.dp))

        AlarmVibrationSelector(selectedVibration) { selectedVibration = it }

        Spacer(Modifier.height(32.dp))

        OutlinedButton(
            onClick = {},
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = Color.LightGray
            ),
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
        ) {
            Text(
                text = "SAVE",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                color = Color.LightGray,
            )
        }
    }
}

@Composable
fun DaysSelector(
    selected: Set<String>,
    onSelectionChanged: (Set<String>) -> Unit
) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
        Text(
            text = "Days",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 20.sp
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
    }

    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            val selectedColor = if (selected.contains(day)) Color.Gray else Color.White
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(selectedColor, shape = CircleShape)
                    .clickable(
                        indication = rememberRipple(bounded = true, radius = 20.dp),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        val new = selected.toMutableSet()
                        if (day in new) new.remove(day) else new.add(day)
                        onSelectionChanged(new)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(day)
            }

        }
    }
}

@Composable
fun VolumeSlider(
    value: Float,
    onVolumeChanged: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
        Text(
            text = "Volume",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 20.sp
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.VolumeUp,
            contentDescription = null
        )
        Slider(
            value = value,
            onValueChange = onVolumeChanged,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Black,
                inactiveTrackColor = Color.LightGray
            )
        )
    }
}


@Composable
fun AlarmSoundSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    val sounds = listOf("Birds", "Guitar", "For more,\ngo to Shop")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
        Text(
            text = "Alarm sound",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 20.sp
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
    }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(sounds) { sound ->
            val isSelected = selected == sound
            OutlinedButton(
                onClick = { if (sound != "Shop") onSelect(sound) },
                border = if (isSelected) BorderStroke(2.dp, Color.Black) else null
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("▶", color=Color.Black)
                    Text(sound, style = MaterialTheme.typography.labelSmall, color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun AlarmVibrationSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    val options = listOf("None", "Basic", "Bee", "Siren")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
        Text(
            text = "Alarm vibration",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 20.sp
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = Color.Gray
        )
    }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options) { item ->
            val isDisabled = item == "Siren"
            OutlinedButton(
                onClick = { if (!isDisabled) onSelect(item) },
                enabled = !isDisabled,
                border = if (selected == item) BorderStroke(2.dp, Color.Black) else null
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("▶", color=Color.Black)
                    Text(item, style = MaterialTheme.typography.labelSmall, color = Color.Black)
                }
            }
        }
    }
}
