package com.lloyds.media.infra.database.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * @Author: Gokul Kalagara
 *
 */

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun toTimestamp(date: Date?): Long {
        return date?.time ?: 0L
    }
}