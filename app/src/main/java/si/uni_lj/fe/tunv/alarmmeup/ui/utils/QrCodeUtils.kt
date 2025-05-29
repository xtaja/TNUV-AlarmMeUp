package si.uni_lj.fe.tunv.alarmmeup.ui.utils
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

fun generateQrCodeBitmap(content: String, size: Int = 512): ImageBitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bmp.asImageBitmap()
}

fun generateQrCodeBitmapWithCorners(
    content: String,
    size: Int = 512,
    cornerLength: Int = size / 5,
    cornerThickness: Int = size / 15,
    color: Int = Color.BLACK
): ImageBitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) color else Color.TRANSPARENT)
        }
    }

    val canvas = Canvas(bmp)
    val paint = Paint().apply {
        this.color = color
        strokeWidth = cornerThickness.toFloat()
        style = Paint.Style.STROKE
        isAntiAlias = true
        // strokeCap = Paint.Cap.ROUND
    }

    val arcRadius = cornerLength.toFloat()
    val arcRect = RectF(0f, 0f, 2 * arcRadius, 2 * arcRadius)

    // Top-left corner
    canvas.drawArc(arcRect, 180f, 90f, false, paint)
    // Top-right corner
    arcRect.offsetTo(size - 2 * arcRadius, 0f)
    canvas.drawArc(arcRect, 270f, 90f, false, paint)
    // Bottom-left corner
    arcRect.offsetTo(0f, size - 2 * arcRadius)
    canvas.drawArc(arcRect, 90f, 90f, false, paint)
    // Bottom-right corner
    arcRect.offsetTo(size - 2 * arcRadius, size - 2 * arcRadius)
    canvas.drawArc(arcRect, 0f, 90f, false, paint)
    return bmp.asImageBitmap()
}