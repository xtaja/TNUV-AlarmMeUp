package si.uni_lj.fe.tunv.alarmmeup

import android.Manifest
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import si.uni_lj.fe.tunv.alarmmeup.ui.AuthenticationScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.HomeScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.Leader
import si.uni_lj.fe.tunv.alarmmeup.ui.LeaderboardScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.LoadingScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.MorningScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.ProfileScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.ProfileSettingsScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.SettingsScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.StoreScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.StreakScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBar
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBarButton
import si.uni_lj.fe.tunv.alarmmeup.ui.components.NavBarStats
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SettingsBtn
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SettingsEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AppDatabase
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import si.uni_lj.fe.tunv.alarmmeup.ui.data.userPrefs
import si.uni_lj.fe.tunv.alarmmeup.ui.minigames.MathGame
import si.uni_lj.fe.tunv.alarmmeup.ui.minigames.MemoryGame
import si.uni_lj.fe.tunv.alarmmeup.ui.minigames.TypingGame
import si.uni_lj.fe.tunv.alarmmeup.ui.minigames.WordleGame
import si.uni_lj.fe.tunv.alarmmeup.ui.snoozeAlarm
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AlarmMeUpTheme
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor
import java.util.Calendar
import kotlin.math.abs

class MainActivity : ComponentActivity() {

    private lateinit var accountPickerLauncher: ActivityResultLauncher<Intent>



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}

