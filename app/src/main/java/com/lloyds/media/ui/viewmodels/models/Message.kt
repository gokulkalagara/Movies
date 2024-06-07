package com.lloyds.media.ui.viewmodels.models

/**
 * @Author: Gokul Kalagara
 * copyright (c) 2024, All rights reserved.
 *
 */
data class Message(
    val messageType: MessageType = MessageType.TOAST,
    val messageStatus: MessageStatus = MessageStatus.NORMAL,
    val message: String
)

enum class MessageStatus {
    POSITIVE,
    NEGATIVE,
    NORMAL
}

enum class MessageType {
    LOG, TOAST, SNACK_BAR, ALERT
}