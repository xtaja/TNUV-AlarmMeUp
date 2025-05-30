package si.uni_lj.fe.tunv.alarmmeup.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import kotlinx.coroutines.launch
import si.uni_lj.fe.tunv.alarmmeup.ui.components.Day
import si.uni_lj.fe.tunv.alarmmeup.ui.components.DayStatus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// Data class definition
data class DateRangeHighlight(
    val start: LocalDate,
    val end: LocalDate,
    val color: Color,
    val borderColor: Color,
    @DrawableRes val icon: Int? = null, // Use @DrawableRes for type safety
    val iconColor: Color = Color.Black
)

// --- Configurable Styling Constants ---
private val CALENDAR_OUTER_EDGE_MARGIN: Dp = 8.dp
private val DAY_CELL_VERTICAL_INNER_MARGIN: Dp = 2.dp
private val DAY_CELL_HORIZONTAL_INNER_MARGIN: Dp = 4.dp
private val DAY_CELL_DEFAULT_CORNER_RADIUS: Dp = 6.dp
private val DAY_CELL_RANGE_BORDER_THICKNESS: Dp = 1.5.dp
private val DAY_CELL_DEFAULT_BORDER_THICKNESS: Dp = 1.dp
private val DAY_CELL_ICON_SIZE: Dp = 12.dp
private val DAY_CELL_TEXT_SIZE: TextUnit = 14.sp
private val RANGE_TEXT_COLOR: Color = Color.Black
private val DEFAULT_ICON_COLOR: Color = Color.Black

// --- NEW: Styling for Larger Streak Display ---
private val STREAK_ICON_SIZE: Dp = 36.dp // Increased icon size
private val STREAK_TEXT_SIZE: TextUnit = 22.sp // Increased text size
private val STREAK_SPACER_WIDTH: Dp = 10.dp // Increased spacer
private val STREAK_ROW_VERTICAL_PADDING: Dp = 12.dp // Increased row padding


@RequiresApi(Build.VERSION_CODES.O)
val alarmData: List<Day> = listOf(
    Day(date = LocalDate.of(2025, Month.MAY, 1), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.MAY, 2), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.MAY, 3), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.MAY, 5), status = DayStatus.MISSED),
    Day(date = LocalDate.of(2025, Month.MAY, 10), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.MAY, 11), status = DayStatus.MISSED),
    Day(date = LocalDate.of(2025, Month.MAY, 12), status = DayStatus.MISSED),
    Day(date = LocalDate.of(2025, Month.MAY, 15), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.MAY, 20), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.MAY, 22), status = DayStatus.MISSED),
    Day(date = LocalDate.of(2025, Month.MAY, 25), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.MAY, 28), status = DayStatus.MISSED),
    Day(date = LocalDate.of(2025, Month.MAY, 30), status = DayStatus.MISSED),
    Day(date = LocalDate.of(2025, Month.MAY, 31), status = DayStatus.COMPLETED),
    Day(date = LocalDate.of(2025, Month.JUNE, 3), status = DayStatus.COMPLETED)
)


