package si.uni_lj.fe.tunv.alarmmeup.ui.minigames

import android.os.Build
import android.content.Context
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.WinScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ChallengeEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ExitButton
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TypingGame(
    onExit: (challengeEnum: ChallengeEnum, success: Boolean, fromWinScreen: Boolean) -> Unit,
    sessionRepo: SessionRepo
) {
    var numOfXP = 50
    var numOfSunCoins = 10
    var isFinished by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    LaunchedEffect(isFinished) {
        if (isFinished) {
            sessionRepo.addXPAndCoins(numOfXP, numOfSunCoins)
            sessionRepo.setGameCompletedToday()
        }
    }
    if (isFinished) {
        WinScreen(
            repo = sessionRepo,
            numOfXP = numOfXP,
            numOfSunCoins = numOfSunCoins,
            onCollect = onExit,
                gameEnum = ChallengeEnum.Typing
        )
    } else {
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { stringResource(R.string.exit_game_question) },
                text = { stringResource(R.string.exit_game_text) },
                confirmButton = { TextButton(onClick = { showExitDialog = false; onExit(ChallengeEnum.Typing, true, false) })
                { stringResource(R.string.exit) }
                },
                dismissButton = { TextButton(onClick = { showExitDialog = false })
                { stringResource(R.string.cancel)}
                }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            ExitButton(onExit = { showExitDialog = true })
        }

        TypingGameUI(onFinished = { isFinished = true })
    }
}

@Composable
fun TypingGameUI(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sentences by remember {
        mutableStateOf(loadSentencesFromRaw(context))
    }
    var target by remember { mutableStateOf(sentences.random()) }
    var userInput by remember { mutableStateOf(TextFieldValue("")) }

    fun resetSentence() {
        target = (sentences - target).let { if (it.isEmpty()) sentences else it }.random()
        userInput = TextFieldValue("")
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.find_sentences), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(target, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Box(Modifier.padding(16.dp)) {
                    BasicTextField(
                        value = userInput,
                        onValueChange = { newValue ->
                            val text = newValue.text
                            if (target.startsWith(text)) {
                                userInput = newValue
                                if (text == target) onFinished()
                            } else {
                                resetSentence()
                            }
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.fillMaxSize()
                    )
                    if (userInput.text.isEmpty()) {
                        Text(
                            stringResource(R.string.start_typing),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            if (userInput.text.isNotEmpty() && userInput.text != target) {
                Text(
                    stringResource(R.string.keep_typing),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Black)
                )
            }
        }
    }
}

private fun loadSentencesFromRaw(context: Context): List<String> {
    val inputStream = context.resources.openRawResource(R.raw.typing_sentences)
    return inputStream.bufferedReader().useLines { seq -> seq.filter { it.isNotBlank() }.toList() }
}