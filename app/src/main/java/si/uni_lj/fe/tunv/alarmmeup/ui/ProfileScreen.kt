package si.uni_lj.fe.tunv.alarmmeup.ui


import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePicture

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    resourceId: Int,
    name:String,
    surname: String,
    username: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfilePicture(resourceId = resourceId, size = 120.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$name $surname",
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = username,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}