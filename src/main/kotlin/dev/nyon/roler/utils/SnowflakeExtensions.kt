package dev.nyon.roler.utils

import dev.kord.common.entity.Snowflake

val String.snowflake: Snowflake
    get() = Snowflake(this)