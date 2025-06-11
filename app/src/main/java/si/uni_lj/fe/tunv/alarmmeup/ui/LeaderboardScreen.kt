package si.uni_lj.fe.tunv.alarmmeup.ui

import android.os.Build
import android.se.omapi.Session
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.zIndex
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.AccentColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.SecondaryColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.TertiaryColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

data class Leader(
    val name: String,
    val handle: String,
    val avatarRes: Int,
    val xp: Int,
    val flames: Int,
    val rank: Int
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeaderboardScreen(
    repo: SessionRepo
) {

    var leaders = repo.getLeaderboard().collectAsState(initial = emptyList())

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item { AvatarCluster(leaders = leaders.value) }

        if (!leaders.value.isEmpty()) {

            item { Spacer(modifier = Modifier.height(75.dp)) }

            items(leaders.value.subList(3, leaders.value.size)) { leader ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp)
                            .padding(vertical = 2.dp)
                            .background(SecondaryColor, RoundedCornerShape(12.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(67.dp)
                                .zIndex(1f)
                                .offset(x = (-35).dp)
                                .background(WhiteColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(leader.avatarRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.offset(x = (-16).dp),
                            ) {
                                Text(
                                    leader.name,
                                    fontSize = 16.sp,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    leader.handle,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.offset(x = (-25).dp),
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("XP ")
                                    }
                                    append("${leader.xp}")
                                },
                                fontSize = 14.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_streak),
                                    contentDescription = "Flames",
                                    modifier = Modifier.size(14.dp),
                                    tint = AccentColor
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(text = "${leader.flames}", fontSize = 12.sp)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                    }

                    Text(
                        text = "${leader.rank}",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .zIndex(2f)
                    )
                }
            }
        }
    }
}

@Composable
fun CircleBubble(
    text: String,
    icon: Boolean = false,
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    color: Color = TertiaryColor,
    contentAlignment: Alignment = Alignment.Center,
    offset: Dp = 0.dp,
    zindex: Float,
    sizeFont: TextUnit = 15.sp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color = color, shape = CircleShape)
            .zIndex(zindex),
        contentAlignment = contentAlignment,
    ) {

        if (icon) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_streak),
                    contentDescription = "Flames",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = text,
                    fontSize = sizeFont,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.offset(y = offset),
                )
            }
        } else {
            Text(
                text = text,
                fontSize = sizeFont,
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.offset(y = offset),
            )
        }
    }
}



@Composable
fun AvatarCluster(leaders: List<Leader>) {
    val center = leaders.getOrNull(0)
    val left = leaders.getOrNull(1)
    val right = leaders.getOrNull(2)

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        center?.let {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .height(250.dp)
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_day_sunny_color_icon),
                    contentDescription = "Sun background",
                    modifier = Modifier
                        .size(250.dp)
                )

                Box(
                    modifier = Modifier
                        .offset(x = (-21).dp, y = (-11).dp)
                ){
                    CircleBubble(
                        text = it.handle,
                        size = 70.dp,
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .offset(x = (-60).dp, y = (-30).dp),
                        offset = (20).dp,
                        zindex = (1f)
                    )

                    CircleBubble(
                        text = "XP ${it.xp}",
                        size = 60.dp,
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = (-70).dp, y = (10).dp),
                        offset = (0).dp,
                        zindex = (2f),
                        sizeFont = 10.sp
                    )

                    CircleBubble(
                        text = it.flames.toString(),
                        icon = true,
                        size = 40.dp,
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = (-90).dp, y = (0).dp),
                        offset = (0).dp,
                        zindex = (3f),
                    )

                }

                Image(
                    painter = painterResource(id = it.avatarRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )

            }
        }
        left?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp)
                    .height(250.dp)
                    .offset(x = (-50).dp, y = 100.dp)
                    .zIndex(2f),
            ) {
                Box(
                    modifier = Modifier
                        .size(117.dp)
                        .background(WhiteColor, shape = CircleShape)
                )

                Box(
                    modifier = Modifier
                        .offset(x = (10).dp, y = (0).dp)
                ){
                    CircleBubble(
                        text = it.handle,
                        size = 70.dp,
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .offset(x = (-60).dp, y = (-30).dp),
                        offset = (20).dp,
                        zindex = (1f)
                    )

                    CircleBubble(
                        text = "XP ${it.xp}",
                        size = 60.dp,
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = (-70).dp, y = (10).dp),
                        offset = (0).dp,
                        zindex = (2f),
                        sizeFont = 10.sp
                    )

                    CircleBubble(
                        text = it.flames.toString(),
                        icon = true,
                        size = 40.dp,
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = (-90).dp, y = (0).dp),
                        offset = (0).dp,
                        zindex = (3f),
                    )

                }

                Image(
                    painter = painterResource(it.avatarRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )



            }
        }
        right?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp)
                    .height(250.dp)
                    .offset(x = 50.dp, y = 110.dp)
                    .zIndex(3f),
            ) {
                Box(
                    modifier = Modifier
                        .size(97.dp)
                        .background(WhiteColor, shape = CircleShape)
                )

                Box(
                    modifier = Modifier
                        .offset(x = (120).dp, y = (30).dp)
                ){
                    CircleBubble(
                        text = it.handle,
                        size = 70.dp,
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .offset(x = (-60).dp, y = (-30).dp),
                        offset = (20).dp,
                        zindex = (1f)
                    )

                    CircleBubble(
                        text = "XP ${it.xp}",
                        size = 60.dp,
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = (-70).dp, y = (10).dp),
                        offset = (0).dp,
                        zindex = (2f),
                        sizeFont = 10.sp
                    )

                    CircleBubble(
                        text = it.flames.toString(),
                        icon = true,
                        size = 40.dp,
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset(x = (-20).dp, y = (20).dp),
                        offset = (0).dp,
                        zindex = (3f),
                    )

                }

                Image(
                    painter = painterResource(it.avatarRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )


            }
        }
    }
}
