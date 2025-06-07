package si.uni_lj.fe.tunv.alarmmeup.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter

@Composable
fun EditableProfilePicture(
    imageUri: Uri?,
    placeholderRes: Int,
    size: Dp = 120.dp,
    onPhotoPicked: (Uri) -> Unit
) {
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let(onPhotoPicked)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) photoPickerLauncher.launch("image/*")
    }

    fun pickImage() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        val hasPermission = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            photoPickerLauncher.launch("image/*")
        } else {
            permissionLauncher.launch(permission)
        }
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable { pickImage() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        } else {
            Image(
                painter = painterResource(id = placeholderRes),
                contentDescription = "Profile picture placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
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
    resourceId: Int = android.R.drawable.sym_def_app_icon,
    currentName: String = "Amy",
    currentSurname: String = "Adams",
    currentUsername: String = "@amyzams",
    currentEmail: String = "amyadams@gmail.com",
    onSaveProfile: (name: String, surname: String, username: String, email: String) -> Unit = { _, _, _, _ -> },
    onChangePassword: (oldPassword: String, newPassword: String) -> Unit = { _, _ -> }
) {
    var name by remember { mutableStateOf(currentName) }
    var surname by remember { mutableStateOf(currentSurname) }
    var username by remember { mutableStateOf(currentUsername) }
    var email by remember { mutableStateOf(currentEmail) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var profilePicUri by remember { mutableStateOf<Uri?>(null) }

    val scrollState = rememberScrollState()

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
            placeholderRes = resourceId,
            onPhotoPicked = { uri -> profilePicUri = uri }
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
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Surname") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            val profileChanged = name != currentName ||
                    surname != currentSurname ||
                    username != currentUsername ||
                    email != currentEmail

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = { Text("Old Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
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
                    onClick = {},
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
                    onClick = {},
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
                        text = "SAVE",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                        color = Color.LightGray,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
