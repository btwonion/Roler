package dev.nyon.roler.commands

import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.GuildMessageCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.actionRow
import dev.kord.rest.builder.message.modify.embed
import dev.nyon.roler.getRole
import dev.nyon.roler.guild
import dev.nyon.roler.kord
import dev.nyon.roler.roleEntries
import dev.nyon.roler.utils.discordPartialEmoji
import dev.nyon.roler.utils.snowflake

val roleMessageUpdater = kord.on<GuildMessageCommandInteractionCreateEvent> {
    if (this.interaction.invokedCommandName != "Update Role Message") return@on
    val response = this.interaction.deferEphemeralResponse()
    val message = this.interaction.getTarget()
    message.edit {
        embed {
            generateEmbed()
        }
        actionRow {
            generateButtons()
        }
    }
    response.respond {
        content = "updated!"
    }
}

val commandHandler = kord.on<GuildChatInputCommandInteractionCreateEvent> {
    if (this.interaction.invokedCommandName != "sendrolemessage") return@on
    val response = this.interaction.deferEphemeralResponse()
    this.interaction.getChannel().createMessage {
        embed {
            generateEmbed()
        }
        actionRow {
            generateButtons()
        }
    }
    response.respond {
        content = "Message sent!"
    }
}

val buttonHandler = kord.on<GuildButtonInteractionCreateEvent> {
    if (!this.interaction.componentId.startsWith("role_")) return@on
    val response = this.interaction.deferEphemeralResponse()
    val role = guild.getRole(this.interaction.componentId.split("_")[1].snowflake)
    val member = this.interaction.user.asMember()
    if (member.roleIds.contains(role.id)) {
        member.removeRole(role.id)
        response.respond {
            content = "You removed the role: ${role.mention}!"
        }
        return@on
    }
    member.addRole(role.id)
    response.respond {
        content = "The role ${role.mention} was assigned to you!"
    }
}

private suspend fun EmbedBuilder.generateEmbed() {
    title = "**Roles**"
    val builder = StringBuilder("Click on the buttons below to assign them to yourself!\n")
    roleEntries.forEach {
        builder.append("\n ➜ ${it.getRole().mention}")
    }
    color = Color(0x738AFF)
    description = builder.toString()
}

private suspend fun ActionRowBuilder.generateButtons() {
    roleEntries.forEach {
        interactionButton(ButtonStyle.Secondary, "role_${it.id}") {
            label = it.getRole().name
            emoji = it.emoji.discordPartialEmoji()
        }
    }
}