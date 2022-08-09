package dev.nyon.roler

import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Role

data class RoleEntry(val id: Snowflake, val emoji: DiscordPartialEmoji, val emojiRaw: String) {

    suspend fun getRole(): Role = guild.getRole(id)

}