@RequiresApi(Build.VERSION_CODES.O)
fun CreateRanges(
    daysInput: List<Day>,
    @DrawableRes checkIconId: Int?,
    @DrawableRes snoozeIconId: Int?,
    @DrawableRes closeIconId: Int?
): List<DateRangeHighlight> {
    if (daysInput.isEmpty()) {
        return emptyList()
    }
    val days = daysInput.filter { it.date != null }.sortedBy { it.date }
    if (days.isEmpty()) {
        return emptyList()
    }

    val rangeList: MutableList<DateRangeHighlight> = mutableListOf()
    val calendarStartDate = LocalDate.now().minusMonths(12)
    val calendarEndDate = LocalDate.now().plusMonths(12)
    val defaultFillerColor = Color.LightGray
    val defaultIconColor = Color.Black

    val firstAlarmDate = days.first().date!!
    if (firstAlarmDate.isAfter(calendarStartDate)) {
        val endOfInitialFiller = firstAlarmDate.minusDays(1)
        if (!endOfInitialFiller.isBefore(calendarStartDate)) {
            rangeList.add(
                DateRangeHighlight(
                    start = calendarStartDate,
                    end = endOfInitialFiller,
                    color = Color.White,
                    borderColor = Color.White,
                    icon = closeIconId,
                    iconColor = defaultIconColor
                )
            )
        }
    }

    var currentRangeStartDate = firstAlarmDate
    var currentRangeStatus = days.first().status
    var lastProcessedDate = firstAlarmDate

    for (i in 1 until days.size) {
        val currentDayEntry = days[i]
        val currentDate = currentDayEntry.date!!

        if (currentDate.isAfter(lastProcessedDate.plusDays(1))) {
            rangeList.add(
                DateRangeHighlight(
                    start = currentRangeStartDate,
                    end = lastProcessedDate,
                    color = if (currentRangeStatus == DayStatus.COMPLETED) Color(0xFFFFA500) else defaultFillerColor,
                    borderColor = if (currentRangeStatus == DayStatus.COMPLETED) Color(0xFFFFA500) else defaultFillerColor,
                    icon = if (currentRangeStatus == DayStatus.COMPLETED) checkIconId else snoozeIconId,
                    iconColor = defaultIconColor
                )
            )
            val endOfGapFiller = currentDate.minusDays(1)
            if (!endOfGapFiller.isBefore(lastProcessedDate.plusDays(1))) {
                rangeList.add(
                    DateRangeHighlight(
                        start = lastProcessedDate.plusDays(1),
                        end = endOfGapFiller,
                        color = Color.White,
                        borderColor = Color.White,
                        icon = closeIconId,
                        iconColor = defaultIconColor
                    )
                )
            }
            currentRangeStartDate = currentDate
            currentRangeStatus = currentDayEntry.status
        } else if (currentDayEntry.status != currentRangeStatus) {
            rangeList.add(
                DateRangeHighlight(
                    start = currentRangeStartDate,
                    end = lastProcessedDate,
                    color = if (currentRangeStatus == DayStatus.COMPLETED) Color(0xFFFFA500) else defaultFillerColor,
                    borderColor = if (currentRangeStatus == DayStatus.COMPLETED) Color(0xFFFFA500) else defaultFillerColor,
                    icon = if (currentRangeStatus == DayStatus.COMPLETED) checkIconId else snoozeIconId,
                    iconColor = defaultIconColor
                )
            )
            currentRangeStartDate = currentDate
            currentRangeStatus = currentDayEntry.status
        }
        lastProcessedDate = currentDate
    }

    rangeList.add(
        DateRangeHighlight(
            start = currentRangeStartDate,
            end = lastProcessedDate,
            color = if (currentRangeStatus == DayStatus.COMPLETED) Color(0xFFFFA500) else defaultFillerColor,
            borderColor = if (currentRangeStatus == DayStatus.COMPLETED) Color(0xFFFFA500) else defaultFillerColor,
            icon = if (currentRangeStatus == DayStatus.COMPLETED) checkIconId else snoozeIconId,
            iconColor = defaultIconColor
        )
    )

    val lastAlarmDate = days.last().date!!
    if (lastAlarmDate.isBefore(calendarEndDate)) {
        val startOfFinalFiller = lastAlarmDate.plusDays(1)
        if (!calendarEndDate.isBefore(startOfFinalFiller)) {
            rangeList.add(
                DateRangeHighlight(
                    start = startOfFinalFiller,
                    end = calendarEndDate,
                    color = Color.White,
                    borderColor = Color.White,
                    icon = closeIconId,
                    iconColor = defaultIconColor
                )
            )
        }
    }
    return rangeList.toList()
}


