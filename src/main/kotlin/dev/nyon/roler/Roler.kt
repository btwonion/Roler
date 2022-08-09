package dev.nyon.roler

import dev.kord.cache.map.MapLikeCollection
import dev.kord.cache.map.internal.MapEntryCache
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.PresenceStatus
import dev.kord.common.entity.optional.OptionalBoolean
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import dev.nyon.roler.utils.snowflake


lateinit var kord: Kord
lateinit var roleEntries: List<RoleEntry>
val guildID = System.getenv("GUILD_ID").snowflake
lateinit var guild: Guild

@OptIn(PrivilegedIntent::class)
suspend fun main() {

    kord = Kord(System.getenv("BOT_TOKEN")) {
        cache {
            members { cache, description -> MapEntryCache(cache, description, MapLikeCollection.none()) }
        }
    }

    val roleCache = hashMapOf<String, ArrayList<Pair<String, String>>>()

    System.getenv().filter { it.key != "BOT_TOKEN" && it.key != "GUILD_ID" && it.key != "PWD" }.forEach {
        val splitted = it.key.split("_")
        val entry = roleCache[splitted[0]]
        if (entry == null) roleCache[splitted[0]] = arrayListOf(splitted[1] to it.value)
        else {
            entry += splitted[1] to it.value
            roleCache[splitted[0]] = entry
        }
    }

    roleEntries = roleCache.map { (_, value) ->
        val emojiPair = value.filter { it.first == "emoji" }[0]
        val idPair = value.filter { it.first == "id" }[0]

        val customSplitted = emojiPair.second.split(":")
        val emoji = DiscordPartialEmoji(
            animated = OptionalBoolean.Value(customSplitted[0] == "<a"),
            name = customSplitted[1],
            id = customSplitted[2].removeSuffix(">").snowflake
        )

        RoleEntry(idPair.second.snowflake, emoji, emojiPair.second)
    }
    kord.on<ReadyEvent> {
        kord.editPresence {
            status = PresenceStatus.Online
            competing("your button clicks")
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

        roleMessageUpdater
        commandHandler
        buttonHandler

        println("Bot started!")
    }

    kord.login {
        intents = Intents.all
    }

}