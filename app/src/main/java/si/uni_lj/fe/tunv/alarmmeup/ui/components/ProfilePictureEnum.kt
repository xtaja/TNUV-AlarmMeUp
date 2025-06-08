package si.uni_lj.fe.tunv.alarmmeup.ui.components
import si.uni_lj.fe.tunv.alarmmeup.R

enum class ProfilePictureEnum {
    Man1,
    Man2,
    Man3,
    Man4,
    Man5,
    Man6,
    Man7,
    Man8,
    Man9,
    Man10,
    Man11,
    Man12,
    Man13,
    Man14,
    Man15,
    Woman1,
    Woman2,
    Woman3,
    Woman4,
    Woman5,
    Woman6,
    Woman7,
    Woman8,
    Woman9,
    Woman10,
    Woman11,
    Woman12,
    Woman13,
    Woman14,
    Woman15;

    companion object {
        @JvmStatic
        fun toResource(profilePictureEnum: ProfilePictureEnum): Int = when (profilePictureEnum) {
            Man1 -> R.drawable.man1
            Man2 -> R.drawable.man2
            Man3 -> R.drawable.man3
            Man4 -> R.drawable.man4
            Man5 -> R.drawable.man5
            Man6 -> R.drawable.man6
            Man7 -> R.drawable.man7
            Man8 -> R.drawable.man8
            Man9 -> R.drawable.man9
            Man10 -> R.drawable.man10
            Man11 -> R.drawable.man11
            Man12 -> R.drawable.man12
            Man13 -> R.drawable.man13
            Man14 -> R.drawable.man14
            Man15 -> R.drawable.man15
            Woman1 -> R.drawable.woman1
            Woman2 -> R.drawable.woman2
            Woman3 -> R.drawable.woman3
            Woman4 -> R.drawable.woman4
            Woman5 -> R.drawable.woman5
            Woman6 -> R.drawable.woman6
            Woman7 -> R.drawable.woman7
            Woman8 -> R.drawable.woman8
            Woman9 -> R.drawable.woman9
            Woman10 -> R.drawable.woman10
            Woman11 -> R.drawable.woman11
            Woman12 -> R.drawable.woman12
            Woman13 -> R.drawable.woman13
            Woman14 -> R.drawable.woman14
            Woman15 -> R.drawable.woman15
        }

        @JvmStatic
        fun toEnum(resourceId: Int): ProfilePictureEnum = when (resourceId) {
            R.drawable.man1 -> Man1
            R.drawable.man2 -> Man2
            R.drawable.man3 -> Man3
            R.drawable.man4 -> Man4
            R.drawable.man5 -> Man5
            R.drawable.man6 -> Man6
            R.drawable.man7 -> Man7
            R.drawable.man8 -> Man8
            R.drawable.man9 -> Man9
            R.drawable.man10 -> Man10
            R.drawable.man11 -> Man11
            R.drawable.man12 -> Man12
            R.drawable.man13 -> Man13
            R.drawable.man14 -> Man14
            R.drawable.man15 -> Man15
            R.drawable.woman1 -> Woman1
            R.drawable.woman2 -> Woman2
            R.drawable.woman3 -> Woman3
            R.drawable.woman4 -> Woman4
            R.drawable.woman5 -> Woman5
            R.drawable.woman6 -> Woman6
            R.drawable.woman7 -> Woman7
            R.drawable.woman8 -> Woman8
            R.drawable.woman9 -> Woman9
            R.drawable.woman10 -> Woman10
            R.drawable.woman11 -> Woman11
            R.drawable.woman12 -> Woman12
            R.drawable.woman13 -> Woman13
            R.drawable.woman14 -> Woman14
            R.drawable.woman15 -> Woman15

            else -> throw IllegalArgumentException("Invalid profile picture resource")
        }
    }
}
