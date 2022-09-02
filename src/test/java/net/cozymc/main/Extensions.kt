package net.cozymc.main

import be.seeseemelk.mockbukkit.entity.PlayerMock

fun PlayerMock.getMessages(): List<String> {
    val messages = mutableListOf<String>()
    var message: String? = nextMessage()
    while (message != null) {
        messages.add(message)
        message = nextMessage()
    }
    return messages
}