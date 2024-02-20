package com.example.piterbridges.presentation.bridges.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ONE_HOUR = 60 * 60 * 1000

class Bridge(
    val id: Int,
    val title: String,
    val titleEng: String,
    val description: String,
    val descriptionEng: String,
    val divorces: List<Divorces>,
    val lat: Double,
    val lng: Double,
    val photoCloseUrl: String,
    val photoOpenUrl: String,
    val public: Boolean,
)

fun stateBridge(divorces: List<Divorces>): Int {
    val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentDate = Date()
    val timeText: String = timeFormat.format(currentDate)
    val timeDate: Date? = timeFormat.parse(timeText)
    val currentDatePlusHour = System.currentTimeMillis() + ONE_HOUR
    val timeTextPlusHour: String = timeFormat.format(currentDatePlusHour)
    val timeDatePlusHour: Date? = timeFormat.parse(timeTextPlusHour)
    var stateBridge = 0

    for (position in divorces) {
        if (timeDate != null && timeDatePlusHour != null) {
            val bridgeStartTime: Date? = timeFormat.parse(position.startTime)
            val bridgeEndTime: Date? = timeFormat.parse(position.endTime)
            if (timeDate.before(bridgeStartTime) || (timeDate.after(bridgeEndTime))) {
                if (timeDatePlusHour.after(bridgeStartTime) && (timeDate.before(
                        bridgeEndTime
                    ))
                ) {
                    stateBridge = 0
                    break
                } else {
                    stateBridge = 1
                }
            } else {
                stateBridge = 2
                break
            }
        }
    }
    return stateBridge
}
