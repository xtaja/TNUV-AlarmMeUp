package si.uni_lj.fe.tunv.alarmmeup.ui.components

enum class DaysEnum(val bit: Int) {
    SUNDAY    (1 shl 0),
    MONDAY    (1 shl 1),
    TUESDAY   (1 shl 2),
    WEDNESDAY (1 shl 3),
    THURSDAY  (1 shl 4),
    FRIDAY    (1 shl 5),
    SATURDAY  (1 shl 6);

    companion object {
        fun fromSet(days: Set<DaysEnum>): Int =
            days.fold(0) { acc, day -> acc or day.bit }

        fun toSet(mask: Int): Set<DaysEnum> =
            DaysEnum.entries.filter { mask and it.bit != 0 }.toSet()
    }
}
