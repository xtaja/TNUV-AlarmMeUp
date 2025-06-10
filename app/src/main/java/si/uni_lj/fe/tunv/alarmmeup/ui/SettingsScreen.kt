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
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.components.DaysEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SoundEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AlarmType
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SoundEntity
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreCategory
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreItemData
import si.uni_lj.fe.tunv.alarmmeup.ui.data.VibrationEntity
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.GrayColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.SecondaryColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

data class SettingCategory(
    val id: Int,
    val name: String,
    val content: @Composable () -> Unit
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    repo: SessionRepo,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedDays by remember { mutableStateOf(setOf(DaysEnum.MONDAY, DaysEnum.TUESDAY, DaysEnum.WEDNESDAY,
        DaysEnum.THURSDAY, DaysEnum.FRIDAY, DaysEnum.SATURDAY, DaysEnum.SUNDAY)) }
    var volume by remember { mutableStateOf(0.6f) }
    var selectedSoundId : Int? by remember { mutableStateOf(0) }
    var selectedVibrationId : Int? by remember { mutableStateOf(0) }
    var showIncompleteDaysDialog by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()

    val user by repo.currentUser.collectAsState(initial = null)
    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Images")
        }
        return
    }

    val alarm by repo.getClock(user!!.id).collectAsState(initial = null)

    val ownedSounds by repo.getOwnedSounds().collectAsState(initial = emptyList())
    val ownedVibrations by repo.getOwnedVibrations()
        .collectAsState(initial = emptyList())

    LaunchedEffect(alarm) {
        alarm?.let { currentAlarm ->
            volume = currentAlarm.volume
            selectedSoundId = currentAlarm.soundId
            selectedVibrationId = currentAlarm.vibrationId
            selectedDays = DaysEnum.toSet(currentAlarm.daysMask)
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                DaysSelector(selectedDays) { selectedDays = it }

                Spacer(Modifier.height(24.dp))
            }

            item {
                VolumeSlider(volume) { volume = it }
                Spacer(Modifier.height(24.dp))
            }

            item {
                AlarmSoundSelector(
                    ownedSounds = ownedSounds,
                    ownedVibrations = ownedVibrations,
                    selectedSoundId = selectedSoundId,
                    onSoundSelected = { soundId -> selectedSoundId = soundId },
                    selectedVibrationId = selectedVibrationId,
                    onVibrationSelected = { vibrationId ->
                        selectedVibrationId = vibrationId
                    }
                )

                Spacer(Modifier.height(24.dp))
            }

//        AlarmVibrationSelector(selectedVibration) { selectedVibration = it }

        }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {onCancel()},
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = GrayColor,
                            contentColor = WhiteColor,
                            disabledContainerColor = GrayColor,
                            disabledContentColor = SecondaryColor
                        ),
                        border = BorderStroke(1.dp, BlackColor),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                    ) {
                        Text(
                            text = "CANCEL",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                            color = SecondaryColor,
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    OutlinedButton(
                        onClick = {
                            if (selectedDays.size < 7) {
                                showIncompleteDaysDialog = true
                            } else {
                                scope.launch {
                                    val originalAlarm = alarm ?: return@launch

                                    val newDaysMask = DaysEnum.fromSet(selectedDays)

                                    val updatedAlarm = originalAlarm.copy(
                                        volume = volume,
                                        daysMask = newDaysMask,
                                        soundId = selectedSoundId,
                                        vibrationId = selectedVibrationId
                                    )

                                    repo.setAlarmSettings(updatedAlarm)
                                }
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = GrayColor,
                            contentColor = WhiteColor,
                            disabledContainerColor = BlackColor,
                            disabledContentColor = BlackColor
                        ),
                        border = BorderStroke(1.dp, BlackColor),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                    ) {
                        Text(
                            text = "SAVE",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                            color = SecondaryColor,
                        )
                    }
                }

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
                    TextButton(onClick = {
                        showIncompleteDaysDialog = false
                        scope.launch {
                            val originalAlarm = alarm ?: return@launch

                            val newDaysMask = DaysEnum.fromSet(selectedDays)

                            val updatedAlarm = originalAlarm.copy(
                                volume = volume,
                                daysMask = newDaysMask,
                                soundId = selectedSoundId,
                                vibrationId = selectedVibrationId
                            )

                            repo.setAlarmSettings(updatedAlarm)
                        }
                    }) {
                        Text("Got it")
                    }
                }
            )
        }

    }
}