private val dummyLeaders = listOf(
    Leader("Alice Anders",  "@alicea", R.drawable.woman2, xp = 1500, flames = 45, rank = 4),
    Leader("Bob Brown",     "@bobb",   R.drawable.man2, xp = 1380, flames = 38, rank = 5),
    Leader("Cara Chen",     "@cara_c", R.drawable.woman4, xp = 1275, flames = 31, rank = 6),
    Leader("David Drew",    "@daved",  R.drawable.man5, xp = 1160, flames = 27, rank = 7),
    Leader("Eva Eastwood",  "@evae",   R.drawable.woman8, xp = 1045, flames = 22, rank = 8),
    Leader("Frank Foster",  "@frankf", R.drawable.man8, xp =  980, flames = 18, rank = 9),
    Leader("Gina Gonzales", "@ginag",  R.drawable.woman10, xp =  880, flames = 14, rank = 10),
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier, onGoogleClick: () -> Unit) {
    var selectedScreen by remember { mutableStateOf("Auth") }
    val minigameScreens = listOf("MathGame", "TypingGame", "MemoryGame", "WordleGame")
    var profileTabClickCount by remember { mutableStateOf(0) }

    var profilePicture by remember {mutableStateOf(R.drawable.man1)}
    val ctx        = LocalContext.current
    var showMorningScreen by remember { mutableStateOf(false) }
    val mainScope  = rememberCoroutineScope()
    val sessionRepo = remember {
        SessionRepo(
            ctx.userPrefs, AppDatabase.get(ctx).userDao(),
            daoAlarms = AppDatabase.get(ctx).alarmDao(),
            userSoundDao = AppDatabase.get(ctx).userSoundDao(),
            userVibrationDao = AppDatabase.get(ctx).userVibrationDao()
        )
    }

    val user by sessionRepo.currentUser.collectAsState(initial = null)

    LaunchedEffect(user) {
        if (user != null && sessionRepo.user != user) {
            sessionRepo.user = user
        }
    }
    val gameCompletedToday = sessionRepo.wasGameCompletedToday()
    val alarmEntity by user?.let { sessionRepo.getClock(it.id).collectAsState(initial = null) } ?: remember { mutableStateOf(null) }
    var forceHomeScreen by remember { mutableStateOf(false) }
    fun isWithinWakeupWindow(): Boolean {
        val alarm = alarmEntity
        if (alarm == null) return false
        val wakeupTotalMinutes = alarm.hour * 60 + alarm.minute
        val calendar = Calendar.getInstance()
        val currentTotalMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        return abs(currentTotalMinutes - wakeupTotalMinutes) <= 15
    }

    var clockChangeable by remember { mutableStateOf(false) }

    var avatarVersion by remember { mutableIntStateOf(0) }


    // Read and clear the flag from SharedPreferences
    LaunchedEffect(Unit) {
        val prefs = ctx.getSharedPreferences("alarmmeup_prefs", android.content.Context.MODE_PRIVATE)
        if (prefs.getBoolean("showMorningScreen", false)) {
            showMorningScreen = true
            prefs.edit().putBoolean("showMorningScreen", false).apply()
        }
    }

    // Always check the flag on every recomposition (in addition to listener)
    SideEffect {
        val prefs = ctx.getSharedPreferences("alarmmeup_prefs", android.content.Context.MODE_PRIVATE)
        if (prefs.getBoolean("showMorningScreen", false)) {
            showMorningScreen = true
            prefs.edit().putBoolean("showMorningScreen", false).apply()
        }
    }
    
    DisposableEffect(Unit) {
        val prefs = ctx.getSharedPreferences("alarmmeup_prefs", Context.MODE_PRIVATE)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "showMorningScreen" && prefs.getBoolean("showMorningScreen", false)) {
                showMorningScreen = true
                prefs.edit().putBoolean("showMorningScreen", false).apply()
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }


    Column(
        modifier = modifier.fillMaxSize()
        .background(WhiteColor),
    ) {
        // Top Navigation Bar
        if (selectedScreen !in listOf("Auth", "Loading") && selectedScreen !in minigameScreens) {
            NavBar(backgroundAlignment = Alignment.TopStart) {
                NavBarButton(
                    iconResId = R.drawable.ic_streak,
                    contentDescription = "Streak Calendar",
                    onClick = { selectedScreen = "StreakCalendar" },
                    isActive = selectedScreen == "StreakCalendar",
                    label = "24", //change to steak number
                    iconSize = 32.dp
                )
                NavBarStats( //XP and SunCoins
                    numOfXP = user?.xp ?: 0,
                    numOfSunCoins = user?.coins ?: 0
                )
                NavBarButton(
                    iconResId = R.drawable.ic_profile,
                    contentDescription = "Profile",
                    onClick = { selectedScreen = "Profile"; profileTabClickCount++ },
                    isActive = selectedScreen == "Profile",
                    isAtSettings = selectedScreen == "Settings" || selectedScreen == "ProfileSettings",

                )
            }
        }

        // Main Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(WhiteColor),
            contentAlignment = Alignment.Center
        ) {
            when (selectedScreen) {
                "StreakCalendar" -> StreakScreen(R.drawable.snooze, R.drawable.ic_streak, R.drawable.check, R.drawable.close)
                "Profile" -> ProfileScreen(
                    repo = sessionRepo,
                    resetKey = profileTabClickCount,
                    onSettingsClick = { selectedScreen = "ProfileSettings" })
                "Leaderboard" -> LeaderboardScreen(
                    leaders = dummyLeaders)
                "Home" -> {
                    if (showMorningScreen /*&& !gameCompletedToday*/) {
                        MorningScreen(
                            onMathClick = { selectedScreen = "MathGame" },
                            onTypingClick = { selectedScreen = "TypingGame" },
                            onMemoryClick = { selectedScreen = "MemoryGame" },
                            onWordleClick = { selectedScreen = "WordleGame" },
                            onSnoozeClick = { snoozeAlarm(ctx) }
                        )
                    } else {
                        HomeScreen(
                            repo = sessionRepo,
                            isTimeEdible = clockChangeable,
                            R.drawable.ic_suncoin
                        )
                    }
                    LaunchedEffect(selectedScreen) {
                        if (selectedScreen == "Home" && forceHomeScreen) {
                            forceHomeScreen = false
                        }
                    }
                }
                "Store" -> StoreScreen(repo = sessionRepo, onChangeClicked = {
                    clockChangeable = true
                })
                "Settings" -> SettingsScreen(
                    repo = sessionRepo,
                    onCancel = { selectedScreen = "Profile" }
                    )
                "Auth" -> AuthenticationScreen(
                    iconResId = R.drawable.ic_original_logo,
                    onAuthenticated = { it ->
                        selectedScreen = "Loading"
                        mainScope.launch {
                            sessionRepo.login(it)
                        } },
                    onGoogleClick = onGoogleClick,
                    onRequireAvatarSelection = { selectedScreen = "ChooseAvatar" }
                )
                "Loading" -> LoadingScreen(R.layout.onboarding_step1,
                    onFinished = { selectedScreen = "Home" }
                )
                "ChooseAvatar" -> ChooseAvatar(
                    onAvatarSelected = { selectedAvatarResourceId, userId ->
                        mainScope.launch {
                            sessionRepo.updateProfilePicture(
                                userId,
                                ProfilePictureEnum.toEnum(selectedAvatarResourceId)
                            )

                            avatarVersion++
                            selectedScreen = "ProfileSettings"
                        }
                    },
                    listOf(
                        R.drawable.man1,
                        R.drawable.woman1,
                        R.drawable.man2,
                        R.drawable.woman2,
                        R.drawable.man3,
                        R.drawable.woman3,
                        R.drawable.man4,
                        R.drawable.woman4,
                        R.drawable.man5,
                        R.drawable.woman5,
                        R.drawable.man6,
                        R.drawable.woman6,
                        R.drawable.man7,
                        R.drawable.woman7,
                        R.drawable.man8,
                        R.drawable.woman8,
                        R.drawable.man9,
                        R.drawable.woman9,
                        R.drawable.man10,
                        R.drawable.woman10,
                        R.drawable.man11,
                        R.drawable.woman11,
                        R.drawable.man12,
                        R.drawable.woman12,
                        R.drawable.man13,
                        R.drawable.woman13,
                        R.drawable.man14,
                        R.drawable.woman14,
                        R.drawable.man15,
                        R.drawable.woman15
                    ),
                    sessionRepo
                )
                "ProfileSettings" -> ProfileSettingsScreen(
                    repo = sessionRepo,
                    onReturnProfileView = {
                        selectedScreen = "Profile"
                    },
                    onProfilesClick = { selectedScreen = "ChooseAvatar" },
                    goToAuthorizationScreen = { selectedScreen = "Auth" }
                )
                "MathGame" -> MathGame(
                    onExit = { fromWinScreen ->
                        if (fromWinScreen) {
                            forceHomeScreen = true
                            showMorningScreen = false
                            selectedScreen = "Home"
                        } else {
                            forceHomeScreen = false
                            showMorningScreen = true
                            selectedScreen = "Home"
                        }
                    },
                    sessionRepo = sessionRepo
                )
                "TypingGame" -> TypingGame(
                    onExit = { fromWinScreen ->
                        if (fromWinScreen) {
                            forceHomeScreen = true
                            showMorningScreen = false
                            selectedScreen = "Home"
                        } else {
                            forceHomeScreen = false
                            showMorningScreen = true
                            selectedScreen = "Home"
                        }
                    },
                    sessionRepo = sessionRepo
                )
                "MemoryGame" -> MemoryGame(
                    onExit = { fromWinScreen ->
                        if (fromWinScreen) {
                            forceHomeScreen = true
                            showMorningScreen = false
                            selectedScreen = "Home"
                        } else {
                            forceHomeScreen = false
                            showMorningScreen = true
                            selectedScreen = "Home"
                        }
                    },
                    sessionRepo = sessionRepo
                )
                "WordleGame" -> WordleGame(
                    onExit = { fromWinScreen ->
                        if (fromWinScreen) {
                            forceHomeScreen = true
                            showMorningScreen = false
                            selectedScreen = "Home"
                        } else {
                            forceHomeScreen = false
                            showMorningScreen = true
                            selectedScreen = "Home"
                        }
                    },
                    sessionRepo = sessionRepo
                )
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
        if (selectedScreen !in listOf("Auth", "Loading") && selectedScreen !in minigameScreens) {
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
                    onClick = {
                        forceHomeScreen = true
                        selectedScreen = "Home"
                    },
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

