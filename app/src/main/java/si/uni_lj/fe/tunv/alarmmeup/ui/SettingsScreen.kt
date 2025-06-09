package si.uni_lj.fe.tunv.alarmmeup.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AlarmType
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreCategory
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreItemData

data class SettingCategory(
    val id: Int,
    val name: String,
    val content: @Composable () -> Unit
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {
    var selectedDays by remember { mutableStateOf(setOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")) }
    var volume by remember { mutableStateOf(0.6f) }
    var showIncompleteDaysDialog by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DaysSelector(selectedDays) { selectedDays = it }

        Spacer(Modifier.height(24.dp))

        VolumeSlider(volume) { volume = it }

        AlarmSoundSelector (selectedDays, { show -> showIncompleteDaysDialog = show })

        Spacer(Modifier.height(24.dp))

//        AlarmVibrationSelector(selectedVibration) { selectedVibration = it }

        if (showIncompleteDaysDialog){
            AlertDialog(
                onDismissRequest = { showIncompleteDaysDialog = false },
                title = { Text("Heads up!") },
                text = {
                    Text(
                        "Remember, if you don’t select all days of the week, you could risk losing your streak!"
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showIncompleteDaysDialog = false }) {
                        Text("Got it")
                    }
                }
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

val categories = listOf(
    StoreCategory(
        id = 1,
        name = "Featured Sounds",
        items = listOf(
            StoreItemData(
                id = 1,
                name = "Siren 1",
                cost = 100,
                description = "Loud pitch sounds",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.alarm_sound,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 2,
                name = "Digital 1",
                cost = 100,
                description = "Generic digital alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 3,
                name = "Digital 2",
                cost = 100,
                description = "Generic digital alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 4,
                name = "8-bit",
                cost = 100,
                description = "Pixel style alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.chiptune_alarm_ringtone_song,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 5,
                name = "Old phone",
                cost = 100,
                description = "Old style phone alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.clock_alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 6,
                name = "Digital 3",
                cost = 100,
                description = "Generic digital alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.digital_alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 7,
                name = "Digital 4",
                cost = 100,
                description = "Generic digital alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.digital_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 8,
                name = "Soft waves",
                cost = 100,
                description = "Soft morning pulse alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.dont_need_alarms,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 9,
                name = "Tropical",
                cost = 100,
                description = "Tropical vibe alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.tropical_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 10,
                name = "Doomsday",
                cost = 100,
                description = "Ominous alarm",
                type = AlarmType.SOUND,
                soundResourceId = si.uni_lj.fe.tunv.alarmmeup.R.raw.wake_up_call,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 11,
                name = "Siren 2",
                cost = 100,
                description = "Loud pitch sounds",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.alarm_siren_sound_effect,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            )
        )
    ),
    StoreCategory(
        id = 2,
        name = "Popular Themes",
        items = listOf(
            StoreItemData(
                id = 29,
                name = "Ripple",
                cost = 100,
                description = "Gentle ripple pulses",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 600L, 300L, 400L, 200L),
                vibrationAmplitudes = intArrayOf(0, 120, 0, 100, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 30,
                name = "Quest",
                cost = 100,
                description = "Escalating adventure pulse",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 100L, 50L, 120L, 50L, 140L, 50L, 160L, 50L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 31,
                name = "8-Bit Zap",
                cost = 100,
                description = "Retro arcade buzz",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 500L, 500L, 500L, 500L, 500L, 500L),
                vibrationAmplitudes = intArrayOf(0, 220, 0, 240, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 32,
                name = "Starlink",
                cost = 100,
                description = "Blinking console beeps",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 150L, 75L, 75L, 75L, 150L, 75L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 220, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 33,
                name = "Campfire",
                cost = 100,
                description = "Irregular crackles",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 60L, 30L, 120L, 30L, 90L, 30L, 60L, 30L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 200, 0, 180, 0, 200, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 34,
                name = "Metro",
                cost = 100,
                description = "Rolling city rhythm",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 300L, 100L, 150L, 100L, 300L, 100L),
                vibrationAmplitudes = intArrayOf(0, 220, 0, 200, 0, 230, 0),
                vibrationRepeat = 1
            )
        )
    )
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmSoundSelector(
    selectedDays: Set<String>,
    showIncompleteDaysDialog: (Boolean) -> Unit
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
            text = "Owned alarm sounds",
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
//    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//        items(sounds) { sound ->
//            val isSelected = selected == sound
//            OutlinedButton(
//                onClick = { if (sound != "Shop") onSelect(sound) },
//                border = if (isSelected) BorderStroke(2.dp, Color.Black) else null
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text("▶", color=Color.Black)
//                    Text(sound, style = MaterialTheme.typography.labelSmall, color = Color.Black)
//                }
//            }
//        }
//    }

    var categories by remember { mutableStateOf(categories) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current
    val globalVibrator = remember {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    val actualItemToVibrate: StoreItemData? = remember(categories) {
        categories.asSequence()
            .flatMap { it.items }
            .find { it.playing }
    }

    DisposableEffect(actualItemToVibrate) {
        if (actualItemToVibrate != null &&
            actualItemToVibrate.type == AlarmType.VIBRATION &&
            actualItemToVibrate.vibrationPattern != null &&
            actualItemToVibrate.vibrationAmplitudes != null) {
            try {
                globalVibrator.vibrate(
                    VibrationEffect.createWaveform(
                        actualItemToVibrate.vibrationPattern,
                        actualItemToVibrate.vibrationAmplitudes,
                        actualItemToVibrate.vibrationRepeat
                    )
                )
            } catch (e: Exception) {
                Log.e("CentralEffect", "ERROR vibrating ${actualItemToVibrate.name}: ${e.message}", e)
            }
        } else {
            globalVibrator.cancel()
        }

        onDispose {
            globalVibrator.cancel()
        }
    }

    val onTogglePlay = { clickedItem: StoreItemData ->
        val targetItemIdForPlaying = if (!clickedItem.playing) clickedItem.id else null

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        categories = categories.map { category ->
            category.copy(items = category.items.map { item ->
                item.copy(playing = item.id == targetItemIdForPlaying)
            })
        }


        if (targetItemIdForPlaying != null) {
            if (clickedItem.soundResourceId != null) {
                try {
                    mediaPlayer =
                        MediaPlayer.create(context, clickedItem.soundResourceId)
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.setOnErrorListener { mp, what, extra ->
                        mp.release()
                        if (mediaPlayer == mp) mediaPlayer = null
                        categories = categories.map { cat ->
                            cat.copy(items = cat.items.map { i ->
                                if (i.id == clickedItem.id) i.copy(playing = false)
                                else i
                            })
                        }
                        true
                    }
                    mediaPlayer?.start()
                } catch (e: Exception) {
                    mediaPlayer?.release()
                    mediaPlayer = null
                    categories = categories.map { cat ->
                        cat.copy(items = cat.items.map { i ->
                            if (i.id == clickedItem.id) i.copy(playing = false)
                            else i
                        })
                    }
                }
            }
        }
    }

    val soundCategories = categories.map { cat ->
        cat.copy(
            items = cat.items.filter { it.type == AlarmType.SOUND }
        )
    }.filter { it.items.isNotEmpty() }

    val vibCategories = categories.map { cat ->
        cat.copy(
            items = cat.items.filter { it.type == AlarmType.VIBRATION }
        )
    }.filter { it.items.isNotEmpty() }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            soundCategories.forEach { cat ->
                item {
                    AlarmSettings(
                        category = cat,
                        onTogglePlay = onTogglePlay
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = Color.Gray
                    )
                    Text(
                        text = "Owned vibrations",
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
            }

            vibCategories.forEach { cat ->
                item {
                    AlarmSettings(
                        category = cat,
                        onTogglePlay = onTogglePlay
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    text = "CANCEL",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                    color = Color.LightGray,
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            OutlinedButton(
                onClick = {
                    if (selectedDays.size < 7) {
                        showIncompleteDaysDialog(true)
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Black,
                    disabledContentColor = Color.Black
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

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun AlarmSettings(
    category: StoreCategory,
    onTogglePlay: (StoreItemData) -> Unit,
) {
    Column {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(category.items, key = { it.id }) { storeItem ->
                StoreItemCard(item = storeItem, onTogglePlay = onTogglePlay)
            }
        }
    }
}


//@Composable
//fun AlarmVibrationSelector(
//    selected: String,
//    onSelect: (String) -> Unit
//) {
//    val options = listOf("None", "Basic", "Bee", "Siren")
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        HorizontalDivider(
//            modifier = Modifier
//                .weight(1f)
//                .height(1.dp),
//            color = Color.Gray
//        )
//        Text(
//            text = "Alarm vibration",
//            modifier = Modifier.padding(horizontal = 8.dp),
//            fontSize = 20.sp
//        )
//        HorizontalDivider(
//            modifier = Modifier
//                .weight(1f)
//                .height(1.dp),
//            color = Color.Gray
//        )
//    }
//    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//        items(options) { item ->
//            val isDisabled = item == "Siren"
//            OutlinedButton(
//                onClick = { if (!isDisabled) onSelect(item) },
//                enabled = !isDisabled,
//                border = if (selected == item) BorderStroke(2.dp, Color.Black) else null
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text("▶", color=Color.Black)
//                    Text(item, style = MaterialTheme.typography.labelSmall, color = Color.Black)
//                }
//            }
//        }
//    }
//}
