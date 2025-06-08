package si.uni_lj.fe.tunv.alarmmeup.ui.components

import si.uni_lj.fe.tunv.alarmmeup.R

enum class SoundEnum {
    Siren1,
    Digital1,
    Digital2,
    EightBit,
    OldPhone,
    Digital3,
    Digital4,
    SoftWaves,
    Tropical,
    Doomsday,
    Siren2,
    Drops,
    Beep,
    Golden,
    Sweet,
    Jingles,
    Melodic,
    Lofi,
    Voyage,
    Memphic,
    Anime,
    Police;

    companion object {
        @JvmStatic
        fun toResource(soundEnum: SoundEnum): Int = when (soundEnum) {
            Siren1 -> R.raw.alarm_sound
            Digital1 -> R.raw.alarm
            Digital2 -> R.raw.alarm_clock
            EightBit -> R.raw.chiptune_alarm_ringtone_song
            OldPhone -> R.raw.clock_alarm
            Digital3 -> R.raw.digital_alarm
            Digital4 -> R.raw.digital_alarm_clock
            SoftWaves -> R.raw.dont_need_alarms
            Tropical -> R.raw.tropical_alarm_clock
            Doomsday -> R.raw.wake_up_call
            Siren2 -> R.raw.alarm_siren_sound_effect
            Drops -> R.raw.drippling_drops
            Beep -> R.raw.funny_alarm
            Golden -> R.raw.golden_time
            Sweet -> R.raw.good_morning
            Jingles -> R.raw.jingle_bells_alarm_clock
            Melodic -> R.raw.kirby_alarm_clock
            Lofi -> R.raw.lofi_alarm_clock
            Voyage -> R.raw.majestic_voyage
            Memphic -> R.raw.memphis
            Anime -> R.raw.phone_anime
            Police -> R.raw.siren_police_trap
        }
    }
}