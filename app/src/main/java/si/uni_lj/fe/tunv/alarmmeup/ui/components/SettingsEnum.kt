package si.uni_lj.fe.tunv.alarmmeup.ui.components

enum class SettingsEnum {
    Profile,
    Settings,
    ProfileSettings;
    companion object {
        @JvmStatic
        fun fromString(value: String): SettingsEnum = when (value) {
            "Profile" -> Profile
            "Settings" -> Settings
            "ProfileSettings" -> ProfileSettings
            else -> throw IllegalArgumentException("Invalid value: $value")
        }
    }

}