@Composable
fun DaysSelector(
    selected: Set<DaysEnum>,
    onSelectionChanged: (Set<DaysEnum>) -> Unit
) {
    val days = listOf(DaysEnum.MONDAY, DaysEnum.TUESDAY, DaysEnum.WEDNESDAY,
        DaysEnum.THURSDAY, DaysEnum.FRIDAY, DaysEnum.SATURDAY, DaysEnum.SUNDAY)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = GrayColor
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
            color = GrayColor
        )
    }

    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            val selectedColor = if (selected.contains(day)) GrayColor else WhiteColor
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
                Text(DaysEnum.toString(day))
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
            color = GrayColor
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
            color = GrayColor
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
                thumbColor = BlackColor,
                activeTrackColor = BlackColor,
                inactiveTrackColor = SecondaryColor
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
    ownedSounds: List<SoundEntity>,
    ownedVibrations: List<VibrationEntity>,
    selectedSoundId: Int?,
    onSoundSelected: (Int) -> Unit,
    selectedVibrationId: Int?,
    onVibrationSelected: (Int) -> Unit
) {

    var playingItemId by remember { mutableStateOf<Int?>(null) }
    var playingItemType by remember { mutableStateOf<AlarmType?>(null) }

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val vibrator =
        remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    val actualVibrationToPlay: VibrationEntity? = remember(playingItemId, playingItemType) {
        if (playingItemType == AlarmType.VIBRATION) {
            ownedVibrations.find { it.id == playingItemId }
        } else {
            null
        }
    }

    DisposableEffect(actualVibrationToPlay) {
        if (actualVibrationToPlay != null) {
            try {
                val pattern = actualVibrationToPlay.patternJson
                    .removeSurrounding("[", "]")
                    .split(',')
                    .map { it.trim().toLong() }
                    .toLongArray()
                val amplitudes = actualVibrationToPlay.amplitudeJson
                    .removeSurrounding("[", "]")
                    .split(',')
                    .map { it.trim().toInt() }
                    .toIntArray()
                vibrator.vibrate(
                    VibrationEffect.createWaveform(pattern, amplitudes, 0)
                )
            } catch (e: Exception) {
                Log.e("AlarmSoundSelector", "Error playing vibration", e)
            }
        } else {
            vibrator.cancel()
        }

        onDispose {
            vibrator.cancel()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    val onToggleSoundPlay = { sound: SoundEntity ->
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator.cancel()

        val isNowPlaying = sound.id != playingItemId || playingItemType != AlarmType.SOUND

        playingItemId = if (isNowPlaying) sound.id else null
        playingItemType = if (isNowPlaying) AlarmType.SOUND else null

        if (isNowPlaying) {
            try {
                val resourceId = SoundEnum.toResource(sound.soundIdentifier)
                mediaPlayer = MediaPlayer.create(context, resourceId).apply {
                    isLooping = true
                    start()
                }
            } catch (e: Exception) {
                Log.e("AlarmSoundSelector", "Error playing sound", e)
                playingItemId = null
            }
        }
    }

    val onToggleVibrationPlay = { vibration: VibrationEntity ->
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        vibrator.cancel()

        val isNowPlaying = vibration.id != playingItemId || playingItemType != AlarmType.VIBRATION

        playingItemId = if (isNowPlaying) vibration.id else null
        playingItemType = if (isNowPlaying) AlarmType.VIBRATION else null
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = GrayColor
            )
            Text(
                text = "Owned Sounds",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 20.sp
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = GrayColor
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ownedSounds, key = { "sound-${it.id}" }) { sound ->
                val isSelected = sound.id == selectedSoundId
                val isPlaying =
                    sound.id == playingItemId && playingItemType == AlarmType.SOUND
                SoundCard(
                    sound = sound,
                    isSelected = isSelected,
                    isPlaying = isPlaying,
                    onSelect = { onSoundSelected(sound.id) },
                    onTogglePlay = { onToggleSoundPlay(sound) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = GrayColor
            )
            Text(
                text = "Owned Vibrations",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 20.sp
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = GrayColor
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ownedVibrations, key = { "vibration-${it.id}" }) { vibration ->
                val isSelected = vibration.id == selectedVibrationId
                val isPlaying =
                    vibration.id == playingItemId && playingItemType == AlarmType.VIBRATION
                VibrationCard(
                    vibration = vibration,
                    isSelected = isSelected,
                    isPlaying = isPlaying,
                    onSelect = { onVibrationSelected(vibration.id) },
                    onTogglePlay = {
                        onToggleVibrationPlay(
                            vibration
                        )
                    }
                )
            }
        }
    }
}



@Composable
fun SoundCard(
    sound: SoundEntity,
    isSelected: Boolean,
    isPlaying: Boolean,
    onSelect: () -> Unit,
    onTogglePlay: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
    ) {
        Card(
            modifier = Modifier
                .width(140.dp)
                .clickable(onClick = onSelect),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(SecondaryColor)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable(onClick = onTogglePlay),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            if (isPlaying) R.drawable.ic_pause
                            else R.drawable.ic_play
                        ),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sound.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp
                    )
                }
                if (isPlaying) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(WhiteColor),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SettingsScreenWithLottieOverlay(true)
                    }
                }
            }
        }
    }
}

@Composable
fun VibrationCard(
    vibration: VibrationEntity,
    isSelected: Boolean,
    isPlaying: Boolean,
    onSelect: () -> Unit,
    onTogglePlay: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
    ) {
        Card(
            modifier = Modifier
                .width(140.dp)
                .clickable(onClick = onSelect),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(SecondaryColor)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable(onClick = onTogglePlay),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            if (isPlaying) R.drawable.ic_pause
                            else R.drawable.ic_play
                        ),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = vibration.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp
                    )
                }
                if (isPlaying) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(WhiteColor),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SettingsScreenWithLottieOverlay(true)
                    }
                }
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
//            color = GrayColor
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
//            color = GrayColor
//        )
//    }
//    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//        items(options) { item ->
//            val isDisabled = item == "Siren"
//            OutlinedButton(
//                onClick = { if (!isDisabled) onSelect(item) },
//                enabled = !isDisabled,
//                border = if (selected == item) BorderStroke(2.dp, BlackColor) else null
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text("▶", color=BlackColor)
//                    Text(item, style = MaterialTheme.typography.labelSmall, color = BlackColor)
//                }
//            }
//        }
//    }
//}
