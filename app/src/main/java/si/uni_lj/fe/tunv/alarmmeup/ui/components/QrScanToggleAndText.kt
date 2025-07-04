package si.uni_lj.fe.tunv.alarmmeup.ui.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import si.uni_lj.fe.tunv.alarmmeup.ui.theme.BlackColor
import androidx.compose.ui.res.stringResource
import si.uni_lj.fe.tunv.alarmmeup.R


@Composable
fun QrScanToggleAndText(
    isScan: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ScanQRCodeToggle(
            isScan = isScan,
            onToggle = onToggle,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.qr_scan_friends),
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            color = BlackColor
        )
    }
}