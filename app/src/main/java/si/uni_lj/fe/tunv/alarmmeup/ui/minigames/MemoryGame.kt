package si.uni_lj.fe.tunv.alarmmeup.ui.minigames
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import si.uni_lj.fe.tunv.alarmmeup.R
import si.uni_lj.fe.tunv.alarmmeup.ui.WinScreen
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ChallengeEnum
import si.uni_lj.fe.tunv.alarmmeup.ui.components.ExitButton
import si.uni_lj.fe.tunv.alarmmeup.ui.data.SessionRepo
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.MainColor
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.WhiteColor

data class MemoryCard(val id: Int, val symbol: String, var isRevealed: Boolean = false, var isMatched: Boolean = false)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MemoryGame(
    onExit: (challengeEnum: ChallengeEnum, success: Boolean, fromWinScreen: Boolean) -> Unit,
    sessionRepo: SessionRepo
) {
    var numOfXP = 50
    var numOfSunCoins = 10
    var isFinished by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }


    val symbols = listOf("🥝", "🍌", "🍇", "🍉", "🍒", "🍋", "🍓", "🍑")
    var cards by remember { mutableStateOf((symbols + symbols).shuffled().mapIndexed { i, s -> MemoryCard(i, s) }) }
    var selectedIndices by remember { mutableStateOf(listOf<Int>()) }
    var matches by remember { mutableStateOf(0) }


    LaunchedEffect(selectedIndices) {
        if (selectedIndices.size == 2) {
            val first = cards[selectedIndices[0]]
            val second = cards[selectedIndices[1]]
            if (first.symbol == second.symbol) {
                cards = cards.map {
                    if (it.id == first.id || it.id == second.id) it.copy(isMatched = true)
                    else it
                }
                matches += 1
            } else {
                delay(800)
                cards = cards.map {
                    if (it.id == first.id || it.id == second.id) it.copy(isRevealed = false)
                    else it
                }
            }
            selectedIndices = listOf()
        }
    }

    LaunchedEffect(matches) {
        if (matches == symbols.size) {
            isFinished = true
        }
    }

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
                gameEnum = ChallengeEnum.Memory
        )
    } else {
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { stringResource(R.string.exit_game_question) },
                text = { stringResource(R.string.exit_game_text) },
                confirmButton = {
                    TextButton(onClick = { showExitDialog = false; onExit(ChallengeEnum.Memory, true, false) })
                    { stringResource(R.string.exit) }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false })
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(stringResource(R.string.memory), fontSize = 45.sp)
                Spacer(modifier = Modifier.height(100.dp))
                Text(stringResource(R.string.find_pairs), fontSize = 20.sp)
                Spacer(modifier = Modifier.height(50.dp))
                MemoryGrid(
                    cards = cards,
                    onCardClick = { idx ->
                        if (!cards[idx].isRevealed && !cards[idx].isMatched && selectedIndices.size < 2) {
                            cards = cards.mapIndexed { i, card ->
                                if (i == idx) card.copy(isRevealed = true) else card
                            }
                            selectedIndices = selectedIndices + idx
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MemoryGrid(cards: List<MemoryCard>, onCardClick: (Int) -> Unit) {
    val columns = 4
    Column {
        cards.chunked(columns).forEach { row ->
            Row {
                row.forEach { card ->
                    val cardColor = when {
                        card.isMatched -> WhiteColor
                        card.isRevealed -> WhiteColor
                        else -> MainColor
                    }
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(60.dp)
                            .shadow(8.dp, RoundedCornerShape(8.dp))
                            .background(
                                cardColor,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable(enabled = !card.isRevealed && !card.isMatched) { onCardClick(card.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (card.isRevealed || card.isMatched) card.symbol else "?",
                            fontSize = 28.sp,
                            color = WhiteColor

                        )
                    }
                }
            }
        }
    }
}

