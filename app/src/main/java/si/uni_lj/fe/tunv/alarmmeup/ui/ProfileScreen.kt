package si.uni_lj.fe.tunv.alarmmeup.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.ui.components.CloudsDecoration
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePicture
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfileSettingsBtn
import si.uni_lj.fe.tunv.alarmmeup.ui.components.QrCodeView
import si.uni_lj.fe.tunv.alarmmeup.ui.components.QrScanToggleAndText
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ScanView
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo

sealed class ProfileTabScreen {
    object Main : ProfileTabScreen()
    object QrCode : ProfileTabScreen()
    object Scan : ProfileTabScreen()
}

@Composable
fun ProfileScreen(
    repo: SessionRepo,
    resetKey: Int,
    onSettingsClick: () -> Unit
) {
    val user by repo.currentUser.collectAsState(initial = null)

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading profile…")
        }
        return
    }

    var currentScreen by remember { mutableStateOf<ProfileTabScreen>(ProfileTabScreen.Main) }
    androidx.compose.runtime.LaunchedEffect(resetKey) {
        currentScreen = ProfileTabScreen.Main
    }

    when (currentScreen) {
        ProfileTabScreen.Main -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfilePicture(resourceId = ProfilePictureEnum.toResource(user!!.profilePicture), size = 120.dp)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(250.dp)
                        .height(60.dp)
                ) {
                    CloudsDecoration(modifier = Modifier.fillMaxSize())
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = user!!.fullName,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(0.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "@${user!!.username}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier.clickable { currentScreen = ProfileTabScreen.QrCode }
                            ) {
                                QrCodeView(content = user!!.username, size = 80)
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileSettingsBtn(
                        onSettingsClick
                    )
                }
            }
        }
        ProfileTabScreen.QrCode -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfilePicture(resourceId = ProfilePictureEnum.toResource(user!!.profilePicture), size = 120.dp)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(250.dp)
                            .height(60.dp)
                    ) {
                        CloudsDecoration(modifier = Modifier.fillMaxSize())
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = user!!.fullName,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(0.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                QrCodeView(
                                    content = user!!.username,
                                    size = 80,
                                    QRcolor = Color(0xF2F2F2F2)
                                )
                                Text(
                                    text = user!!.username,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                                QrCodeView(
                                    content = user!!.username,
                                    size = 80,
                                    QRcolor = Color(0xF2F2F2F2)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(35.dp))
                    QrCodeView(content = user!!.username, size = 500)
                    Text(
                        text = user!!.username,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                var isScan by remember { mutableStateOf(false) }
                QrScanToggleAndText(
                    isScan = isScan,
                    onToggle = {
                        isScan = it
                        currentScreen = if (it) ProfileTabScreen.Scan else ProfileTabScreen.QrCode
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 0.dp)
                )
            }
        }
        ProfileTabScreen.Scan -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ScanView(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
                QrScanToggleAndText(
                    isScan = true,
                    onToggle = {
                        if (!it) currentScreen = ProfileTabScreen.QrCode
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 0.dp)
                )
            }
        }
    }
}