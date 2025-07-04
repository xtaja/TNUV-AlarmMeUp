package si.uni_lj.fe.tunv.alarmmeup.ui

import android.util.Patterns
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ChallengeEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ProfilePictureEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AlarmEntity
import si.uni_lj.fe.tunv.alarmmeup.ui.data.AppDatabase
import si.uni_lj.fe.tunv.alarmmeup.ui.data.UserEntity
import si.uni_lj.fe.tunv.alarmmeup.ui.data.UserSoundEntity
import si.uni_lj.fe.tunv.alarmmeup.ui.data.UserVibrationEntity
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.GrayColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.Red19
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.SecondaryColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

@Composable
fun AuthButton(text: String, onClick: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    OutlinedButton (
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenWidth * 0.01f)
            .height(screenWidth * 0.12f),
        colors = ButtonDefaults.buttonColors(containerColor = SecondaryColor),
        border = BorderStroke(1.dp, BlackColor),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 25.sp,
                color = BlackColor)
        )
    }
}

@Composable
fun MorphingAuthBox(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    onContinue: (Int) -> Unit,
    showName: Boolean = false,
    showSurname: Boolean = false,
    showEmail: Boolean = false,
    showPassword: Boolean = true,
    showConfirmPassword: Boolean = false,
    showEmailOrUsername: Boolean = false,
    height: Int,
) {

    val ctx   = LocalContext.current
    val scope = rememberCoroutineScope()
    val targetHeight by animateDpAsState(
        targetValue = if (isActive) height.dp else 50.dp,
        animationSpec = tween(600, easing = FastOutSlowInEasing)
    )

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var passwordMismatch by remember { mutableStateOf(false) }
    var invalidEmail by remember { mutableStateOf(false) }
    var emailOrUsername by remember { mutableStateOf("") }

    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) SecondaryColor else SecondaryColor,
        animationSpec = tween(600)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(targetHeight)
            .clip(RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, BlackColor), RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(if (!isActive) Modifier.clickable { onClick() } else Modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 25.sp),
                color = BlackColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isActive) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (showName) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text(stringResource(R.string.full_name_label)) },
                            placeholder = { Text(stringResource(R.string.full_name_placeholder)) },
                            singleLine = true,
                            isError = showError && fullName.isBlank(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (showSurname) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text(stringResource(R.string.Username)) },
                            placeholder = { Text(stringResource(R.string.username_placeholder)) },
                            singleLine = true,
                            isError = showError && username.isBlank(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (showEmail) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(stringResource(R.string.email_label)) },
                            placeholder = { Text(stringResource(R.string.email_placeholder)) },
                            singleLine = true,
                            isError = showError && (email.isBlank() || invalidEmail),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (invalidEmail) {
                            Text(
                                text = stringResource(R.string.valid_email),
                                color = Red19,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else {
                            Spacer(Modifier.height(29.dp))
                        }
                    }

                    if (showEmailOrUsername) {
                        OutlinedTextField(
                            value = emailOrUsername,
                            onValueChange = { emailOrUsername = it },
                            label = { Text(stringResource(R.string.emailOrUsername_placeholder)) },
                            placeholder = { Text(stringResource(R.string.emailOrUsername_email)) },
                            singleLine = true,
                            isError = showError && (emailOrUsername.isBlank()),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (showError && emailOrUsername.isNotBlank()) {
                            Text(
                                text = stringResource(R.string.invalid_credentials),
                                color = Red19,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    if (showPassword) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(stringResource(R.string.password_label)) },
                            placeholder = { Text(stringResource(R.string.password_placeholder)) },
                            singleLine = true,
                            isError = showError && password.isBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }

                    if (showConfirmPassword) {
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text(stringResource(R.string.confirm_password_label)) },
                            placeholder = { Text(stringResource(R.string.confirm_password_placeholder)) },
                            singleLine = true,
                            isError = showError && (confirmPassword.isBlank() || passwordMismatch),
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation()
                        )

                        if (passwordMismatch) {
                            Text(
                                text = stringResource(R.string.do_not_match),
                                color = Red19,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedButton(
                    onClick = {
                        showError = (showEmail && email.isBlank()) ||
                                (showEmailOrUsername && emailOrUsername.isBlank()) ||
                                (showPassword && password.isBlank()) ||
                                (showName && fullName.isBlank()) ||
                                (showSurname && username.isBlank()) ||
                                (showConfirmPassword && confirmPassword.isBlank())

                        invalidEmail = showEmail && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        passwordMismatch = showConfirmPassword && password != confirmPassword

                        scope.launch {
                            if (showConfirmPassword) {
                                if (passwordMismatch || invalidEmail) {
                                    showError = true
                                    return@launch;
                                }

                                val userDao = AppDatabase.get(ctx).userDao()
                                val alarmDao = AppDatabase.get(ctx).alarmDao()
                                val userVibrationDao = AppDatabase.get(ctx).userVibrationDao()
                                val userSoundDao = AppDatabase.get(ctx).userSoundDao()


                                val conflicts = userDao.checkDetailsAvailable(email, username)
                                val isAvailable = (conflicts == 0)

                                if (!isAvailable) {
                                    showError = true
                                    return@launch
                                }

                                val newUser = UserEntity(
                                    0,
                                    fullName,
                                    username,
                                    email,
                                    password,
                                    ProfilePictureEnum.Man1
                                )
                                val userID = userDao.insert(newUser).toInt()


                                userVibrationDao.insert(UserVibrationEntity(
                                        userID,
                                        29
                                    )
                                )

                                userSoundDao.insert( UserSoundEntity(
                                        userID,
                                        2
                                    )
                                )

                                alarmDao.insert(AlarmEntity(
                                    userID,
                                    hour = 12,
                                    minute = 0,
                                    soundId = 2,
                                    vibrationId = 29,
                                    daysMask = 0,
                                    challenge = ChallengeEnum.Math,
                                    enabled = true
                                ))


                                onContinue(userID)
                            }
                            else {
                                if (!showError && !invalidEmail && !passwordMismatch) {

                                    val userDao = AppDatabase.get(ctx).userDao()

                                    val user = if (showEmailOrUsername) {
                                        userDao.findByCredentials(emailOrUsername, password)
                                    } else {
                                        userDao.findByCredentials(email, password)
                                    }

                                    if (user != null) {
                                        onContinue(user.id)
                                    } else {
                                        showError = true
                                    }
                                }
                            }
                        }
                    }
                    ,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = GrayColor,
                        contentColor = WhiteColor,
                        disabledContainerColor = GrayColor,
                        disabledContentColor = SecondaryColor
                    ),
                    border = BorderStroke(1.dp, BlackColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    Text(
                        text = stringResource(R.string.continue_key),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                        color = SecondaryColor,
                    )
                }
            }
        }
    }
}




