package com.example.tire.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


//val AppHttpClient: HttpClient by lazy {
//    HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(
//                Json {
//                    prettyPrint = true
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                }
//            )
//        }
//    }
//}

