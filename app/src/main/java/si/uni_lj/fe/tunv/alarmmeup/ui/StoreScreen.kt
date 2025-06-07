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
import androidx.annotation.RequiresApi
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.input.key.type
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
                name = "Siren 1",
                cost = 100,
                description = "Loud pitch sounds",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.alarm_sound,
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
                soundResourceId = R.raw.alarm,
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
                soundResourceId = R.raw.alarm_clock,
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
                soundResourceId = R.raw.chiptune_alarm_ringtone_song,
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
                soundResourceId = R.raw.clock_alarm,
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
                soundResourceId = R.raw.digital_alarm,
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
                soundResourceId = R.raw.digital_alarm_clock,
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
                soundResourceId = R.raw.dont_need_alarms,
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
                soundResourceId = R.raw.tropical_alarm_clock,
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
                soundResourceId = R.raw.wake_up_call,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 11,
                name = "Siren 1",
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
                id = 23,
                name = "Wave Wash",
                cost = 100,
                description = "Slow rolling surf",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 150L, 75L, 150L, 75L, 300L, 75L, 150L, 75L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 200, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 24,
                name = "Police",
                cost = 100,
                description = "Urgent siren-like pulses",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 150L, 75L, 150L, 75L, 300L, 75L, 150L, 75L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 200, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 25,
                name = "ZigZag",
                cost = 100,
                description = "Sharp stutter pattern",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 80L, 40L, 120L, 40L, 160L, 40L),
                vibrationAmplitudes = intArrayOf(0, 200, 0, 220, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 26,
                name = "Heartbeat",
                cost = 100,
                description = "Double‑beat rhythm",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 200L, 100L, 200L, 500L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 27,
                name = "Chill Wave",
                cost = 100,
                description = "Gentle ebb and flow",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 700L, 300L),
                vibrationAmplitudes = intArrayOf(0, 100, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 28,
                name = "Thunder",
                cost = 100,
                description = "Rolling rumble bursts",
                type = AlarmType.VIBRATION,
                soundResourceId = null,
                vibrationPattern = longArrayOf(0L, 500L, 100L, 500L, 100L, 500L, 100L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            )
        )
    ),
    StoreCategory(
        id = 3,
        name = "New Alarm Packs",
        items = listOf(
            StoreItemData(
                id = 12,
                name = "Drops",
                cost = 100,
                description = "Dripping drop alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.drippling_drops,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 13,
                name = "Beep",
                cost = 100,
                description = "Funny alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.funny_alarm,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 14,
                name = "Golden",
                cost = 100,
                description = "Kickstart your morning alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.golden_time,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 15,
                name = "Sweet",
                cost = 100,
                description = "Sweet alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.good_morning,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 16,
                name = "Jingles",
                cost = 100,
                description = "Festive alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.jingle_bells_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 17,
                name = "Melodic",
                cost = 100,
                description = "Soft beats alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.kirby_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 18,
                name = "Lo-fi",
                cost = 100,
                description = "Lo-fi alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.lofi_alarm_clock,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 19,
                name = "Voyage",
                cost = 100,
                description = "Majestic alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.majestic_voyage,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 20,
                name = "Memphis",
                cost = 100,
                description = "Upbeat alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.memphis,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 21,
                name = "Anime",
                cost = 100,
                description = "Upbeat anime vibe alarm",
                type = AlarmType.SOUND,
                soundResourceId = R.raw.phone_anime,
                vibrationPattern = null,
                vibrationAmplitudes = null,
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 22,
                name = "Police",
                cost = 100,
                description = "Police upbeat alarm",
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StoreScreen() {

    var categories by remember { mutableStateOf(initialSampleCategories) }
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
        Log.d("CentralEffect", "Current actualItemToVibrate: NAME: ${actualItemToVibrate?.name}, ID: ${actualItemToVibrate?.id}, TYPE: ${actualItemToVibrate?.type}, HAS_PATTERN: ${actualItemToVibrate?.vibrationPattern != null}, HAS_AMPS: ${actualItemToVibrate?.vibrationAmplitudes != null}")

        if (actualItemToVibrate != null &&
            actualItemToVibrate.type == AlarmType.VIBRATION &&
            actualItemToVibrate.vibrationPattern != null &&
            actualItemToVibrate.vibrationAmplitudes != null) {
            Log.d("CentralEffect", "Starting vibration for ${actualItemToVibrate.name}")
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
            Log.d("CentralEffect", "NOT starting vibration for ${actualItemToVibrate?.name}. Reason: itemIsNull=${actualItemToVibrate == null}, notVibrationType=${actualItemToVibrate?.type != AlarmType.VIBRATION}, noPattern=${actualItemToVibrate?.vibrationPattern == null}, noAmps=${actualItemToVibrate?.vibrationAmplitudes == null}")
            globalVibrator.cancel()
        }

        onDispose {
            Log.d("CentralEffect", "onDispose: Cancelling vibration for previous item.")
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
                    onTogglePlay = onTogglePlay
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item { SectionDivider("VIBRATIONS") }

        vibCategories.forEach { cat ->
            item {
                StoreCategoryRow(
                    category = cat,
                    onTogglePlay = onTogglePlay
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
                StoreItemCard(item = storeItem, onTogglePlay = onTogglePlay)
            }
        }
    }
}

@Composable
fun StoreItemCard(
    item: StoreItemData,
    onTogglePlay: (StoreItemData) -> Unit,
) {
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