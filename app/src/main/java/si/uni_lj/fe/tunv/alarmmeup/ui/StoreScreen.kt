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
import androidx.compose.ui.platform.LocalContext


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
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 500L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 2,
                name = "Rain",
                cost = 100,
                description = "Soothing rainfall at night",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 100L, 50L, 100L, 50L, 100L, 50L),
                vibrationAmplitudes = intArrayOf(0, 200, 0, 200, 0, 200, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 3,
                name = "Waves",
                cost = 100,
                description = "Gentle ocean tides",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 4,
                name = "Fireplace",
                cost = 100,
                description = "Crackling warm fire",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 5,
                name = "Wind",
                cost = 100,
                description = "Soft mountain breeze",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 6,
                name = "Chimes",
                cost = 100,
                description = "Light bell sounds",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 7,
                name = "Stream",
                cost = 100,
                description = "Running water flow",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 8,
                name = "Thunder",
                cost = 100,
                description = "Distant storm rumbles",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 300L, 100L),
                vibrationAmplitudes = intArrayOf(0, 255, 0),
                vibrationRepeat = 1
            )
        )
    ),
    StoreCategory(
        id = 2,
        name = "Popular Themes",
        items = listOf(
            StoreItemData(
                id = 9,
                name = "Zen Garden",
                cost = 100,
                description = "Minimal and focused",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 250L, 250L),
                vibrationAmplitudes = intArrayOf(0, 150, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 10,
                name = "Adventure",
                cost = 100,
                description = "Dynamic and epic",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 11,
                name = "Retro",
                cost = 100,
                description = "Old-school pixel sounds",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 1000L, 500L),
                vibrationAmplitudes = intArrayOf(0, 180, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 12,
                name = "Sci-Fi",
                cost = 100,
                description = "Futuristic beeps",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 150L, 75L, 150L, 75L, 150L, 75L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 13,
                name = "Forest Camp",
                cost = 100,
                description = "Crickets and fire",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 14,
                name = "City Morning",
                cost = 100,
                description = "Urban buzz",
                soundResourceId = R.raw.alarm_sound,
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
                id = 15,
                name = "Focus Pack",
                cost = 100,
                description = "Designed to help you concentrate",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L, 80L, 40L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 16,
                name = "Relax Pack",
                cost = 100,
                description = "Wind down your day gently",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 600L, 400L),
                vibrationAmplitudes = intArrayOf(0, 100, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 17,
                name = "Productivity",
                cost = 100,
                description = "Kickstart your morning",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 150L, 50L, 150L, 50L, 150L, 50L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 18,
                name = "Meditation",
                cost = 100,
                description = "Breathing rhythm guidance",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 800L, 800L),
                vibrationAmplitudes = intArrayOf(0, 80, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 19,
                name = "Creative Vibes",
                cost = 100,
                description = "For artists & thinkers",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 120L, 60L, 120L, 60L, 120L, 60L, 120L, 60L),
                vibrationAmplitudes = intArrayOf(0, 200, 0, 200, 0, 200, 0, 200, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 20,
                name = "Daily Energy",
                cost = 100,
                description = "Music to energize",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 50L, 200L, 50L, 200L, 50L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 21,
                name = "Explorer Pack",
                cost = 100,
                description = "Nature meets melody",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 200L, 100L, 200L),
                vibrationAmplitudes = intArrayOf(0, 255, 0, 255),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 22,
                name = "Studio Set",
                cost = 100,
                description = "Clean tones for focus",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 300L, 100L, 300L, 100L, 300L, 100L),
                vibrationAmplitudes = intArrayOf(0, 180, 0, 180, 0, 180, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 23,
                name = "Momentum",
                cost = 100,
                description = "Steady and sharp alarms",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 600L, 300L),
                vibrationAmplitudes = intArrayOf(0, 255, 0),
                vibrationRepeat = 1
            ),
            StoreItemData(
                id = 24,
                name = "Gentle Wake",
                cost = 100,
                description = "Wake up without stress",
                soundResourceId = R.raw.alarm_sound,
                vibrationPattern = longArrayOf(0, 400L, 400L),
                vibrationAmplitudes = intArrayOf(0, 120, 0),
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

        categories.forEach { category ->
            item {
                StoreCategoryRow(
                    category = category,
                    onTogglePlay = onTogglePlay,
                    globalVibrator
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