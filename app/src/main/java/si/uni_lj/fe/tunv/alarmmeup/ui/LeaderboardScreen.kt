package si.uni_lj.fe.tunv.alarmmeup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import si.uni_lj.fe.tunv.alarmmeup.R
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

@Composable
fun LeaderboardScreen(leaders: List<Leader>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AvatarCluster(leaders = leaders)
        Spacer(modifier = Modifier.height(24.dp))
        LeaderboardList(leaders)
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
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(WhiteColor, shape = CircleShape)
                )
                Image(
                    painter = painterResource(it.avatarRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                FlameXpBubble(flames = it.flames, xp = it.xp, fontSize = 12.sp, iconSize = 16.dp, padding = 8.dp)
            }
        }
        left?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .offset(x = -100.dp, y = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(WhiteColor, shape = CircleShape)
                )
                Image(
                    painter = painterResource(it.avatarRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                FlameXpBubble(flames = it.flames, xp = it.xp, fontSize = 10.sp, iconSize = 12.dp, padding = 6.dp)
            }
        }
        right?.let {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .offset(x = 100.dp, y = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(WhiteColor, shape = CircleShape)
                )
                Image(
                    painter = painterResource(it.avatarRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                FlameXpBubble(flames = it.flames, xp = it.xp, fontSize = 10.sp, iconSize = 12.dp, padding = 6.dp)
            }
        }
    }
}

@Composable
fun BoxScope.FlameXpBubble(
    flames: Int,
    xp: Int,
    fontSize: androidx.compose.ui.unit.TextUnit,
    iconSize: androidx.compose.ui.unit.Dp,
    padding: androidx.compose.ui.unit.Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = padding)
            .background(color = TertiaryColor, RoundedCornerShape(12.dp))
            .padding(padding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_streak),
                contentDescription = "Flames",
                modifier = Modifier.size(iconSize),
                tint = AccentColor
            )
            Spacer(Modifier.width(2.dp))
            Text(text = "$flames", fontSize = fontSize)
        }
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("XP ")
                }
                append("$xp")
            },
            fontSize = fontSize
        )
    }
}

@Composable
fun LeaderboardList(leaders: List<Leader>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(leaders) { leader ->
            Box(modifier = Modifier.fillMaxWidth()) {
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
