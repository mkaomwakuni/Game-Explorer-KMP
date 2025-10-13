package org.sea.rawg.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            useAlternativeNames = false
        }, contentType = ContentType.Application.Json)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30.seconds.inWholeMilliseconds
        connectTimeoutMillis = 15.seconds.inWholeMilliseconds
        socketTimeoutMillis = 30.seconds.inWholeMilliseconds
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }

    defaultRequest {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }
}