package dev.nyon.roler.commands

import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.nyon.roler.RoleEntry
import dev.nyon.roler.kord
import dev.nyon.roler.roleEntries
import dev.nyon.roler.utils.roleCollection
import org.litote.kmongo.eq

val roleManageCommandHandler = kord.on<GuildChatInputCommandInteractionCreateEvent> {
    val response = this.interaction.deferEphemeralResponse()
    when (this.interaction.invokedCommandName) {
        "removerole" -> {
            val role = this.interaction.command.roles["role"]!!
            if (roleEntries.none { it.id == role.id }) {
                response.respond {
                    content = "The role cannot be found in the database!"
                }
                return@on
            }
            roleEntries.removeIf { it.id == role.id }
            roleCollection.deleteMany(RoleEntry::id eq role.id)
            response.respond {
                content =
                    "Successfully removed role ${role.name} from the database. \nTo update the role message please trigger the message command!"
            }
        }

        "addrole" -> {
            val role = this.interaction.command.roles["role"]!!
            val emoji = this.interaction.command.strings["emoji"]!!

            if (roleEntries.any { it.id == role.id }) {
                response.respond {
                    content = "The role is already in the database!"
                }
                return@on
            }

            val roleEntry = RoleEntry(role.id, emoji)
            roleEntries += roleEntry
            roleCollection.insertOne(roleEntry)
            response.respond {
                content =
                    "The role ${role.name} was added to the database. \nTo update the role message please trigger the message command!"
            }
        }
    }
}