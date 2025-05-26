package com.example.damstocktracker.data.mapper

import com.example.damstocktracker.data.remote.dto.IntradayInfoDto
import com.example.damstocktracker.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun IntradayInfoDto.toIntradayInfo(): IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val localDateTime = LocalDateTime.parse(timestamp,formatter)
    return IntradayInfo(
        date = localDateTime,
        close = close
    )
}
