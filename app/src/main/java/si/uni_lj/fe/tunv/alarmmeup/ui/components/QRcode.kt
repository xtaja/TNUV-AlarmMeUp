package si.uni_lj.fe.tunv.alarmmeup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import si.uni_lj.fe.tunv.alarmmeup.ui.utils.generateQrCodeBitmapWithCorners

@Composable
fun QrCodeView(content: String, size: Int = 512, QRcolor: Color = Color.Black) {
    val qrBitmap = generateQrCodeBitmapWithCorners(content,
        size = size,
        color = QRcolor.toArgb(),

    )
    Image(bitmap = qrBitmap, contentDescription = "QR Code")
}