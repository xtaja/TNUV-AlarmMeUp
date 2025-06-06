package si.uni_lj.fe.tunv.alarmmeup.ui

import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreCategory
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreItemData
import android.content.Context
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.platform.LocalContext
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AlarmType


@Composable
fun SettingsScreenWithLottieOverlay(showLottie: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (showLottie) {
            AndroidView(
                factory = { context ->
                    LayoutInflater.from(context)
                        .inflate(R.layout.alarm_soundwaves, null)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f))
            )
        }
    }
}

val initialSampleCategories = listOf(
    StoreCategory(
        id = 1,
        name = "Featured Sounds",
        items = listOf(
            StoreItemData(
                id = 1,
                name = "Birds",
                cost = 100,
                description = "Peaceful forest ambience",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 2,
                name = "Rain",
                cost = 100,
                description = "Soothing rainfall at night",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 3,
                name = "Waves",
                cost = 100,
                description = "Gentle ocean tides",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 4,
                name = "Fireplace",
                cost = 100,
                description = "Crackling warm fire",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.chiptune_alarm_ringtone_song,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 5,
                name = "Wind",
                cost = 100,
                description = "Soft mountain breeze",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.clock_alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 6,
                name = "Chimes",
                cost = 100,
                description = "Light bell sounds",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.digital_alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 7,
                name = "Stream",
                cost = 100,
                description = "Running water flow",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.digital_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 8,
                name = "Thunder",
                cost = 100,
                description = "Distant storm rumbles",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.dont_need_alarms,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 9,
                name = "Gentle Wake",
                cost = 100,
                description = "Wake up without stress",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.tropical_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 10,
                name = "Gentle Wake",
                cost = 100,
                description = "Wake up without stress",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.wake_up_call,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 11,
                name = "Gentle Wake",
                cost = 100,
                description = "Wake up without stress",
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
                id = 11,
                name = "Zen Garden",
                cost = 100,
                description = "Minimal and focused",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 250L, 250L),
                vibrationAmplitudes = intArrayOf(0, 150, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 12,
                name = "Adventure",
                cost = 100,
                description = "Dynamic and epic",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 13,
                name = "Retro",
                cost = 100,
                description = "Old-school pixel sounds",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 1000L, 500L),
                vibrationAmplitudes = intArrayOf(0, 180, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 14,
                name = "Sci-Fi",
                cost = 100,
                description = "Futuristic beeps",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 150L, 75L, 150L, 75L, 150L, 75L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 15,
                name = "Forest Camp",
                cost = 100,
                description = "Crickets and fire",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 16,
                name = "City Morning",
                cost = 100,
                description = "Urban buzz",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 400L, 200L),
                vibrationAmplitudes = intArrayOf(0, 220, 0),
                vibrationRepeat = 1
            )
        )
    ),
    StoreCategory(
        id = 3,
        name = "New Alarm Packs",
        items = listOf(
            StoreItemData(
                id = 17,
                name = "Focus Pack",
                cost = 100,
                description = "Designed to help you concentrate",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.drippling_drops,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 18,
                name = "Relax Pack",
                cost = 100,
                description = "Wind down your day gently",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.funny_alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 19,
                name = "Productivity",
                cost = 100,
                description = "Kickstart your morning",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.golden_time,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 20,
                name = "Meditation",
                cost = 100,
                description = "Breathing rhythm guidance",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.good_morning,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 21,
                name = "Creative Vibes",
                cost = 100,
                description = "For artists & thinkers",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.jingle_bells_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 22,
                name = "Daily Energy",
                cost = 100,
                description = "Music to energize",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.kirby_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 23,
                name = "Explorer Pack",
                cost = 100,
                description = "Nature meets melody",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.lofi_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 24,
                name = "Studio Set",
                cost = 100,
                description = "Clean tones for focus",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.majestic_voyage,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 25,
                name = "Momentum",
                cost = 100,
                description = "Steady and sharp alarms",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.memphis,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 26,
                name = "Gentle Wake",
                cost = 100,
                description = "Wake up without stress",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.phone_anime,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 27,
                name = "Gentle Wake",
                cost = 100,
                description = "Wake up without stress",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.siren_police_trap,
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
                id = 28,
                name = "Zen Garden",
                cost = 100,
                description = "Minimal and focused",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 250L, 250L),
                vibrationAmplitudes = intArrayOf(0, 150, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 29,
                name = "Adventure",
                cost = 100,
                description = "Dynamic and epic",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 30,
                name = "Retro",
                cost = 100,
                description = "Old-school pixel sounds",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 1000L, 500L),
                vibrationAmplitudes = intArrayOf(0, 180, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 31,
                name = "Sci-Fi",
                cost = 100,
                description = "Futuristic beeps",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 150L, 75L, 150L, 75L, 150L, 75L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 32,
                name = "Forest Camp",
                cost = 100,
                description = "Crickets and fire",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 33,
                name = "City Morning",
                cost = 100,
                description = "Urban buzz",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0, 400L, 200L),
                vibrationAmplitudes = intArrayOf(0, 220, 0),
                vibrationRepeat = 1
            )
        )
    )
)

