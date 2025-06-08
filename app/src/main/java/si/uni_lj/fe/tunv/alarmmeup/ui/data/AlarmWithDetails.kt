package si.uni_lj.fe.tunv.alarmmeup.ui.data

import androidx.room.Embedded
import androidx.room.Relation

data class AlarmWithDetails(
    @Embedded val alarm: AlarmEntity,

    @Relation(
        parentColumn = "soundId",
        entityColumn = "id"
    )
    val sound: SoundEntity?,

    @Relation(
        parentColumn = "vibrationId",
        entityColumn = "id"
    )
    val vibration: VibrationEntity?
)