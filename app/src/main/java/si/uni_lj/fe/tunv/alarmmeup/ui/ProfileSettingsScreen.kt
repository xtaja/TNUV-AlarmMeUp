package si.uni_lj.fe.tunv.alarmmeup.ui

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import si.uni_lj.fe.tunv.alarmmeup.ChooseAvatar
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AppDatabase
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import java.util.logging.Logger

@Composable
fun EditableProfilePicture(
    imageUri: Uri?,
    placeholderRes: Int,
    size: Dp = 120.dp,
    onPhotoPicked: (Uri) -> Unit,
    onProfilesClick: () -> Unit
) {
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let(onPhotoPicked)
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable{onProfilesClick()},
        contentAlignment = Alignment.Center
    ) {
        val painter = if (imageUri != null)
            rememberAsyncImagePainter(imageUri)
        else painterResource(placeholderRes)

        Image(
            painter = painter,
            contentDescription = "Profile picture",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit picture",
            tint = Color.White,
            modifier = Modifier.size(size / 3)
        )
    }
}

@Composable
fun ProfileSettingsScreen(
    repo: SessionRepo,
    onReturnProfileView: () -> Unit,
    onProfilesClick: () -> Unit,
    goToAuthorizationScreen: () -> Unit
) {
    val user by repo.currentUser.collectAsState(initial = null)

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading profile…")
        }
        return
    }

    var newFullname by remember { mutableStateOf("") }
    var newUsername by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }

    LaunchedEffect(user!!.id) {
        newFullname = user!!.fullName
        newUsername = user!!.username
        newEmail    = user!!.email
    }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var profilePicUri by remember { mutableStateOf<Uri?>(null) }

    val scrollState = rememberScrollState()

    val scope  = rememberCoroutineScope()
    val ctx   = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        EditableProfilePicture(
            imageUri = profilePicUri,
            placeholderRes = ProfilePictureEnum.toResource(user!!.profilePicture),
            onPhotoPicked = { uri -> profilePicUri = uri },
            onProfilesClick = onProfilesClick
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            OutlinedTextField(
                value = newFullname,
                onValueChange = { it -> newFullname = it },
                label = { Text("Name and Surname") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newUsername,
                onValueChange = { it -> newUsername = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newEmail,
                onValueChange = { it -> newEmail = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            val profileChanged = newFullname != user!!.fullName ||
                    newUsername != user!!.username ||
                    newEmail    != user!!.email

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = oldPassword,
                onValueChange = { it -> oldPassword = it },
                label = { Text("Old Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { it -> newPassword = it },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { it -> confirmPassword = it },
                label = { Text("Confirm New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            val passwordValid = oldPassword.isNotBlank() &&
                    newPassword.isNotBlank() &&
                    newPassword == confirmPassword

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                OutlinedButton(
                    onClick = {onReturnProfileView()},
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White,
                        disabledContainerColor = Color.DarkGray,
                        disabledContentColor = Color.LightGray
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    Text(
                        text = "CANCEL",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                        color = Color.LightGray,
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                OutlinedButton(
                    enabled = profileChanged || passwordValid,
                    onClick = {
                        scope.launch {

                            val userDao = AppDatabase.get(ctx).userDao()

                            val conflictsUsername = userDao.checkUsernameAvailable(newUsername)
                            val usernameAvailable = (conflictsUsername == 0)


                            val conflictsEmail = userDao.checkEmailAvailable(newEmail)
                            val emailAvailable = (conflictsEmail == 0)

                            if (!usernameAvailable && user!!.username != newUsername) {
                                return@launch
                            }

                            if (!emailAvailable && user!!.email != newEmail) {
                                return@launch
                            }

                            if (profileChanged) {
                                val updated = user!!.copy(
                                    fullName = newFullname,
                                    username = newUsername,
                                    email    = newEmail
                                )
                                repo.saveProfile(updated)
                            }

                            if (passwordValid) {
                                val ok = repo.changePassword(
                                    id   = user!!.id,
                                    old  = oldPassword,
                                    new  = newPassword
                                )

                                if (!ok) {
                                    return@launch
                                }
                            }


                            onReturnProfileView()
                        }
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Black,
                        disabledContentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    Text(
                        text = "SAVE",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                        color = Color.LightGray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                var selected by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            repo.logout()
                            goToAuthorizationScreen()}
                        selected = true},
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selected) Color.LightGray else Color.White,
                    ),
                    border = BorderStroke(1.dp, Color.Red),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                ) {
                    Text(
                        text = "SIGN OUT",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                        color = Color.Red,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