@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    MaterialTheme { StoreScreen() }
}

@Composable
fun SectionDivider(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color.Gray
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}

@Composable
fun StoreScreen() {

    var categories by remember { mutableStateOf(initialSampleCategories) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current
    val globalVibrator = remember {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }


    val onTogglePlay = { clickedItem: StoreItemData ->
        val wasPlayingThisItem = clickedItem.playing
        val targetItemIdForPlaying =
            if (!wasPlayingThisItem) clickedItem.id else null

        Log.d("StoreScreen", "Disposing MediaPlayer")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        globalVibrator.cancel()

        mediaPlayer = null

        categories = categories.map { category ->
            category.copy(items = category.items.map { item ->
                item.copy(playing = item.id == targetItemIdForPlaying)
            })
        }

        if (targetItemIdForPlaying != null && clickedItem.id == targetItemIdForPlaying) {
            if (clickedItem.soundResourceId != null) {
                try {
                    Log.d(
                        "StoreScreen",
                        "Attempting to play sound ID: ${clickedItem.soundResourceId} for item: ${clickedItem.name}"
                    )
                    mediaPlayer =
                        MediaPlayer.create(context, clickedItem.soundResourceId)
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.setOnErrorListener { mp, what, extra ->
                        Log.e(
                            "StoreScreen",
                            "MediaPlayer Error: what: $what, extra: $extra for item: ${clickedItem.name}"
                        )
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
                    Log.d(
                        "StoreScreen",
                        "MediaPlayer started for item: ${clickedItem.name}"
                    )
                } catch (e: Exception) {
                    Log.e(
                        "StoreScreen",
                        "Error creating or starting MediaPlayer for item: ${clickedItem.name}",
                        e
                    )
                    mediaPlayer?.release()
                    mediaPlayer = null
                    categories = categories.map { cat ->
                        cat.copy(items = cat.items.map { i ->
                            if (i.id == clickedItem.id) i.copy(playing = false)
                            else i
                        })
                    }
                }
            } else {
                Log.w(
                    "StoreScreen",
                    "No soundResourceId for item: ${clickedItem.name}"
                )
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Store",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }

        item { SectionDivider("SOUNDS") }

        soundCategories.forEach { cat ->
            item {
                StoreCategoryRow(
                    category = cat,
                    onTogglePlay = onTogglePlay,
                    globalVibrator = globalVibrator
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item { SectionDivider("VIBRATIONS") }

        vibCategories.forEach { cat ->
            item {
                StoreCategoryRow(
                    category = cat,
                    onTogglePlay = onTogglePlay,
                    globalVibrator = globalVibrator
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StoreCategoryRow(
    category: StoreCategory,
    onTogglePlay: (StoreItemData) -> Unit,
    globalVibrator: Vibrator
) {
    Column {
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(category.items, key = { it.id }) { storeItem ->
                StoreItemCard(item = storeItem, onTogglePlay = onTogglePlay, globalVibrator)
            }
        }
    }
}

@Composable
fun StoreItemCard(
    item: StoreItemData,
    onTogglePlay: (StoreItemData) -> Unit,
    globalVibrator: Vibrator
) {
    val context: Context = LocalContext.current

    DisposableEffect(key1 = item.playing) {
        if (item.playing) {
            val pattern = item.vibrationPattern
            val amps    = item.vibrationAmplitudes
            if (pattern != null && amps != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    globalVibrator.vibrate(
                        VibrationEffect.createWaveform(
                            pattern,
                            amps,
                            item.vibrationRepeat
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    globalVibrator.vibrate(pattern, item.vibrationRepeat)
                }
            }
        } else {
            Log.e("StoreItemCard", "Vibrator canceled for item: ${item.name}")
            globalVibrator.cancel()
        }

        onDispose {
            Log.e("StoreItemCard", "Vibrator canceled for item: ${item.name}")
            globalVibrator.cancel()
        }
    }

    Card(
        modifier = Modifier
            .width(140.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onTogglePlay(item) }
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            if (item.playing) R.drawable.ic_pause
                            else R.drawable.ic_play
                        ),
                        contentDescription = if (item.playing) "Pause" else "Play",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${item.cost}",
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_suncoin),
                        contentDescription = "Current Streak",
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp),
                        tint = Color(0xFFFFA500)
                    )
                }
                if (item.playing) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.White),
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