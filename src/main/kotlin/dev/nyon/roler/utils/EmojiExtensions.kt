package dev.nyon.roler.utils

import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.optional.OptionalBoolean

fun String.discordPartialEmoji() = if (!this.startsWith("<")) DiscordPartialEmoji(name = this) else {
    val customSplitted = this.split(":")
    DiscordPartialEmoji(
        customSplitted[2].removeSuffix(">").snowflake,
        customSplitted[1],
        OptionalBoolean.Value(customSplitted[0] == "<a")
    )
}