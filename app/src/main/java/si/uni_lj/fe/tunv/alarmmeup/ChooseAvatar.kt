package si.uni_lj.fe.tunv.alarmmeup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum

@Composable
fun ChooseAvatar(
    onAvatarSelected: (Int, Int) -> Unit,
    imageOptions: List<Int>,
    repo: SessionRepo
){

    val user by repo.currentUser.collectAsState(initial = null)

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading Images")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.Gray
                )
                Text(
                    text = "Choose your avatar",
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.Gray
                )
            }
        }

        items(imageOptions) { resId ->
            val isSelected = resId == ProfilePictureEnum.toResource(user!!.profilePicture)

            Image(
                painter = painterResource(resId),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = if (isSelected) Color.DarkGray else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onAvatarSelected(resId, user!!.id) },
                contentScale = ContentScale.Crop
            )
        }

    }
}