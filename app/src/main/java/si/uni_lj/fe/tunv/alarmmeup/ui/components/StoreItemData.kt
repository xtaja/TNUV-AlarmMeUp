package si.uni_lj.fe.tunv.alarmmeup.ui.data
import si.uni_lj.fe.tunv.alarmmeup.R

data class StoreItemData(
    val id: Int,
    val name: String,
    val cost: Int,
    var playing: Boolean = false,
    val imageUrl: String? = null,
    val description: String = "",
    val type: AlarmType,
    val soundResourceId: Int? = null,
    val vibrationPattern: LongArray? = null,
    val vibrationAmplitudes: IntArray? = null,
    val vibrationRepeat: Int = -1
)
