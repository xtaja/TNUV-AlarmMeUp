package si.uni_lj.fe.tunv.alarmmeup.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreCategory
import si.uni_lj.fe.tunv.alarmmeup.ui.data.StoreItemData

//val sampleCategories = listOf(
//    StoreCategory(
//        id = 1,
//        name = "Featured Sounds",
//        items = List(8) { StoreItemData(id = it, name = "Sound ${it + 1}") }
//    ),
//    StoreCategory(
//        id = 2,
//        name = "Popular Themes",
//        items = List(6) { StoreItemData(id = it, name = "Theme ${it + 1}") }
//    ),
//    StoreCategory(
//        id = 3,
//        name = "New Alarm Packs",
//        items = List(10) { StoreItemData(id = it, name = "Pack ${it + 1}") }
//    )
//)

val sampleCategories = listOf(
    StoreCategory(
        id = 1,
        name = "Featured Sounds",
        items = listOf(
            StoreItemData(1, "Birds", description = "Peaceful forest ambience"),
            StoreItemData(2, "Rain", description = "Soothing rainfall at night"),
            StoreItemData(3, "Waves", description = "Gentle ocean tides"),
            StoreItemData(4, "Fireplace", description = "Crackling warm fire"),
            StoreItemData(5, "Wind", description = "Soft mountain breeze"),
            StoreItemData(6, "Chimes", description = "Light bell sounds"),
            StoreItemData(7, "Stream", description = "Running water flow"),
            StoreItemData(8, "Thunder", description = "Distant storm rumbles")
        )
    ),
    StoreCategory(
        id = 2,
        name = "Popular Themes",
        items = listOf(
            StoreItemData(9, "Zen Garden", description = "Minimal and focused"),
            StoreItemData(10, "Adventure", description = "Dynamic and epic"),
            StoreItemData(11, "Retro", description = "Old-school pixel sounds"),
            StoreItemData(12, "Sci-Fi", description = "Futuristic beeps"),
            StoreItemData(13, "Forest Camp", description = "Crickets and fire"),
            StoreItemData(14, "City Morning", description = "Urban buzz")
        )
    ),
    StoreCategory(
        id = 3,
        name = "New Alarm Packs",
        items = listOf(
            StoreItemData(15, "Focus Pack", description = "Designed to help you concentrate"),
            StoreItemData(16, "Relax Pack", description = "Wind down your day gently"),
            StoreItemData(17, "Productivity Boost", description = "Kickstart your morning"),
            StoreItemData(18, "Meditation", description = "Breathing rhythm guidance"),
            StoreItemData(19, "Creative Vibes", description = "For artists & thinkers"),
            StoreItemData(20, "Daily Energy", description = "Music to energize"),
            StoreItemData(21, "Explorer Pack", description = "Nature meets melody"),
            StoreItemData(22, "Studio Set", description = "Clean tones for focus"),
            StoreItemData(23, "Momentum", description = "Steady and sharp alarms"),
            StoreItemData(24, "Gentle Wake", description = "Wake up without stress")
        )
    )
)


@Preview(showBackground = true)
@Composable
fun StoreScreenPreview() {
    MaterialTheme {
        StoreScreen()
    }
}

@Composable
fun StoreScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Store",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        sampleCategories.forEach { category ->
            item {
                StoreCategoryRow(category = category)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StoreCategoryRow(category: StoreCategory) {
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
            items(category.items) { storeItem ->
                StoreItemCard(item = storeItem)
            }
        }
    }
}

@Composable
fun StoreItemCard(item: StoreItemData, isDefault: Boolean = false) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(70.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = "Play",
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
        }
    }
}

