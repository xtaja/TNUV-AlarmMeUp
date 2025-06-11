package si.uni_lj.fe.tunv.alarmmeup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SoundEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AppDatabase
import si.uni_lj.fe.tunv.alarmmeup.ui.data.Keys
import si.uni_lj.fe.tunv.alarmmeup.ui.data.userPrefs
import androidx.core.net.toUri


class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        val pendingResult: PendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = context.userPrefs.data
                    .map { preferences ->
                        preferences[Keys.CURRENT_ID]
                    }
                    .first()

                if (userId == null) {
                    return@launch
                }

                val db = AppDatabase.get(context)

                val alarm = db.alarmDao().selectById(userId).first()
                val soundEntity = alarm!!.soundId!!.let { db.soundDao().getById(it) }
                val vibrationEntity = alarm.vibrationId!!.let { db.vibrationDao().getById(it) }

                val soundUri: Uri = soundEntity?.let {
                    val resourceId = SoundEnum.toResource(it.soundIdentifier)
                    val uriString = "android.resource://${context.packageName}/$resourceId"
                    uriString.toUri()
                } ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

                val vibrationPattern: LongArray
                val vibrationAmplitudes: IntArray
                if (vibrationEntity != null) {
                    vibrationPattern = vibrationEntity.patternJson
                        .removeSurrounding("[", "]")
                        .split(',')
                        .map { it.trim().toLong() }
                        .toLongArray()
                    vibrationAmplitudes = vibrationEntity.amplitudeJson
                        .removeSurrounding("[", "]")
                        .split(',')
                        .map { it.trim().toInt() }
                        .toIntArray()
                } else {
                    vibrationPattern = longArrayOf(0, 500, 500, 500)
                    vibrationAmplitudes = intArrayOf(0, 255, 0, 255)
                }

                val channelId = "alarm_channel_${alarm.id}"
                val channelName = "Alarm ($channelId)"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, channelName, importance)

                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                channel.setSound(soundUri, audioAttributes)


                val vibrationEffect = VibrationEffect.createWaveform(vibrationPattern, vibrationAmplitudes, -1)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    channel.vibrationEffect = vibrationEffect
                } else {
                    channel.vibrationPattern = vibrationPattern
                }

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

                val fullScreenIntent = Intent(context, WakeUpActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("ALARM_ID", alarm.id)
                }
                val fullScreenPendingIntent = PendingIntent.getActivity(
                    context, alarm.id, fullScreenIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )


                val notification = NotificationCompat.Builder(context, "alarm_channel_${alarm.id}")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Alarm")
                    .setContentText("Wake up! Your alarm is ringing.")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setContentIntent(fullScreenPendingIntent)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(alarm.id, notification)

            } finally {
                pendingResult.finish()
            }
        }
    }
}