@Composable
fun AuthenticationScreen(
    iconResId: Int,
    onAuthenticated: (Int) -> Unit,
    onGoogleClick: () -> Unit,
    onRequireAvatarSelection: () -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var activeForm by remember { mutableStateOf<String?>(null) }
    var showButton by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(screenWidth * 0.12f)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )

    {
        if (activeForm == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = screenWidth * 0.08f)
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Logo",
                    modifier = Modifier.size(screenWidth * 0.6f)
                )

                Text(
                    text = stringResource(R.string.welcome),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = screenWidth * 0.05f)
                )

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(top = screenWidth * 0.01f, bottom = screenWidth * 0.05f)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = screenWidth * 0.02f),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(screenWidth * 0.3f)
                            .padding(end = screenWidth * 0.03f)
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = (screenWidth.value * 0.07f).sp,
                    )
                }
            }
        }


        MorphingAuthBox(
            label = stringResource(R.string.signup),
            isActive = activeForm == "SIGN UP",
            onClick = { activeForm = "SIGN UP" },
            onContinue = { userId ->
                onAuthenticated(userId)
                onRequireAvatarSelection()
            },
            showName = true,
            showSurname = true,
            showEmail = true,
            showPassword = true,
            showConfirmPassword = true,
            height = 500
        )

        Spacer(modifier = Modifier.height(16.dp))

        MorphingAuthBox(
            label = stringResource(R.string.signin),
            isActive = activeForm == "SIGN IN",
            onClick = { activeForm = "SIGN IN" },
            onContinue = onAuthenticated,
            showEmailOrUsername = true,
            showPassword = true,
            height = 300
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = GrayColor
            )

            Text(
                text = stringResource(R.string.or_signup),
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = GrayColor
            )
        }

        AuthButton(stringResource(R.string.gmail), onClick = onGoogleClick)

    }
}

