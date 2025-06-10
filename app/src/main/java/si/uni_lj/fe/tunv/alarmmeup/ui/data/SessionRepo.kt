package si.uni_lj.fe.tunv.alarmmeup.ui.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum

val Context.userPrefs by preferencesDataStore("user_prefs")
object Keys { val CURRENT_ID = intPreferencesKey("current_uid") }


class SessionRepo(
    private val ds: DataStore<Preferences>,
    private val dao: UserDao,
    private val daoAlarms: AlarmDao,
    private val userSoundDao: UserSoundDao,
    private val userVibrationDao: UserVibrationDao
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUser: Flow<UserEntity?> =
        ds.data.map { it[Keys.CURRENT_ID] }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                if (id == null) flowOf(null) else dao.byIdFlow(id)
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOwnedSounds(): Flow<List<SoundEntity>> {
        return currentUser.flatMapLatest { user ->
            if (user == null) {
                flowOf(emptyList())
            } else {
                userSoundDao.getUnlockedSounds(user.id)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOwnedVibrations(): Flow<List<VibrationEntity>> {
        return currentUser.flatMapLatest { user ->
            if (user == null) {
                flowOf(emptyList())
            } else {
                userVibrationDao.getUnlockedVibrations(user.id)
            }
        }
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

    var user by mutableStateOf<UserEntity?>(null)

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

    suspend fun setAlarmSettings(ent: AlarmEntity) {
        daoAlarms.update(ent)
    }

    suspend fun addXPAndCoins(xp: Int, coins: Int) {
        user?.let {
            val updatedUser = it.copy(
                xp = (it.xp ?: 0) + xp,
                coins = (it.coins ?: 0) + coins
            )
            user = updatedUser
            dao.update(updatedUser)
            println("Added $xp XP and $coins coins. New total: ${updatedUser.xp} XP, ${updatedUser.coins} coins.")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun wasGameCompletedToday(): Boolean {
        val today = java.time.LocalDate.now().toString()
        return user?.lastGameCompletedDate == today
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun setGameCompletedToday() {
        val today = java.time.LocalDate.now().toString()
        user?.let {
            val updatedUser = it.copy(lastGameCompletedDate = today)
            user = updatedUser
            dao.update(updatedUser)
        }
    }
}