package com.kolee.composepedometer2.common

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtil {

    // Today's Epoch Day, where day 0 is 1970-01-01
    fun getToday(): Long = LocalDate.now().toEpochDay()

    fun formattedCurrentTime(): String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, yy-MM-dd"))

    fun timeOfClock(): String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("a h:mm:ss").withLocale(Locale.US))

    fun dateOfClock(): String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM, yyyy").withLocale(Locale.US))

}