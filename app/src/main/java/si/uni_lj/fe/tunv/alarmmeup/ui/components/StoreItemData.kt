package si.uni_lj.fe.tunv.alarmmeup.ui.data
import si.uni_lj.fe.tunv.alarmmeup.R

/**
 * @param id                  Unique ID of this store item
 * @param name                Display name
 * @param cost                In-app cost
 * @param playing             Is this currently “playing”? (toggled in StoreScreen)
 * @param imageUrl            (Optional) image URL or resource name
 * @param description         Short description
 * @param type                Whether this item is a SOUND or a VIBRATION
 * @param soundResourceId     Android raw resource ID for the alarm sound (R.raw.xxx) or null
 * @param vibrationPattern    [delay₀, vibrate₁, pause₂, vibrate₃, …] (in ms), or null if no vib
 * @param vibrationAmplitudes [amp₀…] matching each index of vibrationPattern, where 0 = silent, 1..255 = strength, or null
 * @param vibrationRepeat     Index into vibrationPattern at which to loop. Pass –1 to play only once.
 */

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
