package si.uni_lj.fe.tunv.alarmmeup.ui.data

import android.R.string
import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.SoundEnum

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
    val enabled: Boolean = true
)

@Entity(
    tableName = "sounds",
    indices = [Index(unique = true, value = ["soundIdentifier"])]
)
data class SoundEntity(
    @PrimaryKey val id: Int,
    val soundIdentifier: SoundEnum,
    val name: String
)

@Entity(tableName = "vibrations")
data class VibrationEntity(
    @PrimaryKey val id: Int,
    val name: String,
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
    val profilePicture: ProfilePictureEnum
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
interface SoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<SoundEntity>)
    @Query("SELECT * FROM sounds")
    fun all(): Flow<List<SoundEntity>>
}

@Dao
interface VibrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAll(list: List<VibrationEntity>)
    @Query("SELECT * FROM vibrations")
    fun all(): Flow<List<VibrationEntity>>
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
}

@Database(
    entities = [AlarmEntity::class, SoundEntity::class, VibrationEntity::class, UserEntity::class],
    version = 100,
    exportSchema = false
)
@TypeConverters(EnumConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao
    abstract fun soundDao(): SoundDao
    abstract fun vibrationDao(): VibrationDao
    abstract fun userDao(): UserDao

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
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        seedEverything(ctx)
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        seedEverything(ctx)
                    }

                    private fun seedEverything(ctx: Context) {
                        // run seeding off the UI thread
                        CoroutineScope(Dispatchers.IO).launch {
                            val soundSeeds = listOf(
                                SoundEntity(1,  SoundEnum.Siren1,     "Siren 1"),
                                SoundEntity(2,  SoundEnum.Digital1,   "Digital 1"),
                                SoundEntity(3,  SoundEnum.Digital2,   "Digital 2"),
                                SoundEntity(4,  SoundEnum.EightBit,   "8-bit"),
                                SoundEntity(5,  SoundEnum.OldPhone,   "Old phone"),
                                SoundEntity(6,  SoundEnum.Digital3,   "Digital 3"),
                                SoundEntity(7,  SoundEnum.Digital4,   "Digital 4"),
                                SoundEntity(8,  SoundEnum.SoftWaves,  "Soft waves"),
                                SoundEntity(9,  SoundEnum.Tropical,   "Tropical"),
                                SoundEntity(10, SoundEnum.Doomsday,   "Doomsday"),
                                SoundEntity(11, SoundEnum.Siren2,     "Siren 2"),

                                SoundEntity(12, SoundEnum.Drops,      "Drops"),
                                SoundEntity(13, SoundEnum.Beep,       "Beep"),
                                SoundEntity(14, SoundEnum.Golden,     "Golden"),
                                SoundEntity(15, SoundEnum.Sweet,      "Sweet"),
                                SoundEntity(16, SoundEnum.Jingles,    "Jingles"),
                                SoundEntity(17, SoundEnum.Melodic,    "Melodic"),
                                SoundEntity(18, SoundEnum.Lofi,       "Lo-fi"),
                                SoundEntity(19, SoundEnum.Voyage,     "Voyage"),
                                SoundEntity(20, SoundEnum.Memphic,    "Memphis"),
                                SoundEntity(21, SoundEnum.Anime,      "Anime"),
                                SoundEntity(22, SoundEnum.Police,     "Police")
                            )

                            val vibrationSeeds = listOf(
                                VibrationEntity(
                                    id = 23, name = "Wave Wash",
                                    patternJson   = "[0,150,75,150,75,300,75,150,75]",
                                    amplitudeJson = "[0,255,0,255,0,200,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 24, name = "Police",
                                    patternJson   = "[0,150,75,150,75,300,75,150,75]",
                                    amplitudeJson = "[0,255,0,255,0,200,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 25, name = "ZigZag",
                                    patternJson   = "[0,80,40,120,40,160,40]",
                                    amplitudeJson = "[0,200,0,220,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 26, name = "Heartbeat",
                                    patternJson   = "[0,200,100,200,500]",
                                    amplitudeJson = "[0,255,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 27, name = "Chill Wave",
                                    patternJson   = "[0,700,300]",
                                    amplitudeJson = "[0,100,0]"
                                ),
                                VibrationEntity(
                                    id = 28, name = "Thunder",
                                    patternJson   = "[0,500,100,500,100,500,100]",
                                    amplitudeJson = "[0,255,0,255,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 29, name = "Ripple",
                                    patternJson   = "[0,600,300,400,200]",
                                    amplitudeJson = "[0,120,0,100,0]"
                                ),
                                VibrationEntity(
                                    id = 30, name = "Quest",
                                    patternJson   = "[0,100,50,120,50,140,50,160,50]",
                                    amplitudeJson = "[0,255,0,255,0,255,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 31, name = "8-Bit Zap",
                                    patternJson   = "[0,500,500,500,500,500,500]",
                                    amplitudeJson = "[0,220,0,240,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 32, name = "Starlink",
                                    patternJson   = "[0,150,75,75,75,150,75]",
                                    amplitudeJson = "[0,255,0,220,0,255,0]"
                                ),
                                VibrationEntity(
                                    id = 33, name = "Campfire",
                                    patternJson   = "[0,60,30,120,30,90,30,60,30]",
                                    amplitudeJson = "[0,255,0,200,0,180,0,200,0]"
                                ),
                                VibrationEntity(
                                    id = 34, name = "Metro",
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
                                    enabled = true
                                ),
                                AlarmEntity(
                                    id = 2,
                                    hour = 9,
                                    minute = 0,
                                    soundId = 1,
                                    vibrationId = 23,
                                    enabled = true
                                ),
                                AlarmEntity(
                                    id = 3,
                                    hour = 11,
                                    minute = 0,
                                    soundId = 1,
                                    vibrationId = 23,
                                    enabled = true
                                )

                            )

                            val defaultUsers = listOf(
                                UserEntity(
                                    0,
                                    "Amy Adams",
                                    "amyzams",
                                    "amyadams@gmail.com",
                                    "123",
                                    ProfilePictureEnum.Man1
                                ),
                                UserEntity(
                                    0,
                                    "Maria Lora",
                                    "mara",
                                    "marialora@gmail.com",
                                    "pass",
                                    ProfilePictureEnum.Man1
                                ),
                                UserEntity(
                                    0,
                                    "Test",
                                    "test",
                                    "t",
                                    "t",
                                    ProfilePictureEnum.Man1
                                )
                            )
                            get(ctx).apply {
                                userDao().insertAll(defaultUsers)
                                soundDao().insertAll(soundSeeds)
                                vibrationDao().insertAll(vibrationSeeds)
                                alarmDao().insertAll(defaultAlarmsEntities)
                            }
                        }
                    }
                })
                .build()
    }
}