package dev.nyon.roler

import dev.kord.common.entity.Snowflake
import kotlinx.serialization.Serializable

@Serializable
data class RoleEntry(val id: Snowflake, val emoji: String)

suspend fun RoleEntry.getRole() = guild.getRole(id)