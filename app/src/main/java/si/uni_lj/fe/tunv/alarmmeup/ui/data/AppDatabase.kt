package si.uni_lj.fe.tunv.alarmmeup.ui.data

import android.R.string
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ChallengeEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.DayStatus
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SoundCategoryEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SoundEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.VibrationCategoryEnum
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.logging.Level

@Entity(
    tableName = "alarms",
    foreignKeys = [
        ForeignKey(
            entity = SoundEntity::class,
            parentColumns = ["id"],
            childColumns = ["soundId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = VibrationEntity::class,
            parentColumns = ["id"],
            childColumns = ["vibrationId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("soundId"),
        Index("vibrationId")
    ]
)
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val soundId: Int?,
    val vibrationId: Int?,
    val volume: Float = 1f,
    val daysMask: Int,
    val challenge: ChallengeEnum,
    val enabled: Boolean = true
)

@Entity(
    tableName = "user_streak_data",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [ Index("userId") ]
)
data class UserStreakData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateRang: LocalDateTime,
    val dateActionFinished: LocalDateTime?,
    val challenge: ChallengeEnum,
    val soundId: Int,
    val vibrationId: Int,
    val action: DayStatus,
    val userId: Int
);

@Entity(
    tableName = "user_sounds",
    primaryKeys = ["userId", "soundId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SoundEntity::class,
            parentColumns = ["id"],
            childColumns = ["soundId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [ Index("userId"), Index("soundId") ]
)
data class UserSoundEntity(
    val userId: Int,
    val soundId: Int,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "user_vibrations",
    primaryKeys = ["userId", "vibrationId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VibrationEntity::class,
            parentColumns = ["id"],
            childColumns = ["vibrationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [ Index("userId"), Index("vibrationId") ]
)
data class UserVibrationEntity(
    val userId: Int,
    val vibrationId: Int,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "sounds",
    indices = [Index(unique = true, value = ["soundIdentifier"])]
)
data class SoundEntity(
    @PrimaryKey val id: Int,
    val soundIdentifier: SoundEnum,
    val category: SoundCategoryEnum,
    val name: String
)

@Entity(tableName = "vibrations")
data class VibrationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val category: VibrationCategoryEnum,
    val patternJson: String,
    val amplitudeJson: String
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val username: String,
    val email: String,
    val password: String,
    val profilePicture: ProfilePictureEnum,
    val coins: Int = 0,
    val xp: Int = 0,
    val lastGameCompletedDate: String? = null,
    val canChangeClockForFree: Boolean = true,
    val streak: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

class EnumConverters {
    @TypeConverter
    fun soundEnumToString(e: SoundEnum): String = e.name
    @TypeConverter
    fun stringToSoundEnum(s: String): SoundEnum = SoundEnum.valueOf(s)

    @TypeConverter
    fun profilePicToString(e: ProfilePictureEnum): String = e.name
    @TypeConverter
    fun stringToProfilePic(s: String): ProfilePictureEnum =
        ProfilePictureEnum.valueOf(s)

    @TypeConverter
    fun fromDayStatus(value: String?): DayStatus? {
        return value?.let { DayStatus.valueOf(it) }
    }

    @TypeConverter
    fun dayStatusToString(dayStatus: DayStatus?): String? {
        return dayStatus?.name
    }

}

class DateConverters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateTimeToString(e: Long?): LocalDateTime? {
        return e?.let { LocalDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneOffset.UTC) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
}

@Dao
interface AlarmDao {
    @Insert suspend fun insert(entity: AlarmEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<AlarmEntity>)
    @Update suspend fun update(entity: AlarmEntity)
    @Delete suspend fun delete(entity: AlarmEntity)
    @Query("SELECT * FROM alarms ORDER BY hour, minute")
    fun all(): Flow<List<AlarmEntity>>
    @Query("UPDATE alarms SET hour = :hour, minute = :minute WHERE id = :id")
    suspend fun updateClock(hour: Int, minute: Int, id: Int)
    @Query("SELECT * FROM alarms WHERE id = :id")
    fun selectById(id: Int): Flow<AlarmEntity?>
}

@Dao
interface UserStreakDataDao{
    @Insert suspend fun insert(entity: UserStreakData)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<UserStreakData>)
    @Query("SELECT * FROM user_streak_data WHERE userId = :userId")
    suspend fun getAllForUser(userId: Int) : List<UserStreakData>
}

@Dao
interface UserSoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<UserSoundEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entry: UserSoundEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockSound(entry: UserSoundEntity)

    @Delete
    suspend fun removeSound(entry: UserSoundEntity)

    @Query("""
    SELECT s.* 
      FROM sounds AS s 
      INNER JOIN user_sounds AS us 
        ON s.id = us.soundId 
     WHERE us.userId = :userId
    """)
    fun getUnlockedSounds(userId: Int): Flow<List<SoundEntity>>
}

@Dao
interface UserVibrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<UserVibrationEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(entry: UserVibrationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockVibration(entry: UserVibrationEntity)

    @Delete
    suspend fun removeVibration(entry: UserVibrationEntity)

    @Query("""
    SELECT v.* 
      FROM vibrations AS v 
      INNER JOIN user_vibrations AS uv 
        ON v.id = uv.vibrationId 
     WHERE uv.userId = :userId
    """)
    fun getUnlockedVibrations(userId: Int): Flow<List<VibrationEntity>>
}


@Dao
interface SoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<SoundEntity>)
    @Query("SELECT * FROM sounds")
    fun all(): Flow<List<SoundEntity>>

    @Query("SELECT * FROM sounds WHERE id = :id")
    fun getById(id: Int) : SoundEntity?
}

@Dao
interface VibrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<VibrationEntity>)
    @Query("SELECT * FROM vibrations")
    fun all(): Flow<List<VibrationEntity>>

    @Query("SELECT * FROM vibrations WHERE id = :id")
    fun getById(id: Int) : VibrationEntity?
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<UserEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Query("UPDATE users SET profilePicture = :profilePicture WHERE id = :userId")
    suspend fun updateProfilePictureEnum(userId: Int, profilePicture: ProfilePictureEnum)

    @Query("SELECT * FROM users WHERE (username = :username OR email = :username) AND password = :password")
    suspend fun findByCredentials(username: String, password: String): UserEntity?

    @Query("UPDATE users SET password = :newPwd WHERE id = :id")
    suspend fun updatePassword(id: Int, newPwd: String)

    @Query("SELECT COUNT(*) FROM users WHERE email = :email OR username = :username")
    suspend fun checkDetailsAvailable(email: String, username: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun checkUsernameAvailable(username: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun checkEmailAvailable(email: String): Int

    @Query("SELECT * FROM users WHERE id = :id")
    fun byIdFlow(id: Int): Flow<UserEntity?>

    @Query("UPDATE users SET streak = :streak WHERE id = :id")
    fun updateStreak(id: Int, streak: Int)

    @Query("SELECT * FROM users ORDER BY xp DESC LIMIT 10")
    fun getTopTenUsers(): Flow<List<UserEntity>>

    @Query("UPDATE users SET canChangeClockForFree = 0 WHERE id = :id")
    suspend fun setUserChangeClockForFree(id: Int)
}

@Database(
    entities = [AlarmEntity::class, SoundEntity::class, VibrationEntity::class, UserEntity::class, UserSoundEntity::class, UserVibrationEntity::class, UserStreakData::class],
    version = 113,
    exportSchema = false
)
@TypeConverters(EnumConverters::class, DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun soundDao(): SoundDao
    abstract fun vibrationDao(): VibrationDao
    abstract fun userDao(): UserDao
    abstract fun userSoundDao(): UserSoundDao
    abstract fun userVibrationDao(): UserVibrationDao
    abstract fun userStreakDataDao(): UserStreakDataDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDb(ctx).also { INSTANCE = it }
            }

        private fun buildDb(ctx: Context): AppDatabase =
            Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "alarmmeup.db")
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        seedEverything(ctx)
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        seedEverything(ctx)
                    }

                    @RequiresApi(Build.VERSION_CODES.O)
                    private fun seedEverything(ctx: Context) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val soundSeeds = listOf(
                                SoundEntity(1,  SoundEnum.Siren1, SoundCategoryEnum.Featured,   "Siren 1"),
                                SoundEntity(2,  SoundEnum.Digital1, SoundCategoryEnum.Featured,  "Digital 1"),
                                SoundEntity(3,  SoundEnum.Digital2, SoundCategoryEnum.Featured,  "Digital 2"),
                                SoundEntity(4,  SoundEnum.EightBit, SoundCategoryEnum.Featured,  "8-bit"),
                                SoundEntity(5,  SoundEnum.OldPhone, SoundCategoryEnum.Featured,  "Old phone"),
                                SoundEntity(6,  SoundEnum.Digital3, SoundCategoryEnum.Featured,  "Digital 3"),
                                SoundEntity(7,  SoundEnum.Digital4,  SoundCategoryEnum.Featured, "Digital 4"),
                                SoundEntity(8,  SoundEnum.SoftWaves, SoundCategoryEnum.Featured, "Soft waves"),
                                SoundEntity(9,  SoundEnum.Tropical, SoundCategoryEnum.Featured,  "Tropical"),
                                SoundEntity(10, SoundEnum.Doomsday, SoundCategoryEnum.Featured,  "Doomsday"),
                                SoundEntity(11, SoundEnum.Siren2,  SoundCategoryEnum.Featured,   "Siren 2"),

                                SoundEntity(12, SoundEnum.Drops,  SoundCategoryEnum.NewAlarms,    "Drops"),
                                SoundEntity(13, SoundEnum.Beep,   SoundCategoryEnum.NewAlarms,    "Beep"),
                                SoundEntity(14, SoundEnum.Golden,  SoundCategoryEnum.NewAlarms,   "Golden"),
                                SoundEntity(15, SoundEnum.Sweet,   SoundCategoryEnum.NewAlarms,   "Sweet"),
                                SoundEntity(16, SoundEnum.Jingles, SoundCategoryEnum.NewAlarms,   "Jingles"),
                                SoundEntity(17, SoundEnum.Melodic, SoundCategoryEnum.NewAlarms,   "Melodic"),
                                SoundEntity(18, SoundEnum.Lofi,   SoundCategoryEnum.NewAlarms,    "Lo-fi"),
                                SoundEntity(19, SoundEnum.Voyage, SoundCategoryEnum.NewAlarms,    "Voyage"),
                                SoundEntity(20, SoundEnum.Memphic, SoundCategoryEnum.NewAlarms,   "Memphis"),
                                SoundEntity(21, SoundEnum.Anime,   SoundCategoryEnum.NewAlarms,   "Anime"),
                                SoundEntity(22, SoundEnum.Police,  SoundCategoryEnum.NewAlarms,   "Police")
                            )

                            val vibrationSeeds = listOf(
                                VibrationEntity(
                                    id = 23, name = "Wave Wash",
                                    category = VibrationCategoryEnum.Featured,
                                    patternJson   = "[0,150,75,150,75,300,75,150,75]",
                                    amplitudeJson = "[0,255,0,255,0,200,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 24, name = "Police",
                                    category = VibrationCategoryEnum.Featured,
                                    patternJson   = "[0,150,75,150,75,300,75,150,75]",
                                    amplitudeJson = "[0,255,0,255,0,200,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 25, name = "ZigZag",
                                    category = VibrationCategoryEnum.Featured,
                                    patternJson   = "[0,80,40,120,40,160,40]",
                                    amplitudeJson = "[0,200,0,220,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 26, name = "Heartbeat",
                                    category = VibrationCategoryEnum.Featured,
                                    patternJson   = "[0,200,100,200,500]",
                                    amplitudeJson = "[0,255,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 27, name = "Chill Wave",
                                    category = VibrationCategoryEnum.Featured,
                                    patternJson   = "[0,700,300]",
                                    amplitudeJson = "[0,100,0]"
                                ),
                                VibrationEntity(
                                    id = 28, name = "Thunder",
                                    category = VibrationCategoryEnum.Featured,
                                    patternJson   = "[0,500,100,500,100,500,100]",
                                    amplitudeJson = "[0,255,0,255,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 29, name = "Ripple",
                                    category = VibrationCategoryEnum.Popular,
                                    patternJson   = "[0,600,300,400,200]",
                                    amplitudeJson = "[0,120,0,100,0]"
                                ),
                                VibrationEntity(
                                    id = 30, name = "Quest",
                                    category = VibrationCategoryEnum.Popular,
                                    patternJson   = "[0,100,50,120,50,140,50,160,50]",
                                    amplitudeJson = "[0,255,0,255,0,255,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 31, name = "8-Bit Zap",
                                    category = VibrationCategoryEnum.Popular,
                                    patternJson   = "[0,500,500,500,500,500,500]",
                                    amplitudeJson = "[0,220,0,240,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 32, name = "Starlink",
                                    category = VibrationCategoryEnum.Popular,
                                    patternJson   = "[0,150,75,75,75,150,75]",
                                    amplitudeJson = "[0,255,0,220,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 33, name = "Campfire",
                                    category = VibrationCategoryEnum.Popular,
                                    patternJson   = "[0,60,30,120,30,90,30,60,30]",
                                    amplitudeJson = "[0,255,0,200,0,180,0,200,0]"
                                ),
                                VibrationEntity(
                                    id = 34, name = "Metro",
                                    category = VibrationCategoryEnum.Popular,
                                    patternJson   = "[0,300,100,150,100,300,100]",
                                    amplitudeJson = "[0,220,0,200,0,230,0]"
                                )
                            )

                            val defaultAlarmsEntities = listOf(
                                AlarmEntity(
                                    id = 1,
                                    hour = 14,
                                    minute = 0,
                                    soundId = 1,
                                    vibrationId = 23,
                                    daysMask = 0,
                                    challenge = ChallengeEnum.Math,
                                    enabled = true
                                ),
                                AlarmEntity(
                                    id = 2,
                                    hour = 9,
                                    minute = 0,
                                    soundId = 1,
                                    vibrationId = 23,
                                    daysMask = 0,
                                    challenge = ChallengeEnum.Math,
                                    enabled = true
                                ),
                                AlarmEntity(
                                    id = 3,
                                    hour = 11,
                                    minute = 0,
                                    soundId = 1,
                                    vibrationId = 23,
                                    daysMask = 0,
                                    challenge = ChallengeEnum.Math,
                                    enabled = true
                                ),
                                AlarmEntity(
                                    id = 4,
                                    hour = 9,
                                    minute = 0,
                                    soundId = 1,
                                    vibrationId = 23,
                                    daysMask = 0,
                                    challenge = ChallengeEnum.Math,
                                    enabled = true
                                ),
                                AlarmEntity(
                                    id = 5,
                                    hour = 11,
                                    minute = 0,
                                    soundId = 1,
                                    vibrationId = 23,
                                    daysMask = 0,
                                    challenge = ChallengeEnum.Math,
                                    enabled = true
                                )

                            )

                            get(ctx).apply {

                                val amyId   = userDao().insert(UserEntity(
                                    0,
                                    "Amy Adams",
                                    "amyzams",
                                    "amyadams@gmail.com",
                                    "123",
                                    ProfilePictureEnum.Woman4
                                ))
                                val mariaId = userDao().insert(UserEntity(
                                    0,
                                    "Maria Lora",
                                    "mara",
                                    "marialora@gmail.com",
                                    "pass",
                                    ProfilePictureEnum.Woman10
                                ))
                                val marcoId  = userDao().insert(UserEntity(
                                    0,
                                    "Marco So",
                                    "marcs",
                                    "marcs@gmail.com",
                                    "t",
                                    ProfilePictureEnum.Man1,
                                    streak = 3
                                ))
                                val kellyId  = userDao().insert(UserEntity(
                                    0,
                                    "Kelly Millila",
                                    "kelzmilz",
                                    "kelz@gmail.com",
                                    "pass",
                                    ProfilePictureEnum.Woman12,
                                ))
                                val nicoId  = userDao().insert(UserEntity(
                                    0,
                                    "Nico Blo",
                                    "nics",
                                    "nics@gmail.com",
                                    "123",
                                    ProfilePictureEnum.Man8,
                                ))

                                val defaultInventorySounds = listOf(
                                    UserSoundEntity(userId = amyId.toInt(), soundId = 1),
                                    UserSoundEntity(userId = amyId.toInt(), soundId = 2),

                                    UserSoundEntity(userId = mariaId.toInt(), soundId = 4),
                                    UserSoundEntity(userId = mariaId.toInt(), soundId = 5),

                                    UserSoundEntity(userId = marcoId.toInt(), soundId = 10),
                                    UserSoundEntity(userId = marcoId.toInt(), soundId = 12),

                                    UserSoundEntity(userId = kellyId.toInt(), soundId = 4),
                                    UserSoundEntity(userId = kellyId.toInt(), soundId = 5),

                                    UserSoundEntity(userId = nicoId.toInt(), soundId = 10),
                                    UserSoundEntity(userId = nicoId.toInt(), soundId = 12)
                                )

                                val defaultInventoryVibrations = listOf(
                                    UserVibrationEntity(userId = amyId.toInt(), vibrationId = 23),
                                    UserVibrationEntity(userId = amyId.toInt(), vibrationId = 24),

                                    UserVibrationEntity(userId = mariaId.toInt(), vibrationId = 25),
                                    UserVibrationEntity(userId = mariaId.toInt(), vibrationId = 26),

                                    UserVibrationEntity(userId = marcoId.toInt(), vibrationId = 27),
                                    UserVibrationEntity(userId = marcoId.toInt(), vibrationId = 28),

                                    UserVibrationEntity(userId = kellyId.toInt(), vibrationId = 25),
                                    UserVibrationEntity(userId = kellyId.toInt(), vibrationId = 26),

                                    UserVibrationEntity(userId = nicoId.toInt(), vibrationId = 27),
                                    UserVibrationEntity(userId = nicoId.toInt(), vibrationId = 28)
                                )

                                val defaultuserStreakData = listOf(
                                    UserStreakData(0, LocalDateTime.now(), LocalDateTime.now(), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.COMPLETED, marcoId.toInt()),
                                    UserStreakData(0, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.COMPLETED, marcoId.toInt()),
                                    UserStreakData(0, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.COMPLETED, marcoId.toInt()),
                                    UserStreakData(0, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.MISSED, marcoId.toInt()),
                                    UserStreakData(0, LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(4), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.MISSED, marcoId.toInt()),

                                    UserStreakData(0, LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(7), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.MISSED, marcoId.toInt()),
                                    UserStreakData(0, LocalDateTime.now().minusDays(8), LocalDateTime.now().minusDays(8), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.MISSED, marcoId.toInt()),

                                    UserStreakData(0, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.COMPLETED, marcoId.toInt()),
                                    UserStreakData(0, LocalDateTime.now().minusDays(11), LocalDateTime.now().minusDays(11), challenge = ChallengeEnum.Math, 1, 23,
                                        DayStatus.COMPLETED, marcoId.toInt()),

                                )

                                soundDao().insertAll(soundSeeds)
                                vibrationDao().insertAll(vibrationSeeds)
                                alarmDao().insertAll(defaultAlarmsEntities)
                                userVibrationDao().insertAll(defaultInventoryVibrations)
                                userSoundDao().insertAll(defaultInventorySounds)
                                userStreakDataDao().insertAll(defaultuserStreakData)
                            }
                        }
                    }
                })
                .build()
    }
}