@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StreakScreen(
    @DrawableRes snoozeIconId: Int,
    @DrawableRes fireIconId: Int,
    @DrawableRes checkIconId: Int,
    @DrawableRes closeIconId: Int,
    currentStreak: Int = 24
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(6) }
    val endMonth = remember { currentMonth.plusMonths(3) }
    val firstDayOfWeek = remember { DayOfWeek.MONDAY }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )
    val coroutineScope = rememberCoroutineScope()

    val highlightedRanges = remember(alarmData, snoozeIconId, checkIconId, closeIconId) {
        CreateRanges(alarmData, checkIconId, snoozeIconId, closeIconId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Streak Display Row - MODIFIED FOR LARGER SIZE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = STREAK_ROW_VERTICAL_PADDING, // Increased padding
                    horizontal = CALENDAR_OUTER_EDGE_MARGIN
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (fireIconId != 0 && fireIconId != -1) {
                Icon(
                    painter = painterResource(id = fireIconId),
                    contentDescription = "Current Streak",
                    modifier = Modifier.size(STREAK_ICON_SIZE), // Increased size
                    tint = Color(0xFFFFA500)
                )
                Spacer(modifier = Modifier.size(STREAK_SPACER_WIDTH)) // Increased spacer
            }
            Text(
                text = "$currentStreak Days",
                style = MaterialTheme.typography.headlineSmall.copy( // Using a larger base style
                    fontSize = STREAK_TEXT_SIZE // Explicitly set increased font size
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        CalendarHeader(
            currentMonth = calendarState.firstVisibleMonth.yearMonth,
            onPreviousMonth = { coroutineScope.launch { calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.minusMonths(1)) } },
            onNextMonth = { coroutineScope.launch { calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.plusMonths(1)) } }
        )
        DaysOfWeekHeader(daysOfWeek = remember { daysOfWeek(firstDayOfWeek) })

        HorizontalCalendar(
            state = calendarState,
            modifier = Modifier.padding(horizontal = CALENDAR_OUTER_EDGE_MARGIN),
            dayContent = { day ->
                RangeHighlightDayCell(
                    day = day,
                    highlightedRanges = highlightedRanges,
                    verticalMargin = DAY_CELL_VERTICAL_INNER_MARGIN,
                    horizontalMargin = DAY_CELL_HORIZONTAL_INNER_MARGIN,
                    onDayClick = { calendarDay -> println("Clicked on: ${calendarDay.date}") }
                )
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RangeHighlightDayCell(
    day: CalendarDay,
    highlightedRanges: List<DateRangeHighlight>,
    verticalMargin: Dp,
    horizontalMargin: Dp,
    onDayClick: (CalendarDay) -> Unit
) {
    val currentDayDate = day.date
    val zeroRadius = 0.dp

    var cellShape = RoundedCornerShape(DAY_CELL_DEFAULT_CORNER_RADIUS)
    var cellBackgroundColor = MaterialTheme.colorScheme.surface
    var cellBorderStroke: BorderStroke? = null
    var textColor = MaterialTheme.colorScheme.onSurface
    @DrawableRes var displayIconResId: Int? = null
    var displayIconColor: Color = DEFAULT_ICON_COLOR
    val iconModifier: Modifier = Modifier.size(DAY_CELL_ICON_SIZE)

    val today = LocalDate.now()
    if (day.date == today && highlightedRanges.none { range ->
            currentDayDate >= range.start && currentDayDate <= range.end
        }) {
        textColor = MaterialTheme.colorScheme.primary
    }

    val relevantRange = highlightedRanges.find { range ->
        currentDayDate >= range.start && currentDayDate <= range.end
    }

    var paddingStart = horizontalMargin
    var paddingEnd = horizontalMargin
    val paddingTop = verticalMargin
    val paddingBottom = verticalMargin

    if (relevantRange != null) {
        cellBackgroundColor = relevantRange.color
        cellBorderStroke = BorderStroke(DAY_CELL_RANGE_BORDER_THICKNESS, relevantRange.borderColor)
        textColor = RANGE_TEXT_COLOR

        if (relevantRange.icon != null) {
            displayIconResId = relevantRange.icon
            displayIconColor = relevantRange.iconColor
        }

        val isStartDate = currentDayDate == relevantRange.start
        val isEndDate = currentDayDate == relevantRange.end

        if (!isStartDate) paddingStart = 0.dp
        if (!isEndDate) paddingEnd = 0.dp

        cellShape = RoundedCornerShape(
            topStart = if (isStartDate) DAY_CELL_DEFAULT_CORNER_RADIUS else zeroRadius,
            topEnd = if (isEndDate) DAY_CELL_DEFAULT_CORNER_RADIUS else zeroRadius,
            bottomEnd = if (isEndDate) DAY_CELL_DEFAULT_CORNER_RADIUS else zeroRadius,
            bottomStart = if (isStartDate) DAY_CELL_DEFAULT_CORNER_RADIUS else zeroRadius
        )
    } else {
        if (day.position == DayPosition.MonthDate) {
            cellBorderStroke = BorderStroke(
                DAY_CELL_DEFAULT_BORDER_THICKNESS,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(
                start = paddingStart,
                top = paddingTop,
                end = paddingEnd,
                bottom = paddingBottom
            )
    ) {
        if (day.position == DayPosition.MonthDate || relevantRange != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(cellBackgroundColor, cellShape)
                    .then(
                        if (cellBorderStroke != null) Modifier.border(cellBorderStroke, cellShape)
                        else Modifier
                    )
                    .clip(cellShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onDayClick(day) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 1.dp)
                ) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        color = if (day.position == DayPosition.MonthDate || relevantRange != null) textColor
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        fontSize = DAY_CELL_TEXT_SIZE,
                        fontWeight = if (day.date == today && relevantRange == null) FontWeight.Bold else FontWeight.Normal
                    )
                    if (displayIconResId != null) {
                        Icon(
                            painter = painterResource(id = displayIconResId!!),
                            contentDescription = "Range Icon",
                            tint = displayIconColor,
                            modifier = iconModifier.padding(top = 1.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(DAY_CELL_ICON_SIZE + 1.dp))
                    }
                }
            }
        }
    }
}


// --- Helper Composables (CalendarHeader, DaysOfWeekHeader, daysOfWeek) ---
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous Month")
        }
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next Month")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekHeader(daysOfWeek: List<DayOfWeek>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = CALENDAR_OUTER_EDGE_MARGIN)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun daysOfWeek(firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY): List<DayOfWeek> {
    val days = DayOfWeek.entries.toTypedArray()
    val startIndex = days.indexOf(firstDayOfWeek)
    return if (startIndex == -1) days.toList()
    else days.slice(startIndex until days.size) + days.slice(0 until startIndex)
}

