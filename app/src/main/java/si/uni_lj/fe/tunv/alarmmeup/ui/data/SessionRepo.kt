package si.uni_lj.fe.tunv.alarmmeup.ui.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import si.uni_lj.fe.tunv.alarmmeup.ui.alarmData
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import kotlin.math.min

val Context.userPrefs by preferencesDataStore("user_prefs")
object Keys { val CURRENT_ID = intPreferencesKey("current_uid") }


class SessionRepo(
    private val ds: DataStore<Preferences>,
    private val dao: UserDao,
    private val daoAlarms: AlarmDao
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUser: Flow<UserEntity?> =
        ds.data.map { it[Keys.CURRENT_ID] }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                if (id == null) flowOf(null) else dao.byIdFlow(id)
            }

    suspend fun login(id: Int) = ds.edit { it[Keys.CURRENT_ID] = id }
    suspend fun logout()       = ds.edit { it.remove(Keys.CURRENT_ID) }
    suspend fun saveProfile(updated: UserEntity) =
        dao.update(updated)

    suspend fun changePassword(id: Int, old: String, new: String): Boolean {
        val current = dao.byIdFlow(id).first() ?: return false
        if (current.password != old) return false
        dao.updatePassword(id, new)
        return true
    }

    suspend fun updateProfilePicture(id: Int, picture: ProfilePictureEnum) : Boolean {
        dao.updateProfilePictureEnum(id, picture)
        return true
    }

    suspend fun updateClock(hour: Int, minute: Int, id: Int) : Boolean {
        daoAlarms.updateClock(hour, minute, id)
        return true
    }

    fun getClock(id: Int) : Flow<AlarmEntity?> {
        return daoAlarms.selectById(id)
    }
}