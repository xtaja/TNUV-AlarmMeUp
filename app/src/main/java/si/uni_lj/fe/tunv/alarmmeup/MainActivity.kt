package si.uni_lj.fe.tunv.alarmmeup

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import si.uni_lj.fe.tunv.alarmmeup.ui.HomeScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.LeaderboardScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.ProfileScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.SettingsScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.StoreScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.StreakScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.AuthenticationScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.LoadingScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.ProfileSettingsScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBar
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBarButton
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBarStats
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfileSettingsBtn
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SettingsEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SettingsBtn
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AlarmMeUpTheme

class MainActivity : ComponentActivity() {

    private lateinit var accountPickerLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val account = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                Log.d("AccountPicker", "Selected account: $account")
            }
        }

        enableEdgeToEdge()
        setContent {
            val onGoogleClick = {
                val intent = AccountManager.newChooseAccountIntent(
                    null, null, arrayOf("com.google"), null, null, null, null
                )
                accountPickerLauncher.launch(intent)
            }

            AlarmMeUpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onGoogleClick = onGoogleClick
                    )
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier, onGoogleClick: () -> Unit) {
    var selectedScreen by remember { mutableStateOf("Auth") }
    var profileTabClickCount by remember { mutableStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top Navigation Bar
        if (selectedScreen !in listOf("Auth", "Loading")) {
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
                    numOfSunCoins = 520
                )
                NavBarButton(
                    iconResId = R.drawable.ic_profile,
                    contentDescription = "Profile",
                    onClick = { selectedScreen = "Profile"; profileTabClickCount++ },
                    isActive = selectedScreen == "Profile",
                )
            }
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
                "StreakCalendar" -> StreakScreen(R.drawable.snooze, R.drawable.fire, R.drawable.check, R.drawable.close)
                "Profile" -> ProfileScreen(
                    resourceId = R.drawable.placeholder_profile_picture,
                    name = "Amy",
                    surname = "Adams",
                    username = "@amyzams",
                    resetKey = profileTabClickCount,
                    onSettingsClick = { selectedScreen = "ProfileSettings" })
                "Leaderboard" -> LeaderboardScreen()
                "Home" -> HomeScreen()
                "Store" -> StoreScreen()
                "Settings" -> SettingsScreen()
                "Auth" -> AuthenticationScreen(
                    iconResId = R.drawable.ic_original_logo,
                    onAuthenticated = { selectedScreen = "Loading" },
                    onGoogleClick = onGoogleClick
                )
                "Loading" -> LoadingScreen(R.layout.onboarding_step1,
                    onFinished = { selectedScreen = "Home" }
                )
                "ProfileSettings" -> ProfileSettingsScreen(resourceId = R.drawable.placeholder_profile_picture)
                else -> Text("Unknown screen")
            }
            if (selectedScreen == "Profile" || selectedScreen == "ProfileSettings" || selectedScreen == "Settings") {
                SettingsBtn(
                    currentScreen = SettingsEnum.fromString(selectedScreen),
                    onSettingsClick = { selectedScreen = "Settings" }
                )
            }

        }

        // Bottom Navigation Bar
        if (selectedScreen !in listOf("Auth", "Loading")) {
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
}


