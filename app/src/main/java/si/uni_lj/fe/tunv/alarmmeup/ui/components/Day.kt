package si.uni_lj.fe.tunv.alarmmeup.ui.components

import java.time.LocalDate

data class Day(
    val date: LocalDate,
    val status: DayStatus
)