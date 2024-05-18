package di


import data.repository.ImdbRepository
import data.source.ImdbApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.exploreScreen.ExploreScreenModel
import presentation.searchScreen.SearchScreenModel
import presentation.searchScreen.SearchCard


val networkModule = module {
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
    factoryOf(::SearchScreenModel)
   // factoryOf(::presenta)
   // factoryOf(::DetailScreenModel)
}

fun initKoin() {
    startKoin {
        modules(
            networkModule,
            screenModelsModule,
        )
    }
}