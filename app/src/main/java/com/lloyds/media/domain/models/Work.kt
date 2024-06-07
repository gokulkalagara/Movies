package com.lloyds.media.domain.models

import com.lloyds.media.ui.viewmodels.models.Message

/**
 * @Author: Gokul Kalagara
 *
 */
sealed class Work<out T> {
    data class Result<T>(val data: T, val message: Message? = null) : Work<T>()
    data class Stop(val message: Message) : Work<Nothing>()
    data class Backfire(val exception: Exception) : Work<Nothing>()
    object Completed : Work<Nothing>()
    object Cancelled : Work<Nothing>()
    object Suspended : Work<Nothing>()
    object Busy : Work<Nothing>()

    companion object {
        fun <T> result(data: T, message: Message? = null): Result<T> = Result(data, message)
        fun stop(message: Message) = Stop(message)
        fun backfire(exception: Exception) = Backfire(exception)
    }
}

