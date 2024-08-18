package com.pokit.common.dto

import java.time.format.DateTimeFormatter

object DateFormatters {
    val DATE_FORMAT_YYYY_MM_DD: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
}
