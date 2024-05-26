package di


import SearchScreenModel
import data.remote.ImdbApiService
import data.repository.ImdbRepositoryImpl
import domain.repository.ImdbRepository
import domain.useCase.GetMovieCreditsUseCase
import domain.useCase.GetMovieDetailUseCase
import domain.useCase.GetMovieGenreListUseCase
import domain.useCase.GetMovieImagesUseCase
import domain.useCase.GetMovieProvidersUseCase
import domain.useCase.GetMovieSimilarUseCase
import domain.useCase.GetMovieVideosUseCase
import domain.useCase.GetPopularMoviesUseCase
import domain.useCase.GetSearchResultsUseCase
import domain.useCase.GetTopMoviesUseCase
import domain.useCase.GetTopRatedShowsUseCase
import domain.useCase.GetTrendingUseCase
import domain.useCase.GetTvGenreListUseCase
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.detailScreen.movieDetailScreen.MovieDetailScreenModel
import presentation.exploreScreen.ExploreScreenModel


val networkModule = module {
    single<ImdbApiService> { ImdbApiService() }
    single<ImdbRepository> { ImdbRepositoryImpl(get()) }
}
val useCaseModule = module {
    factoryOf(::GetPopularMoviesUseCase)
    factoryOf(::GetSearchResultsUseCase)
    factoryOf(::GetTopMoviesUseCase)
    factoryOf(::GetTopRatedShowsUseCase)
    factoryOf(::GetTrendingUseCase)
    factoryOf(::GetMovieGenreListUseCase)
    factoryOf(::GetTvGenreListUseCase)


    factoryOf(::GetMovieDetailUseCase)
    factoryOf(::GetMovieVideosUseCase)
    factoryOf(::GetMovieSimilarUseCase)
    factoryOf(::GetMovieProvidersUseCase)
    factoryOf(::GetMovieCreditsUseCase)
    factoryOf(::GetMovieImagesUseCase)
}
val screenModelsModule = module {
    factoryOf(::ExploreScreenModel)
    factoryOf(::SearchScreenModel)
    factoryOf(::MovieDetailScreenModel)
}



fun initKoin() {
    startKoin {
        modules(
            networkModule, useCaseModule,
            screenModelsModule
        )
    }
}


/*

val dataModule = module {
    single<ValorantService> { ValorantService() }
    single<ValorantRepository> { ValorantRepositoryImpl(get()) }
}

val useCaseModule = module {
    factoryOf(::GetAgentsUseCase)
    factoryOf(::GetAgentDetailUseCase)
    factoryOf(::GetCompetitiveTiersUseCase)
    factoryOf(::GetMapsUseCase)
    factoryOf(::GetMapDetailUseCase)
    factoryOf(::GetWeaponsUseCase)
    factoryOf(::GetWeaponDetailUseCase)
}

val screenModelsModule = module {
    factoryOf(::AgentsScreenModel)
    factoryOf(::AgentDetailScreenModel)
    factoryOf(::CompetitiveTiersScreenModel)
    factoryOf(::MapsScreenModel)
    factoryOf(::MapDetailScreenModel)
    factoryOf(::WeaponsScreenModel)
    factoryOf(::WeaponDetailScreenModel)
}

fun initKoin() = startKoin { modules(dataModule, useCaseModule, screenModelsModule) }



 */