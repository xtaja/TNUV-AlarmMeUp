package si.uni_lj.fe.tunv.alarmmeup.ui.components

sealed class DayStatus {
    data object COMPLETED : DayStatus()
    data object MISSED : DayStatus()
    data object None : DayStatus()
}