package dev.nyon.roler

import dev.kord.cache.map.MapLikeCollection
import dev.kord.cache.map.internal.MapEntryCache
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.interaction.role
import dev.kord.rest.builder.interaction.string
import dev.nyon.roler.commands.buttonHandler
import dev.nyon.roler.commands.commandHandler
import dev.nyon.roler.commands.roleManageCommandHandler
import dev.nyon.roler.commands.roleMessageUpdater
import dev.nyon.roler.utils.initMongoDbs
import dev.nyon.roler.utils.roleCollection
import dev.nyon.roler.utils.snowflake


lateinit var kord: Kord
var roleEntries: ArrayList<RoleEntry> = arrayListOf()
val guildID = System.getenv("GUILD_ID").snowflake
lateinit var guild: Guild

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    initMongoDbs()
    roleCollection.find().toFlow().collect {
        roleEntries += it
    }

    kord = Kord(System.getenv("BOT_TOKEN")) {
        cache {
            members { cache, description -> MapEntryCache(cache, description, MapLikeCollection.none()) }
        }
    }

    kord.on<ReadyEvent> {
        kord.editPresence {
            status = PresenceStatus.Online
            competing("the role arena")
        }

        guild = kord.getGuild(guildID) ?: error("Guild cannot be found!")

        guild.getApplicationCommands().collect {
            it.delete()
        }

        kord.createGuildMessageCommand(guildID, "Update Role Message") {
            defaultMemberPermissions = Permissions(Permission.Administrator)
        }
        kord.createGuildChatInputCommand(
            guildID, "sendrolemessage", "Sends the message where you can self-assign your roles!"
        ) {
            defaultMemberPermissions = Permissions(Permission.Administrator)
        }

        kord.createGuildChatInputCommand(guildID, "removerole", "Removes the role from the database.") {
            defaultMemberPermissions = Permissions(Permission.Administrator)
            role("role", "The role to remove") { required = true }
        }
        kord.createGuildChatInputCommand(guildID, "addrole", "Adds the role to the database.") {
            defaultMemberPermissions = Permissions(Permission.Administrator)
            role("role", "The role to add") { required = true }
            string("emoji", "The emoji for the role") { required = true }
        }

        roleMessageUpdater
        commandHandler
        buttonHandler
        roleManageCommandHandler

        println("Bot started!")
    }

    kord.login {
        intents = Intents.all
    }
}