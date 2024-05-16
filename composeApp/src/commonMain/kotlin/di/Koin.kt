package di


import data.repository.ImdbRepository
import data.source.ImdbApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.ExploreScreenModel


val dataImdbModule = module {
    single {
        HttpClient {
            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNTQ3ODgzOGJjZGFhZDcwYzQyYmI1NGQ4NmM1MjFkZiIsInN1YiI6IjY2NDUyMTgwOTUwMTUxOWM5ZDFjNzQ1YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.luVDEWJuM2OooZXxsSr5XhlPVtWoHBznfcfe1Bhi7Og")
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<ImdbApiService> { ImdbApiService(get()) }
    single { ImdbRepository(get()) }
}
/*
val dataStreamModule = module {
    single {
        HttpClient {
            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    single<ImdbApiService> { ImdbApiService(get()) }
    single { ImdbRepository(get()) }
}
*/
val screenModelsModule = module {
    factoryOf(::ExploreScreenModel)
   // factoryOf(::presenta)
   // factoryOf(::DetailScreenModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataImdbModule,
            screenModelsModule,
           // dataStreamModule
        )
    }
}