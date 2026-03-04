package com.gcorp.shared.model

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource
import okio.use

object UserPreferencesSerializer : OkioSerializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences()

    override suspend fun readFrom(source: BufferedSource): UserPreferences {
        return try {
            Json.decodeFromString(UserPreferences.serializer(), source.readUtf8())
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserPreferences, sink: BufferedSink) {
        sink.use {
            it.writeUtf8(Json.encodeToString(UserPreferences.serializer(), t))
        }
    }
}
