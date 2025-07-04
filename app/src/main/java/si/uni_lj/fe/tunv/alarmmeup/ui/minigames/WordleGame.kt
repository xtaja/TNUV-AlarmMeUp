package si.uni_lj.fe.tunv.alarmmeup.ui.minigames

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.WinScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ChallengeEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ExitButton
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.GrayColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.Green19
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.MainColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.Yellow19

private val KEYBOARD_ROWS = listOf(
    "QWERTYUIOP",
    "ASDFGHJKL",
    "ZXCVBNM"
)

enum class LetterState { CORRECT, PRESENT, ABSENT, UNSET }

data class LetterBox(val letter: Char?, val state: LetterState = LetterState.UNSET)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WordleGame(
    onExit: (challengeEnum: ChallengeEnum, success: Boolean, fromWinScreen: Boolean) -> Unit,
    sessionRepo: SessionRepo
) {
    val numOfXP = 50
    val numOfSunCoins = 10
    val maxGuesses = 6
    val wordLength = 5

    val context = LocalContext.current
    val inputStream = context.resources.openRawResource(R.raw.words5)
    val words = inputStream.bufferedReader().readLines()
        .map { it.trim().uppercase() }

    var resetKey by remember { mutableStateOf(0) }
    val answer by remember(resetKey) { mutableStateOf(words.random()) }

    var guesses by remember(resetKey) { mutableStateOf(List(maxGuesses) { List(wordLength) { LetterBox(null) } }) }
    var currentGuess by remember(resetKey) { mutableStateOf("") }
    var currentRow by remember(resetKey) { mutableStateOf(0) }
    var isFinished by remember(resetKey) { mutableStateOf(false) }
    var isWin by remember(resetKey) { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showInvalidWord by remember { mutableStateOf(false) }
    var keyboardState by remember(resetKey) { mutableStateOf(mutableMapOf<Char, LetterState>()) }
    var showTryAgain by remember { mutableStateOf(false) }

    LaunchedEffect(isFinished) {
        if (isFinished && isWin) {
            sessionRepo.addXPAndCoins(numOfXP, numOfSunCoins)
            sessionRepo.setGameCompletedToday()
        }
        if (isFinished && !isWin) {
            showTryAgain = true
        }
    }

    fun checkGuess(guess: String): List<LetterBox> {
        val answerChars = answer.toCharArray()
        val guessChars = guess.toCharArray()
        val result = MutableList(wordLength) { LetterBox(guessChars[it], LetterState.ABSENT) }
        val used = BooleanArray(wordLength)

        //first pass: correct position
        for (i in 0 until wordLength) {
            if (guessChars[i] == answerChars[i]) {
                result[i] = LetterBox(guessChars[i], LetterState.CORRECT)
                used[i] = true
            }
        }
        //second pass: present but not correct position
        for (i in 0 until wordLength) {
            if (result[i].state == LetterState.CORRECT) continue
            for (j in 0 until wordLength) {
                if (!used[j] && guessChars[i] == answerChars[j]) {
                    result[i] = LetterBox(guessChars[i], LetterState.PRESENT)
                    used[j] = true
                    break
                }
            }
        }
        return result
    }

    fun updateKeyboard(guessResult: List<LetterBox>) {
        for (box in guessResult) {
            val prev = keyboardState[box.letter!!] ?: LetterState.UNSET
            val new = when (box.state) {
                LetterState.CORRECT -> LetterState.CORRECT
                LetterState.PRESENT -> if (prev != LetterState.CORRECT) LetterState.PRESENT else prev
                LetterState.ABSENT -> if (prev == LetterState.UNSET) LetterState.ABSENT else prev
                else -> prev
            }
            keyboardState[box.letter] = new
        }
    }

    if (isFinished) {
        if (isWin) {
            WinScreen(
                repo = sessionRepo,
                numOfXP = numOfXP,
                numOfSunCoins = numOfSunCoins,
                onCollect = onExit,
                gameEnum = ChallengeEnum.Wordle
            )
        }
    }
    if (!isFinished || (isFinished && !isWin)) {
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { stringResource(R.string.exit_game_question) },
                text = { stringResource(R.string.exit_game_text) },
                confirmButton = {
                    TextButton(onClick = { showExitDialog = false; onExit(ChallengeEnum.Wordle, true, false)})
                    { stringResource(R.string.exit) }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false })
                    { stringResource(R.string.cancel) }
                }
            )
        }
        if (showInvalidWord) {
            AlertDialog(
                onDismissRequest = { showInvalidWord = false },
                title = { stringResource(R.string.invalid_word) },
                text = { stringResource(R.string.five_letter_word) },
                confirmButton = {
                    TextButton(onClick = { showInvalidWord = false })
                    { stringResource(R.string.OK) }
                },
                dismissButton = {}
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            ExitButton(onExit = { showExitDialog = true })
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(stringResource(R.string.wordle), fontSize = 45.sp)
                Spacer(modifier = Modifier.height(50.dp))
                // Board
                for (row in 0 until maxGuesses) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        for (col in 0 until wordLength) {
                            val box = when {
                                row < currentRow -> guesses[row][col]
                                row == currentRow && col < currentGuess.length ->
                                    LetterBox(currentGuess[col], LetterState.UNSET)
                                else -> LetterBox(null, LetterState.UNSET)
                            }
                            val color = when (box.state) {
                                LetterState.CORRECT -> Green19
                                LetterState.PRESENT -> Yellow19
                                LetterState.ABSENT -> GrayColor
                                else -> WhiteColor
                            }
                            Surface(
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(3.dp)
                                    .shadow(8.dp, RoundedCornerShape(6.dp)),
                                color = color,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = box.letter?.toString() ?: "",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (box.state == LetterState.UNSET) BlackColor else WhiteColor
                                    )
                                }
                            }
                        }
                    }
                }
                //failure UI
                if (isFinished && !isWin) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Text("The word was: $answer", fontSize = 18.sp, color = BlackColor)
                    Spacer(modifier = Modifier.height(24.dp))
                    if (showTryAgain) {
                        Button(
                            onClick = {
                                resetKey++
                                showTryAgain = false
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WhiteColor,
                                contentColor = BlackColor
                            ),
                            modifier = Modifier.shadow(10.dp, RoundedCornerShape(16.dp))
                        ) {
                            Text("TRY AGAIN", fontSize = 22.sp)
                        }
                    }
                } else {
                    //Keyboard
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(100.dp))
                        for (row in KEYBOARD_ROWS) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                for (key in row) {
                                    val state = keyboardState[key] ?: LetterState.UNSET
                                    val color = when (state) {
                                        LetterState.CORRECT -> Green19
                                        LetterState.PRESENT -> Yellow19
                                        LetterState.ABSENT -> GrayColor

                                        else -> WhiteColor
                                    }
                                    Surface(
                                        modifier = Modifier
                                            .padding(1.dp)
                                            .size(36.dp)
                                            .shadow(4.dp, RoundedCornerShape(6.dp))
                                            .let {
                                                if (!isFinished && currentGuess.length < wordLength) {
                                                    it.then(Modifier
                                                        .clickable {
                                                            if (!isFinished && currentGuess.length < wordLength) {
                                                                currentGuess += key
                                                            }
                                                        }
                                                    )
                                                } else it
                                            },
                                        color = color,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Text(
                                                key.toString(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = if (state == LetterState.UNSET) BlackColor else WhiteColor
                                            )
                                        }
                                    }
                                }
                                if (row == "ZXCVBNM") {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        modifier = Modifier
                                            .padding(1.dp)
                                            .size(54.dp, 36.dp)
                                            .shadow(4.dp, RoundedCornerShape(6.dp)),
                                        color = MainColor,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Button(
                                                onClick = {
                                                    if (currentGuess.isNotEmpty() && !isFinished) {
                                                        currentGuess = currentGuess.dropLast(1)
                                                    }
                                                },
                                                enabled = !isFinished && currentGuess.isNotEmpty(),
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .size(54.dp, 36.dp)
                                                    .padding(1.dp),
                                                contentPadding = PaddingValues(0.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent)
                                            ) {
                                                Text("⌫", fontWeight = FontWeight.Bold, fontSize = 18.sp,color = WhiteColor)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        modifier = Modifier
                                            .padding(1.dp)
                                            .size(54.dp, 36.dp)
                                            .shadow(4.dp, RoundedCornerShape(6.dp)),
                                        color = MainColor,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Button(
                                                onClick = {
                                                    if (currentGuess.length == wordLength && !isFinished) {
                                                        val guessUpper = currentGuess.uppercase()
                                                        if (words.contains(guessUpper)) {
                                                            val guessResult = checkGuess(guessUpper)
                                                            val newGuesses = guesses.toMutableList()
                                                            newGuesses[currentRow] = guessResult
                                                            guesses = newGuesses
                                                            updateKeyboard(guessResult)
                                                            if (guessUpper == answer) {
                                                                isWin = true
                                                                isFinished = true
                                                            } else if (currentRow == maxGuesses - 1) {
                                                                isWin = false
                                                                isFinished = true
                                                            } else {
                                                                currentRow += 1
                                                                currentGuess = ""
                                                            }
                                                        } else {
                                                            showInvalidWord = true
                                                        }
                                                    }
                                                },
                                                enabled = !isFinished && currentGuess.length == wordLength,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .size(54.dp, 36.dp)
                                                    .padding(1.dp),
                                                contentPadding = PaddingValues(0.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor =Color.Transparent )
                                            ) {
                                                Text("ENTER", fontWeight = FontWeight.Bold, fontSize = 14.sp,color = WhiteColor)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
