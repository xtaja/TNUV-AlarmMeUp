package si.uni_lj.fe.tunv.alarmmeup.ui.data

data class StoreItemData(
    val id: Int,
    val name: String,
    val cost: Int,
    var playing: Boolean = false,
    val imageUrl: String? = null,
    val description: String = "",
    val soundResourceId: Int? = null
)