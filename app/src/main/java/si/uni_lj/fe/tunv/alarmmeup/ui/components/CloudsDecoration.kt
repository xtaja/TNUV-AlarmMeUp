package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.TertiaryColor

@Composable
fun CloudsDecoration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val radiusWidth =width * 0.17f
        val cloudColor= TertiaryColor


        drawCircle(
            color = cloudColor,
            radius = radiusWidth*0.85f,
            center = Offset(width * 0.30f, height * 0.35f)
        )
        drawCircle(
            color = cloudColor,
            radius = radiusWidth*1.1f,
            center = Offset(width * 0.50f, height * 0.33f)
        )
        drawCircle(
            color = cloudColor,
            radius = radiusWidth*0.9f,
            center = Offset(width *0.70f, height * 0.38f)
        )
        drawCircle(
            color = cloudColor,
            radius = radiusWidth*0.7f,
            center = Offset(width * 0.20f, height * 0.51f)
        )
        drawCircle(
            color = cloudColor,
            radius = radiusWidth ,
            center = Offset(width * 0.35f, height * 0.66f)
        )
        drawCircle(
            color = cloudColor,
            radius = radiusWidth,
            center = Offset(width * 0.58f, height * 0.72f)
        )
        drawCircle(
            color = cloudColor,
            radius = radiusWidth*0.9f,
            center = Offset(width * 0.75f, height * 0.56f)
        )
    }
}