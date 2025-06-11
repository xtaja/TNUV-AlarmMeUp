package si.uni_lj.fe.tunv.alarmmeup

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import si.uni_lj.fe.tunv.alarmmeup.ui.WakeUpScreen

class WakeUpActivity : ComponentActivity() {

    private var alarmId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmId = intent.getIntExtra("ALARM_ID", -1)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        setContent {
            WakeUpScreen(
                onAwakeClick = {
                    setShowMorningScreenFlag()
                    stopAlarmAndFinish()
                },
                onSnoozeClick = { stopAlarmAndFinish() }
            )
        }
    }

    private fun setShowMorningScreenFlag() {
        getSharedPreferences("alarmmeup_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("showMorningScreen", true)
            .apply()
    }

    private fun stopAlarmAndFinish() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (alarmId != -1) {
            notificationManager.cancel(alarmId)
        }

        val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("showMorningScreen", true)
        }

        startActivity(mainActivityIntent)

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarmAndFinish()
    }
}
