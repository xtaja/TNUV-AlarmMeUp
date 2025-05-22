package si.uni_lj.fe.tunv.alarmmeup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import si.uni_lj.fe.tunv.alarmmeup.ui.components.Screen
import si.uni_lj.fe.tunv.alarmmeup.ui.HomeScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.LeaderboardScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.ProfileScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.SettingsScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.StoreScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.StreakScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBar
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBarButton
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBarStats
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SettingsBtn
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AlarmMeUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmMeUpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedScreen by remember { mutableStateOf("Home") }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top Navigation Bar
        NavBar(backgroundAlignment = Alignment.TopStart) {
            NavBarButton(
                iconResId = R.drawable.ic_streak,
                contentDescription = "Streak Calendar",
                onClick = { selectedScreen = "StreakCalendar" },
                isActive = selectedScreen == "StreakCalendar",
                label = "24", //change to number
                iconSize = 32.dp
            )
            NavBarStats( //XP and SunCoins
                numOfXP = 120,
                numOfSunCoins= 520
            )
            NavBarButton(
                iconResId = R.drawable.ic_profile,
                contentDescription = "Profile",
                onClick = { selectedScreen = "Profile" },
                isActive = selectedScreen == "Profile"

            )
        }

        // Main Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            when (selectedScreen) {
                "StreakCalendar" -> StreakScreen()
                "Profile" -> ProfileScreen(
                    resourceId = R.drawable.placeholder_profile_picture,
                    name = "Amy",
                    surname = "Adams",
                    username = "@amyzams")
                "Leaderboard" -> LeaderboardScreen()
                "Home" -> HomeScreen()
                "Store" -> StoreScreen()
                "Settings" -> SettingsScreen()
                else -> Text("Unknown screen")
            }
            if (selectedScreen == "Profile" || selectedScreen == "Settings") {
                SettingsBtn(
                    currentScreen = if (selectedScreen == "Settings") Screen.Settings else Screen.Profile,
                    onSettingsClick = { selectedScreen = "Settings" }
                )
            }

        }

        // Bottom Navigation Bar
        NavBar(backgroundAlignment = Alignment.BottomStart) {
            NavBarButton(
                iconResId = R.drawable.ic_leaderboard,
                contentDescription = "Leaderboard",
                onClick = { selectedScreen = "Leaderboard" },
                isActive = selectedScreen == "Leaderboard"

            )
            NavBarButton(
                iconResId = R.drawable.ic_home,
                contentDescription = "Home",
                onClick = { selectedScreen = "Home" },
                isActive = selectedScreen == "Home"

            )
            NavBarButton(
                iconResId = R.drawable.ic_store,
                contentDescription = "Store",
                onClick = { selectedScreen = "Store" },
                isActive = selectedScreen == "Store"

            )
        }
    }
}


