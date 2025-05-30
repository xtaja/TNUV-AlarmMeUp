package si.uni_lj.fe.tunv.alarmmeup.ui

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun LoadingScreen(layoutResId: Int, onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(Random.nextLong(1500, 2500))
        onFinished()
    }

    AndroidView(factory = { context ->
        LayoutInflater.from(context).inflate(layoutResId, null)
    })
}
