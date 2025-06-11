package si.uni_lj.fe.tunv.alarmmeup.ui.minigames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.WinScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ChallengeEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ExitButton
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MathGame(
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
                gameEnum = ChallengeEnum.Math
            )
    } else {
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { stringResource(R.string.exit_game_question) },
                text = { stringResource(R.string.exit_game_text) },
                confirmButton = { TextButton(onClick = { showExitDialog = false; onExit(ChallengeEnum.Math, true, false) })
                { stringResource(R.string.exit) }
                },
                dismissButton = { TextButton(onClick = { showExitDialog = false })
                { stringResource(R.string.cancel) }
                }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            ExitButton(onExit = { showExitDialog = true })
        }

        MathGameUI(isFinished = { isFinished = true })

    }
}


@Composable
fun MathGameUI(
    isFinished : () -> Unit
) {
    var operand1 by remember { mutableStateOf(0) }
    var operand2 by remember { mutableStateOf(0) }
    var operator by remember { mutableStateOf('+') }
    var options by remember { mutableStateOf(listOf<Int>()) }

    fun generateQuestion() {
        operand1 = Random.nextInt(1, 11)
        operand2 = Random.nextInt(1, 11)
        operator = listOf('+', '-', '×', '÷').random()
        val answer = when (operator) {
            '+' -> operand1 + operand2
            '-' -> operand1 - operand2
            '×' -> operand1 * operand2
            '÷' -> operand1 / operand2
            else -> 0
        }

        val distractors = mutableSetOf<Int>()
        while (distractors.size < 3) {
            val delta = Random.nextInt(-5, 6)
            val opt = answer + delta
            if (opt != answer) distractors.add(opt)
        }
        options = (distractors + answer).shuffled()

    }

    LaunchedEffect(Unit) { generateQuestion() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "What is $operand1 $operator $operand2?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                for (row in options.chunked(2)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        for (opt in row) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        val correct = when (operator) {
                                            '+' -> operand1 + operand2
                                            '-' -> operand1 - operand2
                                            '×' -> operand1 * operand2
                                            '÷' -> operand1 / operand2
                                            else -> 0
                                        }
                                        if (opt == correct) {
                                            isFinished()
                                        } else {
                                            generateQuestion()
                                        }
                                    }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = opt.toString(),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

